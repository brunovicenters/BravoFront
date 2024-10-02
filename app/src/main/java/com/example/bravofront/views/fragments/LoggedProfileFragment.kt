package com.example.bravofront.views.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bravofront.R
import com.example.bravofront.api.API
import com.example.bravofront.api.ARQUIVO_LOGIN
import com.example.bravofront.databinding.FragmentLoggedProfileBinding
import com.example.bravofront.databinding.FragmentUnLoggedProfileBinding
import com.example.bravofront.model.ApiResponse
import com.example.bravofront.model.Login
import com.example.bravofront.model.ProdutoIndex
import com.example.bravofront.model.ProfileShow
import com.example.bravofront.views.*
import com.example.bravofront.views.adapters.ProdutoCardRecyclerViewAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoggedProfileFragment(val ctx: Context) : Fragment() {

    lateinit var binding: FragmentLoggedProfileBinding

    lateinit var buyAgainAdapter: ProdutoCardRecyclerViewAdapter
    val listBuyAgain = arrayListOf<ProdutoIndex>()

    var isRV: Boolean = false
    lateinit var recentlyViewedAdapter: ProdutoCardRecyclerViewAdapter
    val listRecViewed = getRecentlyViewed(ctx)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoggedProfileBinding.inflate(inflater, container, false)

        binding.btnLogout.setOnClickListener {
            val sp = ctx.getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE)

            val edit = sp.edit()

            edit.remove("user")
            edit.remove("email")
            edit.remove("password")
            edit.putBoolean("keepLogin", false)

            edit.apply()

            val intent = Intent(ctx, MainActivity::class.java)
            intent.putExtra("frag", R.id.home)
            startActivity(intent)
        }

        binding.btnTermosECondicoes.setOnClickListener {
            val intent = Intent(ctx, TermAndConditionsActivity::class.java)
            startActivity(intent)
        }

        binding.btnSac.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:11990225294"))
            startActivity(intent)
        }

        binding.btnPedidos.setOnClickListener {
            //TODO: Add pedidos
            makeToast("Not Implemented Yet", ctx)
        }

        binding.btnEnderecos.setOnClickListener {
            //TODO: Add endereços
            makeToast("Not Implemented Yet", ctx)
        }

        binding.btnSettings.setOnClickListener {
            //TODO: Add Configurações
            makeToast("Not Implemented Yet", ctx)
        }

        binding.btnEdit.setOnClickListener {
            //TODO: Add Edit Profile
            makeToast("Not Implemented Yet", ctx)
        }

        turnOnLoading(binding.swpRefresh, binding.progressBar, binding.nstProfileShow)

        buyAgainAdapter = ProdutoCardRecyclerViewAdapter(listBuyAgain, R.id.profile)

        if (!listRecViewed.isNullOrEmpty()) {
            isRV = true
            recentlyViewedAdapter = ProdutoCardRecyclerViewAdapter(listRecViewed, R.id.profile)
            binding.rvVistosRecentemente.adapter = recentlyViewedAdapter
        }

        binding.rvCompreNovamente.adapter = buyAgainAdapter

        binding.swpRefresh.setOnRefreshListener {
            updateProfile()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        updateProfile()
    }

    fun updateProfile() {
        val callback = object : Callback<ApiResponse<ProfileShow>> {
            override fun onResponse(
                c: Call<ApiResponse<ProfileShow>>,
                res: Response<ApiResponse<ProfileShow>>
            ) {
                if (res.isSuccessful) {

                    val apiResponse = res.body()

                    apiResponse?.let {

                        val profile = it.data

                        val user = profile.user

                        if (user.name.length > 18) {
                            val newText = user.name.take(18) + "..."
                            binding.nome.text = newText
                        } else {
                            binding.nome.text = user.name
                        }

                        if (user.email.length > 28) {
                            val newText = user.email.take(28) + "..."
                            binding.email.text = newText
                        } else {
                            binding.email.text = user.email
                        }

                        binding.cpf.text = user.cpf

                        val buyAgain = profile.compreNovamente
                        if (buyAgain.isNotEmpty()) {
                            listBuyAgain.clear()
                            listBuyAgain.addAll(buyAgain)
                            binding.txtCompreNovamente.visibility = View.VISIBLE
                            binding.rvCompreNovamente.visibility = View.VISIBLE
                            buyAgainAdapter.notifyDataSetChanged()
                        }

                        if (isRV) {
                            binding.txtVistosRecentemente.visibility = View.VISIBLE
                            binding.rvVistosRecentemente.visibility = View.VISIBLE
                        }

                    }

                } else {
                    makeToast("Erro ao carregar perfil", ctx)
                }

            }

            override fun onFailure(c: Call<ApiResponse<ProfileShow>>, t: Throwable) {
                makeToast(getString(R.string.failed_connect_server), ctx)

                Log.e("ERROR", getString(R.string.failed_execute_server), t)
            }

        }

        API(ctx).profile.show(ctx.getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE).getInt("user", -1).toString()).enqueue(callback)

        turnOffLoading(binding.swpRefresh, binding.progressBar, binding.nstProfileShow)
    }

    companion object {
        @JvmStatic
        fun newInstance(ctx: Context) =
            LoggedProfileFragment(ctx)
    }
}