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
import com.example.bravofront.databinding.FragmentHomeBinding
import com.example.bravofront.model.ApiResponse
import com.example.bravofront.model.Home
import com.example.bravofront.model.ProdutoIndex
import com.example.bravofront.views.adapters.ImagePagerAdapter
import com.example.bravofront.views.adapters.ProdutoSectionAdapter
import com.example.bravofront.views.turnOffLoading
import com.example.bravofront.views.turnOnLoading
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//private const val ARG_PARAM2 = "param2"

private const val AUTO_SCROLL_DELAY = 2500L

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    lateinit var adapterPromoAdapter: ProdutoSectionAdapter
    val listPromo = arrayListOf<ProdutoIndex>()
    var headerPromo = ProdutoIndex()

    lateinit var adapterMostSelledAdapter : ProdutoSectionAdapter
    val listMostSelled = arrayListOf<ProdutoIndex>()
    var headerMostSelled = ProdutoIndex()

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

        Log.d("HomeFragment", "Initializing fragment")
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        val adapterImg = ImagePagerAdapter(this, images)
        binding.viewPager.adapter = adapterImg
        startAutoScroll()

        turnOnLoading(binding.swpRefresh, binding.progressBar, binding.nstScrollHome)

        val layoutManagerPromo = GridLayoutManager(context, 2)
        layoutManagerPromo.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
               return if (position == 0) 2 else 1
            }
        }

        val layoutManagerMostSelled = GridLayoutManager(context, 2)
        layoutManagerMostSelled.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0) 2 else 1
            }
        }

        adapterPromoAdapter = ProdutoSectionAdapter(listPromo, headerPromo)
        adapterMostSelledAdapter = ProdutoSectionAdapter(listMostSelled, headerMostSelled)

        binding.recyclePromo.layoutManager = layoutManagerPromo
        binding.recycleMostSelled.layoutManager = layoutManagerMostSelled

        binding.recyclePromo.adapter = adapterPromoAdapter
        binding.recycleMostSelled.adapter = adapterMostSelledAdapter

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

                        val data = apiResponse.data

                        if (data.promocao.isNotEmpty()) {
                            listPromo.clear()
                            listPromo.addAll(data.promocao)
                            headerPromo = data.promocao[0]

                            binding.txtPromo.visibility = View.VISIBLE
                            binding.recyclePromo.visibility = View.VISIBLE

                            adapterPromoAdapter = ProdutoSectionAdapter(listPromo, headerPromo)
                            binding.recyclePromo.adapter = adapterPromoAdapter
                            adapterPromoAdapter.notifyDataSetChanged()
                        }

                        if (data.produtosMaisVendidos.isNotEmpty()) {
                            listMostSelled.clear()
                            listMostSelled.addAll(data.produtosMaisVendidos)
                            headerMostSelled = data.produtosMaisVendidos[0]

                            binding.txtMostSelled.visibility = View.VISIBLE
                            binding.recycleMostSelled.visibility = View.VISIBLE

                            adapterMostSelledAdapter = ProdutoSectionAdapter(listMostSelled, headerMostSelled)
                            binding.recycleMostSelled.adapter = adapterMostSelledAdapter
                            adapterMostSelledAdapter.notifyDataSetChanged()
                        }
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
    }


    companion object {
        @JvmStatic

        fun newInstance() =
            HomeFragment()
    }
}