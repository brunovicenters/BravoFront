package com.example.bravofront.views.adapters

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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

class ShopCartAdapter (private val list: List<CartItem>, private val ctx: Context): RecyclerView.Adapter<ShopCartAdapter.ViewHolder>() {

    class ViewHolder (private val binding: ShopcartItemBinding, private val ctx: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem) {

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

            binding.cbAddCart.setOnCheckedChangeListener { _, isChecked ->

                if (isChecked) {
                    addToFinalCart(ctx, item)
                    binding.txtOutOfStock.visibility = View.GONE
                } else {
                    removeFromFinalCart(ctx, item.id)
                }

            }

            if (finalCart != null) {
                if (finalCart.indexOfFirst { it.id == item.id } > -1) {
                    binding.cbAddCart.isChecked = true
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

//                                    binding.spinner.setSelection(position, true)

                                    //TODO: Fix new Quantity
                                    addToFinalCart(ctx, item)
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

                    API(ctx).cart.update(CartUpdateRequest(item.id, itensSpinner[position]), itensSpinner[position]).enqueue(callback)
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    binding.spinner.setSelection(item.quantity - 1)
                }
            }

            binding.btnDelete.setOnClickListener {
                removeFromFinalCart(ctx, item.id)
                //TODO:
                // Retrofit delete request
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val shopBinding = ShopcartItemBinding.inflate(layoutInflater)

        return ViewHolder(shopBinding, ctx)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}