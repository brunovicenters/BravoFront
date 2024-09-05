package com.example.bravofront.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bravofront.R
import com.example.bravofront.api.API
import com.example.bravofront.api.HomeApi
import com.example.bravofront.databinding.FragmentHomeBinding
import com.example.bravofront.model.ApiResponse
import com.example.bravofront.model.Home
import com.example.bravofront.model.ProdutoIndex
import com.example.bravofront.views.ImagePagerAdapter
import com.example.bravofront.views.ProdutoCardRecyclerViewAdapter
import com.example.bravofront.views.turnOffLoading
import com.example.bravofront.views.turnOnLoading
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//private const val ARG_PARAM2 = "param2"

private const val AUTO_SCROLL_DELAY = 2500L

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    lateinit var adapterProductAdapter: ProdutoCardRecyclerViewAdapter
    val listPromo = arrayListOf<ProdutoIndex>()

    private val images = listOf(R.drawable.upfoto, R.drawable.fiatmobi, R.drawable.novasaveiro)
    private val autoScrollHandler = Handler(Looper.getMainLooper())
    private val autoScrollRunnable = object : Runnable {
        override fun run() {
            val currentItem = binding.viewPager.currentItem
            val nextItem = (currentItem + 1) % images.size
            binding.viewPager.setCurrentItem(nextItem, true)
            autoScrollHandler.postDelayed(this, AUTO_SCROLL_DELAY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        val adapterImg = ImagePagerAdapter(this, images)
        binding.viewPager.adapter = adapterImg
        startAutoScroll()

        turnOnLoading(binding.swpRefresh, binding.progressBar, binding.nstScrollHome)

        adapterProductAdapter = ProdutoCardRecyclerViewAdapter(listPromo)

        binding.recyclePromo.layoutManager = GridLayoutManager(context, 2)
        binding.recyclePromo.adapter = adapterProductAdapter

        binding.swpRefresh.setOnRefreshListener {
            updateProdutos()
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        updateProdutos()
    }

    private fun startAutoScroll() {
        autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY)
    }

    private fun stopAutoScroll() {
        autoScrollHandler.removeCallbacks(autoScrollRunnable)
    }

    fun updateProdutos() {
        val callback = object : Callback<ApiResponse<Home>> {
            override fun onResponse(call: Call<ApiResponse<Home>>, res: Response<ApiResponse<Home>>) {
                turnOffLoading(binding.swpRefresh, binding.progressBar, binding.nstScrollHome)

                if(res.isSuccessful) {
                   val apiResponse = res.body()
                    apiResponse?.let {
                        listPromo.clear()
                        listPromo.addAll(apiResponse.data.promocao)
                        adapterProductAdapter.notifyDataSetChanged()
                    }
                } else {
                    if (res.code() == 404 || res.code() == 401) {
                        Snackbar
                            .make(binding.containerHome,
                                "Erro ao carregar promocoes",
                                Snackbar.LENGTH_SHORT)
                            .show()

                        Log.e("ERROR", res.errorBody().toString())
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<Home>>, t: Throwable) {
                turnOffLoading(binding.swpRefresh, binding.progressBar, binding.nstScrollHome)

                Snackbar
                    .make(binding.containerHome,
                    "Não foi possível se conectar ao servidor",
                    Snackbar.LENGTH_SHORT)
                    .show()

                Log.e("ERROR", "Falha ao executar serviço", t)
            }
        }

        API.home.index().enqueue(callback)

        turnOnLoading(binding.swpRefresh, binding.progressBar, binding.nstScrollHome)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopAutoScroll()
        _binding = null
    }


    companion object {
        @JvmStatic

        fun newInstance(param1: String, param2: String) =
            HomeFragment()
    }
}