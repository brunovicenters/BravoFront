package com.example.bravofront.views.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.bravofront.R
import com.example.bravofront.databinding.CardItemBinding
import com.example.bravofront.model.ProdutoIndex
import com.example.bravofront.views.ProductShowActivity
import com.example.bravofront.views.formatPrice
import com.squareup.picasso.Picasso

class ProdutoCardRecyclerViewAdapter(private val list: List<ProdutoIndex>, val screen:  Int)
    : RecyclerView.Adapter<ProdutoCardRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder (private val binding: CardItemBinding, private val ctx: Context, private val screen: Int) : RecyclerView.ViewHolder(binding.root) {
        fun bind(produto: ProdutoIndex) {

            binding.root.setOnClickListener {
                val i = Intent(ctx, ProductShowActivity::class.java)
                i.putExtra("id", produto.id)
                i.putExtra("screen", screen)
                startActivity(ctx, i, null)
                (ctx as Activity).finish()
            }

            if (produto.nome.length > 10) {
                val newText = produto.nome.take(10) + "..."
                binding.cardTitle.text = newText
            } else {
                binding.cardTitle.text = produto.nome
            }

            if(produto.desconto != null && produto.desconto.toDouble() > 0.0) {
                val newPrice = (produto.preco.toDouble() ?: 0.0) - (produto.desconto.toDouble() ?: 0.0)
                binding.cardPrice.text = formatPrice(newPrice, ctx.getString(R.string.country_currency), ctx.getString(R.string.language_currency))
                binding.cardOldPrice.text = formatPrice(produto.preco.toDouble(), ctx.getString(R.string.country_currency), ctx.getString(R.string.language_currency))
                binding.cardOldPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.cardPrice.text = formatPrice(produto.preco.toDouble(), ctx.getString(R.string.country_currency), ctx.getString(R.string.language_currency))
                binding.cardOldPrice.visibility = View.GONE
            }

            if (!(produto.imagem == null || produto.imagem == "")) {
                Picasso.get()
                    .load(produto.imagem.trim())
                    .error(R.drawable.no_car_img)
                    .into(binding.imgProduct)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        val cardBinding = CardItemBinding.inflate(layoutInflater)

        return ViewHolder(cardBinding, parent.context, screen)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}