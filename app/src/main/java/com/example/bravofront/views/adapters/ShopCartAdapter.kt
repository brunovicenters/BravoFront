package com.example.bravofront.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.bravofront.R
import com.example.bravofront.databinding.ImgProductItemBinding
import com.example.bravofront.databinding.ShopcartItemBinding
import com.example.bravofront.model.CartItem
import com.example.bravofront.model.Image
import com.squareup.picasso.Picasso

class ShopCartAdapter (private val list: List<CartItem>): RecyclerView.Adapter<ShopCartAdapter.ViewHolder>() {

    class ViewHolder (private val binding: ShopcartItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val shopBinding = ShopcartItemBinding.inflate(layoutInflater)

        return ViewHolder(shopBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}