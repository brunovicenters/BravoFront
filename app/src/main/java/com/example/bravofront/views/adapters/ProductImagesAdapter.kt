package com.example.bravofront.views.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.bravofront.R
import com.example.bravofront.databinding.CardItemBinding
import com.example.bravofront.databinding.ImgProductItemBinding
import com.example.bravofront.model.Image
import com.example.bravofront.model.ProdutoIndex
import com.example.bravofront.views.adapters.ProdutoCardRecyclerViewAdapter.ViewHolder
import com.example.bravofront.views.formatPrice
import com.squareup.picasso.Picasso

class ProductImagesAdapter(private val list: List<Image>, private val mainImg: ImageView)
    : RecyclerView.Adapter<ProductImagesAdapter.ViewHolder>() {

    class ViewHolder (private val binding: ImgProductItemBinding, private val mainImg: ImageView) : RecyclerView.ViewHolder(binding.root) {
        fun bind(img: Image) {

            binding.root.setOnClickListener {
                Picasso.get()
                    .load(img.url.trim())
                    .error(R.drawable.no_car_img)
                    .into(mainImg)
            }

            Picasso.get()
                .load(img.url.trim())
                .error(R.drawable.no_car_img)
                .into(binding.imgProductCarousel)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val imgBinding = ImgProductItemBinding.inflate(layoutInflater)

        return ViewHolder(imgBinding, mainImg)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}