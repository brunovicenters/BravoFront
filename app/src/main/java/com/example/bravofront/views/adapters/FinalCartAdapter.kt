package com.example.bravofront.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bravofront.R
import com.example.bravofront.databinding.FinalCartItemBinding
import com.example.bravofront.model.CartFinalItem
import com.example.bravofront.views.formatPrice
import com.squareup.picasso.Picasso

class FinalCartAdapter(private val list: List<CartFinalItem>): RecyclerView.Adapter<FinalCartAdapter.ViewHolder>() {

    class ViewHolder (private val binding: FinalCartItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartFinalItem) {
            if (item.image == null || item.image == "") {
                binding.imgFinalCartItem.setImageResource(R.drawable.no_car_img)
            } else {
                Picasso.get()
                    .load(item.image.trim())
                    .error(R.drawable.no_car_img)
                    .into(binding.imgFinalCartItem)
            }

            if (item.name.length > 24) {
                val newText = item.name.take(24) + "..."
                binding.txtNomeProdutoFC.text = newText
            } else {
                binding.txtNomeProdutoFC.text = item.name
            }

            binding.txtPriceFC.text = formatPrice(item.price.toDouble(), binding.root.context.getString(R.string.country_currency), binding.root.context.getString(R.string.language_currency))

            binding.txtQtyFC.text = item.quantity.toString() + "x"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val produtoBinding = FinalCartItemBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(produtoBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}