package com.example.bravofront.views.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bravofront.R
import com.example.bravofront.databinding.CardItemBinding
import com.example.bravofront.model.ProdutoIndex
import com.example.bravofront.views.formatPrice
import com.squareup.picasso.Picasso

class ProdutoCardRecyclerViewAdapter(private val list: List<ProdutoIndex>)
    : RecyclerView.Adapter<ProdutoCardRecyclerViewAdapter.ViewHolder>() {



    class ViewHolder (private val binding: CardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(produto: ProdutoIndex) {
            if (produto.nome?.length!! > 10) {
                val newText = produto.nome.take(10) + "..."
                binding.cardTitle.text = newText
            } else {
                binding.cardTitle.text = produto.nome
            }

            if(produto.desconto != null && produto.desconto.toDouble() > 0.0) {
                val newPrice = (produto.preco.toDouble() ?: 0.0) - (produto.desconto.toDouble() ?: 0.0)
                binding.cardPrice.text = formatPrice(newPrice)
                binding.cardOldPrice.text = formatPrice(produto.preco.toDouble())
                binding.cardOldPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.cardPrice.text = formatPrice(produto.preco.toDouble())
                binding.cardOldPrice.visibility = View.GONE
            }

            Picasso.get()
                .load(produto.imagem)
                .error(R.drawable.no_car_img)
                .into(binding.cardImg)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val cardBinding = CardItemBinding.inflate(layoutInflater)

        return ViewHolder(cardBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}