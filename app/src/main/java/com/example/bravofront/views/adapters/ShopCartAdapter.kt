package com.example.bravofront.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bravofront.R
import com.example.bravofront.databinding.ShopcartItemBinding
import com.example.bravofront.model.CartItem
import com.example.bravofront.views.*
import com.squareup.picasso.Picasso

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

            val adapterSpinner = ArrayAdapter(ctx, android.R.layout.simple_spinner_item, itensSpinner)

            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            binding.spinner.adapter = adapterSpinner

            binding.spinner.setSelection(item.quantity - 1)

            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                    binding.txtOutOfStock.visibility = View.GONE
                    binding

                    //TODO:
                    // Retrofit update request
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