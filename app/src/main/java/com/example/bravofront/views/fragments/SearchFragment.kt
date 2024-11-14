package com.example.bravofront.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bravofront.R
import com.example.bravofront.api.API
import com.example.bravofront.databinding.FragmentHomeBinding
import com.example.bravofront.databinding.FragmentSearchBinding
import com.example.bravofront.model.ApiResponse
import com.example.bravofront.model.ProdutoIndex
import com.example.bravofront.model.Search
import com.example.bravofront.views.adapters.ProdutoCardRecyclerViewAdapter
import com.example.bravofront.views.makeToast
import com.example.bravofront.views.turnOffLoading
import com.example.bravofront.views.turnOnLoading
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    lateinit var adapterProductAdapter: ProdutoCardRecyclerViewAdapter
    val listProdutos = arrayListOf<ProdutoIndex>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        adapterProductAdapter = ProdutoCardRecyclerViewAdapter(listProdutos, R.id.search)
        binding.rvSearch.adapter = adapterProductAdapter
        binding.rvSearch.layoutManager = GridLayoutManager(context, 2)

        turnOnLoading(binding.swpRefresh, null, binding.nstScrollSearch)

        binding.swpRefresh.setOnRefreshListener {
            updateProdutos()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateProdutos()
    }

    fun updateProdutos() {
        turnOnLoading(binding.swpRefresh, null, binding.nstScrollSearch)


        val callback = object : Callback<ApiResponse<Search>> {
            override fun onResponse(call: Call<ApiResponse<Search>>, res: Response<ApiResponse<Search>>) {

                if (!(isAdded && context != null)) {
                    return
                }

                turnOffLoading(binding.swpRefresh, null, binding.nstScrollSearch)


                if (res.isSuccessful) {

                    val apiResponse = res.body()

                    apiResponse?.let {

                        listProdutos.clear()
                        listProdutos.addAll(it.data.produtos)
                        adapterProductAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e("ERROR", res.errorBody().toString())
                    if (res.code() == 404 || res.code() == 401) {
                        makeToast("Falha ao carregar a busca", requireContext())
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<Search>>, t: Throwable) {
                if (!(isAdded && context != null)) {
                    return
                }

                turnOffLoading(binding.swpRefresh, null, binding.nstScrollSearch)
                makeToast("Falha ao carregar a busca", requireContext())
                Log.e("ERROR", "Falha ao executar serviço", t)
            }
        }

        API(null).produto.index().enqueue(callback)

        turnOnLoading(binding.swpRefresh, null, binding.nstScrollSearch)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SearchFragment()
    }
}