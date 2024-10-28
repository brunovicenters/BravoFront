package com.example.bravofront.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bravofront.R
import com.example.bravofront.api.API
import com.example.bravofront.api.ARQUIVO_LOGIN
import com.example.bravofront.databinding.ActivityFinalCartBinding
import com.example.bravofront.model.*
import com.example.bravofront.views.adapters.FinalCartAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinalCartActivity : AppCompatActivity() {

    lateinit var binding: ActivityFinalCartBinding
    lateinit var sp: SharedPreferences

    var sumCars: Double = 0.0
//            sum = it.sumOf { it.price.toDouble() }
    var listItems = arrayListOf<CartFinalItem>()
    lateinit var adapter: FinalCartAdapter

    lateinit var listEnderecos: ArrayList<FinalCartEnderecos>
    var enderecoId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinalCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sp = getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE)

        binding.txtFinalPrice.text = formatPrice(sumCars, this.getString(R.string.country_currency), this.getString(R.string.language_currency))

        binding.imgBackBtnFinalBuy.setOnClickListener {
            goBack()
        }

        binding.btnBuyFinal.setOnClickListener {
            //TODO: Add produtos to cart
            // verify endereco id is selected
            // verify payment method is selected
            
            makeToast("Working on that", this)
        }

        adapter = FinalCartAdapter(listItems)
        binding.rvItens.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.rvItens.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        updateFinalCart()
    }

    fun updateFinalCart() {

        val callback = object : Callback<ApiResponse<CartFinal>> {
            override fun onResponse(call: Call<ApiResponse<CartFinal>>, res: Response<ApiResponse<CartFinal>>) {

                if (res.isSuccessful) {

                    val apiResponse = res.body()

                    apiResponse?.let {

                        val produtos = it.data.produtos
                        listItems.clear()
                        listItems.addAll(produtos)
                        adapter.notifyDataSetChanged()

                        produtos.forEach { it ->
                            sumCars += it.price.toDouble()
                        }

                        binding.txtFinalPrice.text = formatPrice( sumCars
                            , this@FinalCartActivity.getString(R.string.country_currency),
                            this@FinalCartActivity.getString(R.string.language_currency))

                        //enderecos
                        if (it.data.enderecos.isEmpty()) {
                            //TODO: Add new address activity
//                            val intent = Intent(this@FinalCartActivity, NewAddressActivity::class.java)
//                            intent.putExtra("from", "final")
//                            startActivity(intent)
                        }

                        setupSpinner(it.data.enderecos)
                    }
                } else {
                    Log.e("ERROR", res.errorBody().toString())
                    if (res.code() == 404 || res.code() == 401) {
                        makeToast("Falha ao carregar a tela de compra", this@FinalCartActivity)
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<CartFinal>>, t: Throwable) {

                makeToast("Não foi possível se conectar ao servidor", this@FinalCartActivity)

                Log.e("ERROR", "Falha ao executar serviço", t)
            }
        }

        val userId: Int = sp.getInt("user", -1)

        if (userId == -1) {
            makeToast("É necessário estar logado", this)
            finish()
        }

        val produtos = mutableListOf<Int>()

        getFinalCart(this@FinalCartActivity).let {
            it?.forEach { it ->
                produtos.add(it.id)
            }
        }

        if (produtos.isEmpty()) {
            makeToast("Nenhum item no carrinho", this)
            finish()
        }

        API(this).cart.final(CartFinalRequest(userId, produtos)).enqueue(callback)
    }

    fun setupSpinner(addressList: List<FinalCartEnderecos>) {
        val modifiedList = mutableListOf(
            FinalCartEnderecos(
                -1, "",
                "Selecione um endereço",
            ),
            *addressList.toTypedArray(),
            FinalCartEnderecos(
                -2, "",
                "Adicionar novo endereço"
            )
        )

        val spinnerItems = modifiedList.map {
            if (it.enderecoId == -1 || it.enderecoId == -2) {
                it.nome
            } else {
                "${it.nome} - ${it.logradouro}".trim()
            }
        }

        val adapterSpinner = ArrayAdapter(this, R.layout.spinner_item_layout, spinnerItems)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapterSpinner

        binding.spinner.setSelection(0)

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    enderecoId = -1
                    return
                } else if (modifiedList[position].enderecoId == -2) {
                    //TODO: Add new address activity
//                    val intent = Intent(this@FinalCartActivity, NewAddressActivity::class.java)
//                    intent.putExtra("from", "final")
//                    startActivity(intent)
                    makeToast("To be implemented", this@FinalCartActivity)
                    enderecoId = -1
                    return
                } else {
                    enderecoId = modifiedList[position].enderecoId
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun goBack() {
        val i = Intent(this, MainActivity::class.java)
        i.putExtra("frag",intent.getIntExtra("screen", R.id.home))
        startActivity(i)
        finish()
    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()

        goBack()
    }
}