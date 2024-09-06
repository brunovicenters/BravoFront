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

    lateinit var adapterMostSoldAdapter : ProdutoSectionAdapter
    val listMostSold = arrayListOf<ProdutoIndex>()
    var headerMostSold = ProdutoIndex()

    lateinit var adapterFirstCategoryAdapter : ProdutoSectionAdapter
    val listFirstCategory = arrayListOf<ProdutoIndex>()
    var headerFirstCategory = ProdutoIndex()

    lateinit var adapterSecondCategoryAdapter : ProdutoSectionAdapter
    val listSecondCategory = arrayListOf<ProdutoIndex>()
    var headerSecondCategory = ProdutoIndex()

    lateinit var adapterThirdCategoryAdapter : ProdutoSectionAdapter
    val listThirdCategory = arrayListOf<ProdutoIndex>()
    var headerThirdCategory = ProdutoIndex()


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

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        val adapterImg = ImagePagerAdapter(this, images)
        binding.viewPager.adapter = adapterImg
        startAutoScroll()

        turnOnLoading(binding.swpRefresh, binding.progressBar, binding.nstScrollHome)

        val layoutManagerPromo = GridLayoutManager(context, 2)
        layoutManagerPromo.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
               return if (position == 0 && listPromo.size % 2 != 0) 2 else 1
            }
        }

        val layoutManagerMostSold = GridLayoutManager(context, 2)
        layoutManagerMostSold.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0 && listMostSold.size % 2 != 0) 2 else 1
            }
        }

        val layoutManagerFirstCategory = GridLayoutManager(context, 2)
        layoutManagerFirstCategory.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0 && listFirstCategory.size % 2 != 0) 2 else 1
            }
        }

        val layoutManagerSecondCategory = GridLayoutManager(context, 2)
        layoutManagerSecondCategory.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0 && listSecondCategory.size % 2 != 0) 2 else 1
            }
        }

        val layoutManagerThirdCategory = GridLayoutManager(context, 2)
        layoutManagerThirdCategory.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0 && listThirdCategory.size % 2 != 0) 2 else 1
            }
        }

        adapterPromoAdapter = ProdutoSectionAdapter(listPromo, headerPromo)
        adapterMostSoldAdapter = ProdutoSectionAdapter(listMostSold, headerMostSold)
        adapterFirstCategoryAdapter = ProdutoSectionAdapter(listFirstCategory, headerFirstCategory)
        adapterSecondCategoryAdapter = ProdutoSectionAdapter(listSecondCategory, headerSecondCategory)
        adapterThirdCategoryAdapter = ProdutoSectionAdapter(listThirdCategory, headerThirdCategory)

        binding.recyclePromo.layoutManager = layoutManagerPromo
        binding.recycleMostSold.layoutManager = layoutManagerMostSold
        binding.recycleFirstCategory.layoutManager = layoutManagerFirstCategory
        binding.recycleSecondCategory.layoutManager = layoutManagerSecondCategory
        binding.recycleThirdCategory.layoutManager = layoutManagerThirdCategory

        binding.recyclePromo.adapter = adapterPromoAdapter
        binding.recycleMostSold.adapter = adapterMostSoldAdapter
        binding.recycleFirstCategory.adapter = adapterFirstCategoryAdapter
        binding.recycleSecondCategory.adapter = adapterSecondCategoryAdapter
        binding.recycleThirdCategory.adapter = adapterThirdCategoryAdapter

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
                            listMostSold.clear()
                            listMostSold.addAll(data.produtosMaisVendidos)
                            headerMostSold = data.produtosMaisVendidos[0]

                            binding.txtMostSelled.visibility = View.VISIBLE
                            binding.recycleMostSold.visibility = View.VISIBLE

                            adapterMostSoldAdapter = ProdutoSectionAdapter(listMostSold, headerMostSold)
                            binding.recycleMostSold.adapter = adapterMostSoldAdapter
                            adapterMostSoldAdapter.notifyDataSetChanged()
                        }

                        if (data.categoriasMaisVendidas.isNotEmpty()) {

                            val cMV = data.categoriasMaisVendidas

                            if (cMV[0].produtos.isNotEmpty()) {

                                val fc = cMV[0]

                                listFirstCategory.clear()
                                listFirstCategory.addAll(fc.produtos)
                                headerFirstCategory = fc.produtos[0]

                                binding.txtFirstCategory.text = fc.categoria.nome
                                binding.txtFirstCategory.visibility = View.VISIBLE
                                binding.recycleFirstCategory.visibility = View.VISIBLE

                                adapterFirstCategoryAdapter = ProdutoSectionAdapter(listFirstCategory, headerFirstCategory)
                                binding.recycleFirstCategory.adapter = adapterFirstCategoryAdapter
                                adapterFirstCategoryAdapter.notifyDataSetChanged()
                            }

                            if (cMV[1].produtos.isNotEmpty()) {
                                val sc = cMV[1]

                                listSecondCategory.clear()
                                listSecondCategory.addAll(sc.produtos)
                                headerSecondCategory = sc.produtos[0]

                                binding.txtSecondCategory.text = sc.categoria.nome
                                binding.txtSecondCategory.visibility = View.VISIBLE
                                binding.recycleSecondCategory.visibility = View.VISIBLE

                                adapterSecondCategoryAdapter = ProdutoSectionAdapter(listSecondCategory, headerSecondCategory)
                                binding.recycleSecondCategory.adapter = adapterSecondCategoryAdapter
                                adapterSecondCategoryAdapter.notifyDataSetChanged()
                            }

                            if (cMV[2].produtos.isNotEmpty()) {
                                val tc = cMV[2]

                                listThirdCategory.clear()
                                listThirdCategory.addAll(tc.produtos)
                                headerThirdCategory = tc.produtos[0]

                                Log.d("HEADER", headerThirdCategory.toString())
                                Log.d("LIST", tc.produtos.size.toString())

                                binding.txtThirdCategory.text = tc.categoria.nome
                                binding.txtThirdCategory.visibility = View.VISIBLE
                                binding.recycleThirdCategory.visibility = View.VISIBLE

                                adapterThirdCategoryAdapter = ProdutoSectionAdapter(listThirdCategory, headerThirdCategory)
                                binding.recycleThirdCategory.adapter = adapterThirdCategoryAdapter
                                adapterThirdCategoryAdapter.notifyDataSetChanged()
                            }


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