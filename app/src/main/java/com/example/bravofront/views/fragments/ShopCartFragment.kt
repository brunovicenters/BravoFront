package com.example.bravofront.views.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bravofront.R
import com.example.bravofront.api.API
import com.example.bravofront.api.ARQUIVO_LOGIN
import com.example.bravofront.databinding.FragmentShopCartBinding
import com.example.bravofront.model.ApiResponse
import com.example.bravofront.model.CartIndex
import com.example.bravofront.model.CartItem
import com.example.bravofront.views.*
import com.example.bravofront.views.adapters.ShopCartAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopCartFragment : Fragment() {

    lateinit var binding: FragmentShopCartBinding

    lateinit var shopCartAdapter: ShopCartAdapter
    val listShopCart = arrayListOf<CartItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val sp = requireContext().getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE)

        if (!(sp.getString("email", "") != "" && sp.getString("password", "") != "")) {
            val i = Intent(requireContext(), LoginActivity::class.java)
            i.putExtra("shopcart", true)
            i.putExtra("frag", R.id.shopcart)
            startActivity(i)
        }

            binding = FragmentShopCartBinding.inflate(inflater, container, false)

        turnOnLoading(binding.swpRefresh, null, binding.nstScrollShopCart)

        binding.btnBuyShopCart.setOnClickListener {
            if (getFinalCart(requireContext()) == null || getFinalCart(requireContext())!!.isEmpty()) {
                makeToast("You need to select at least one product", requireContext())
                return@setOnClickListener
            }

            val i = Intent(requireContext(), FinalCartActivity::class.java)
            i.putExtra("screen", R.id.shopcart)
            startActivity(i)
            requireActivity().finish()
        }

        binding.swpRefresh.setOnRefreshListener {
            updateCart()
        }

        binding.rvCartItems.layoutManager = LinearLayoutManager(requireContext())

        shopCartAdapter = ShopCartAdapter(listShopCart, requireContext()) { updateCart() }

        binding.rvCartItems.adapter = shopCartAdapter

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateCart()
    }

    fun updateCart() {

        val callback = object : Callback<ApiResponse<CartIndex>> {
            override fun onResponse(call: Call<ApiResponse<CartIndex>>, res: Response<ApiResponse<CartIndex>>) {
                turnOffLoading(binding.swpRefresh, null, binding.nstScrollShopCart)

                if (!(isAdded && context != null)) {
                    return
                }

                if (res.isSuccessful) {

                    val apiResponse = res.body()

                    apiResponse?.let {

                        listShopCart.clear()
                        listShopCart.addAll(it.data.carrinho)

                        if (listShopCart.isEmpty()) {
                            binding.txtNoProducts.visibility = View.VISIBLE
                            binding.rvCartItems.visibility = View.GONE
                        }

                        removeAllFromFinalCartBut(requireContext(), listShopCart)

                        shopCartAdapter.notifyDataSetChanged()
                    }
                } else {
                    if (!(isAdded && context != null)) {
                        return
                    }
                    Log.e("ERROR", res.errorBody().toString())
                    if (res.code() == 404 || res.code() == 401) {
                        makeToast("Falha ao carregar o carrinho", requireContext())
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<CartIndex>>, t: Throwable) {
                turnOffLoading(binding.swpRefresh, null, binding.nstScrollShopCart)

                makeToast("Não foi possível se conectar ao servidor", requireContext())

                Log.e("ERROR", "Falha ao executar serviço", t)
            }
        }

        API(requireContext()).cart.index().enqueue(callback)
        turnOnLoading(binding.swpRefresh, null, binding.nstScrollShopCart)

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ShopCartFragment()
    }
}