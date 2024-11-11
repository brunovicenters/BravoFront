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
    var listItems = arrayListOf<CartFinalItem>()
    lateinit var adapter: FinalCartAdapter

    var enderecoId: Int = -1

    var paymentMethodSelected: String = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinalCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sp = getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE)

        binding.txtFinalPrice.text = formatPrice(sumCars, this.getString(R.string.country_currency), this.getString(R.string.language_currency))

        binding.imgBackBtnFinalBuy.setOnClickListener {
            goBack()
        }

        binding.rgPaymentMethod.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbBoleto -> paymentMethodSelected = "boleto"
                R.id.rbCC -> paymentMethodSelected = "cartao"
                R.id.rbPix -> paymentMethodSelected = "pix"
            }
        }

        binding.btnBuyFinal.setOnClickListener {
            //TODO: Add produtos to cart
            // verify endereco id is selected
            if (enderecoId == -1) {
                makeToast("Selecione um endereço", this)
                return@setOnClickListener
            }

            // verify payment method is selected
            if (paymentMethodSelected == "") {
                makeToast("Selecione um meio de pagamento", this)
                return@setOnClickListener
            }

//            makeToast("Working on that", this)

            makePedido()
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

                turnOffLoading(null, binding.progressBar, null)

                if (res.isSuccessful) {

                    val apiResponse = res.body()

                    apiResponse?.let {

                        val produtos = it.data.produtos
                        listItems.clear()
                        listItems.addAll(produtos)
                        adapter.notifyDataSetChanged()

                        sumCars = 0.0

                        produtos.forEach { it ->
                            sumCars += it.price.toDouble() * it.quantity
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
                turnOffLoading(null, binding.progressBar, null)

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

        turnOnLoading(null, binding.progressBar, null)
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

    fun makePedido() {

        val callback = object : Callback<ApiResponse<StorePedidoResponse>> {
            override fun onResponse(call: Call<ApiResponse<StorePedidoResponse>>, res: Response<ApiResponse<StorePedidoResponse>>) {

                turnOffLoading(null, binding.progressBar, null)

                if (res.isSuccessful) {
                    val apiResponse = res.body()

                    apiResponse?.let {

                        if (it.data.pedido == -1) {
                            makeToast("Falha ao realizar pedido: Um ou mais produtos estão fora de estoque.", this@FinalCartActivity)
                        }

                        makeToast("Pedido realizado com sucesso", this@FinalCartActivity)

                        val i = Intent(this@FinalCartActivity, MainActivity::class.java)
                        i.putExtra("frag", R.id.profile)
                        startActivity(i)
                        finish()
                    }

                } else {
                    Log.e("ERROR", res.errorBody().toString())
                    if (res.code() == 404 || res.code() == 401) {
                        makeToast("Falha ao carregar pedido", this@FinalCartActivity)
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<StorePedidoResponse>>, t: Throwable) {
                turnOffLoading(null, binding.progressBar, null)

                makeToast("Não foi possível se conectar ao servidor", this@FinalCartActivity)

                Log.e("ERROR", "Falha ao executar serviço", t)
            }
        }

        val produtos = mutableListOf<ProdutoPedidoRequest>()

        getFinalCart(this@FinalCartActivity).let {
            it?.forEach { produto ->
                produtos.add(ProdutoPedidoRequest(produto.id, produto.quantity, produto.price))
            }
        }

        Log.d("PRODUTOS", produtos.toString())

        API(this).pedido.create(StorePedidoRequest(enderecoId, produtos)).enqueue(callback)

        turnOnLoading(null, binding.progressBar, null)
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