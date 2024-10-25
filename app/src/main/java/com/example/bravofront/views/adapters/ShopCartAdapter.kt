package com.example.bravofront.views.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat.getString
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.bravofront.R
import com.example.bravofront.api.API
import com.example.bravofront.api.ARQUIVO_LOGIN
import com.example.bravofront.databinding.ShopcartItemBinding
import com.example.bravofront.model.*
import com.example.bravofront.views.*
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopCartAdapter (private val list: List<CartItem>, private val ctx: Context, private val updateCart: () -> Unit): RecyclerView.Adapter<ShopCartAdapter.ViewHolder>() {

    class ViewHolder (private val binding: ShopcartItemBinding, private val ctx: Context, private val updateCart: () -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem) {

            binding.imgProduto.setOnClickListener {
                val i = Intent(ctx, ProductShowActivity::class.java)
                i.putExtra("id", item.id)
                i.putExtra("screen", R.id.shopcart)
                startActivity(ctx, i, null)
                (ctx as Activity).finish()
            }

            var changed = 0

            val finalCart = getFinalCart(ctx)

            if (item.changedQty) {
                binding.txtOutOfStock.visibility = View.VISIBLE
            }

            if (item.name.length > 14) {
                val newText = item.name.take(14) + "..."
                binding.txtNomeProduto.text = newText
            } else {
                binding.txtNomeProduto.text = item.name
            }

            binding.txtPriceCart.text = formatPrice(item.price.toDouble(), ctx.getString(R.string.country_currency), ctx.getString(R.string.language_currency))

            if (!(item.image == null || item.image == "")) {
                Picasso.get()
                    .load(item.image.trim())
                    .error(R.drawable.no_car_img)
                    .into(binding.imgProduto)
            }

            val isInCart = finalCart?.indexOfFirst { it.id == item.id } ?: -1 > -1
            binding.cbAddCart.isChecked = isInCart

            binding.cbAddCart.setOnCheckedChangeListener { _, isChecked ->

                if (isChecked) {
                    addToFinalCart(ctx, item)
                    binding.txtOutOfStock.visibility = View.GONE
                } else {
                    removeFromFinalCart(ctx, item.id)
                }

            }

            val itensSpinner = IntArray(item.stock) { it + 1 }.toList()

            val adapterSpinner = ArrayAdapter(ctx, R.layout.spinner_item_layout, itensSpinner)

            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            binding.spinner.adapter = adapterSpinner

            binding.spinner.setSelection(item.quantity - 1)

            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                    binding.txtOutOfStock.visibility = View.GONE

                    val callback = object : Callback<ApiResponse<CartUpdate>> {
                        override fun onResponse(call: Call<ApiResponse<CartUpdate>>, res: Response<ApiResponse<CartUpdate>>) {

                            if(res.isSuccessful) {

                                val apiResponse = res.body()

                                apiResponse?.let {
                                    val data = apiResponse.data

                                    makeToast(data.msg, ctx)

                                    addToFinalCart(ctx,
                                            CartItem(
                                                    item.id,
                                                    item.name,
                                                    itensSpinner[position],
                                                    item.stock,
                                                    item.price,
                                                    item.image,
                                                    false
                                            )
                                        )

                                    updateCart()
                                }
                            } else {
                                Log.e("ERROR", res.errorBody().toString())
                                if (res.code() == 404 || res.code() == 401) {
                                    makeToast("Falha ao carregar o produto", ctx)

                                    Log.e("ERROR", res.errorBody().toString())
                                }
                            }
                        }

                        override fun onFailure(call: Call<ApiResponse<CartUpdate>>, t: Throwable) {

                            makeToast("Não foi possível se conectar ao servidor", ctx)

                            Log.e("ERROR", "Falha ao executar serviço", t)
                        }
                    }


                    if (changed > 0) {
                        API(ctx).cart.update(CartUpdateRequest(item.id, itensSpinner[position]), itensSpinner[position])
                            .enqueue(callback)
                    }

                    changed += 1

                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }

            binding.btnDelete.setOnClickListener {


                showDialog(ctx, "Deletar", "Deletar Item do Carrinho", "Tem certeza de que quer remover o item do carrinho?") { deleteFromCart(item) }



            }

        }

        private fun deleteFromCart(item: CartItem) {
            val callback = object : Callback<ApiResponse<CartUpdate>> {
                override fun onResponse(call: Call<ApiResponse<CartUpdate>>, res: Response<ApiResponse<CartUpdate>>) {

                    if(res.isSuccessful) {

                        val apiResponse = res.body()

                        apiResponse?.let {
                            val data = apiResponse.data

                            when (data.msg) {
                                "not found" -> {
                                    makeToast("Produto não encontrado", ctx)
                                }
                                "error" -> {
                                    makeToast("Falha ao remover produto do carrinho", ctx)
                                }
                                "deleted" -> {
                                    makeToast("Produto removido com sucesso", ctx)
                                }
                            }

                            removeFromFinalCart(ctx, item.id)

                            updateCart()
                        }
                    } else {
                        Log.e("ERROR", res.errorBody().toString())
                        if (res.code() == 404 || res.code() == 401) {
                            makeToast("Falha ao carregar o produto", ctx)

                            Log.e("ERROR", res.errorBody().toString())
                        }
                    }
                }

                override fun onFailure(call: Call<ApiResponse<CartUpdate>>, t: Throwable) {

                    makeToast(getString(ctx, R.string.failed_connect_server), ctx)

                    Log.e("ERROR", "Falha ao executar serviço", t)
                }
            }

            Log.e("ERROR", item.id.toString())

            API(ctx).cart.delete(item.id).enqueue(callback)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val shopBinding = ShopcartItemBinding.inflate(layoutInflater)

        return ViewHolder(shopBinding, ctx, updateCart)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}