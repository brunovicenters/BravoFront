package com.example.bravofront.views

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bravofront.R
import com.example.bravofront.api.API
import com.example.bravofront.api.ARQUIVO_LOGIN
import com.example.bravofront.databinding.ActivityProductShowBinding
import com.example.bravofront.model.*
import com.example.bravofront.views.adapters.ProductImagesAdapter
import com.example.bravofront.views.adapters.ProdutoSectionAdapter
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductShowActivity : AppCompatActivity() {

    lateinit var binding: ActivityProductShowBinding
    private var qtyAvaialable: Int = 0

    lateinit var adapterImages: ProductImagesAdapter
    val listImages = arrayListOf<Image>()

    lateinit var adapterAlike: ProdutoSectionAdapter
    val listAlike = arrayListOf<ProdutoIndex>()
    var headerAlike = ProdutoIndex()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        turnOnLoading(null, binding.progressBar, binding.nstProductShow)

        binding.btnBack.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            i.putExtra("frag",intent.getIntExtra("screen", R.id.home))
            startActivity(i)
            finish()
        }

        val txtQty = binding.txtQuantity

        binding.btnMinus.setOnClickListener {

            if (txtQty.text.toString().toInt() > 0) {
                txtQty.text = (txtQty.text.toString().toInt() - 1).toString()
            } else {
                makeToast("Quantidade mínima atingida", this)
            }
        }

        binding.btnPlus.setOnClickListener {
            if (txtQty.text.toString().toInt() < qtyAvaialable) {
                txtQty.text = (txtQty.text.toString().toInt() + 1).toString()
            } else {
                makeToast("Quantidade máxima atingida", this)
            }
        }

        binding.btnAddCart.setOnClickListener {
            makeToast("Not implemented yet", this)
        }

        binding.btnBuy.setOnClickListener {
            makeToast("Not implemented yet", this)
        }

        val layoutManagerAlike = GridLayoutManager(this, 2)
        layoutManagerAlike.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0 && listAlike.size % 2 != 0) 2 else 1
            }
        }

        adapterImages = ProductImagesAdapter(listImages, binding.imgProduct)
        binding.rvProductImages.adapter = adapterImages

        adapterAlike = ProdutoSectionAdapter(listAlike, headerAlike, null)
        binding.rvAlike.adapter = adapterAlike
        binding.rvAlike.layoutManager = layoutManagerAlike
    }

    override fun onResume() {
        super.onResume()

        updateProduct()
    }

    fun updateProduct() {

        val callback = object : Callback<ApiResponse<ProdutoShow>> {
            override fun onResponse(call: Call<ApiResponse<ProdutoShow>>, res: Response<ApiResponse<ProdutoShow>>) {
                turnOffLoading(null, binding.progressBar, binding.nstProductShow)

                if(res.isSuccessful) {

                    val apiResponse = res.body()

                    apiResponse?.let {
                        val data = apiResponse.data

                        val product = data.produto
                        val images = product.imagem
                        val alike = data.semelhantes

                        val user = this@ProductShowActivity.getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE).getInt("user", -1)
                        if (user != -1) {
                            setRecentlyViewed(this@ProductShowActivity, ProdutoIndex(product.preco, product.desconto, images[0].url, product.nome, product.id))
                        }

                        qtyAvaialable = product.qtd

                        Picasso.get()
                            .load(images[0].url.trim())
                            .error(R.drawable.no_car_img)
                            .into(binding.imgProduct)

                        binding.txtNameProduct.text = product.nome
                        binding.txtCategory.text = product.categoria

                        if(product.desconto != null && product.desconto.toDouble() > 0.0) {
                            val newPrice = (product.preco.toDouble()) - (product.desconto.toDouble())
                            binding.txtPrice.text = formatPrice(newPrice, this@ProductShowActivity.getString(R.string.country_currency), this@ProductShowActivity.getString(R.string.language_currency))
                            binding.txtDiscount.text = formatPrice(product.preco.toDouble(), this@ProductShowActivity.getString(R.string.country_currency), this@ProductShowActivity.getString(R.string.language_currency))
                            binding.txtDiscount.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                        } else {
                            binding.txtPrice.text = formatPrice(product.preco.toDouble(), this@ProductShowActivity.getString(R.string.country_currency), this@ProductShowActivity.getString(R.string.language_currency))
                            binding.txtDiscount.visibility = View.GONE
                        }

                        if(product.desc != null) {
                            binding.txtDescription.text = product.desc
                        } else {
                            binding.txtDescription.text = getString(R.string.no_description)
                        }

                        if (images.isNotEmpty()) {
                            listImages.clear()
                            listImages.addAll(images)
                            adapterImages.notifyDataSetChanged()
                        } else {
                            binding.rvProductImages.visibility = View.GONE
                        }



                        if (!alike.isNullOrEmpty()) {

                            Log.d("alike", alike.toString())

                            listAlike.clear()
                            listAlike.addAll(alike)
                            headerAlike = alike[0]

                            binding.rvAlike.visibility = View.VISIBLE
                            binding.txtAlike.visibility = View.VISIBLE

                            adapterAlike = ProdutoSectionAdapter(listAlike, headerAlike, null)
                            binding.rvAlike.adapter = adapterAlike

                            adapterAlike.notifyDataSetChanged()
                        }
                    }

                } else {
                    if (res.code() == 404 || res.code() == 401) {
                        makeToast("Falha ao carregar o produto", this@ProductShowActivity)

                        Log.e("ERROR", res.errorBody().toString())
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<ProdutoShow>>, t: Throwable) {
                turnOffLoading(null, binding.progressBar, binding.nstProductShow)

                makeToast("Não foi possível se conectar ao servidor", this@ProductShowActivity)

                Log.e("ERROR", "Falha ao executar serviço", t)
            }


        }

        API(null).produto.show(intent.getIntExtra("id", -1)).enqueue(callback)

        turnOnLoading(null, binding.progressBar, binding.nstProductShow)
    }
}