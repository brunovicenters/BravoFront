package com.example.bravofront.views.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.bravofront.R
import com.example.bravofront.databinding.CardItemBinding
import com.example.bravofront.databinding.HeaderCardItemBinding

import com.example.bravofront.model.ProdutoIndex
import com.example.bravofront.views.ProductShowActivity
import com.example.bravofront.views.formatPrice
import com.squareup.picasso.Picasso


class ProdutoSectionAdapter(private val produtos: List<ProdutoIndex>, private val header: ProdutoIndex, private val screen: Int? = null)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1

    inner class HeaderViewHolder(private val binding: HeaderCardItemBinding, private val ctx: Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(produto: ProdutoIndex) {

            binding.root.setOnClickListener {
                val i = Intent(ctx, ProductShowActivity::class.java)
                i.putExtra("id", produto.id)
                i.putExtra("screen", screen)
                startActivity(ctx, i, null)
                (ctx as Activity).finish()
            }

            Log.d("HeaderViewHolder", "Binding header product: $produto")

            if (produto.nome.length > 20) {
                val newText = produto.nome.take(20) + "..."
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

            Picasso.get()
                .load(produto.imagem?.trim())
                .error(R.drawable.no_car_img)
                .into(binding.imgProduct)
        }
    }

    inner class ItemViewHolder(private val binding: CardItemBinding, private val ctx: Context) : RecyclerView.ViewHolder(binding.root) {
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

            Picasso.get()
                .load(produto.imagem?.trim())
                .error(R.drawable.no_car_img)
                .into(binding.imgProduct)

        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (produtos.size % 2 != 0 && position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val layoutInflater = LayoutInflater.from(parent.context)

            val cardBinding = HeaderCardItemBinding.inflate(layoutInflater, parent, false)

            HeaderViewHolder(cardBinding, parent.context)
        } else {
            val layoutInflater = LayoutInflater.from(parent.context)

            val cardBinding = CardItemBinding.inflate(layoutInflater)

            ItemViewHolder(cardBinding, parent.context)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            (holder as HeaderViewHolder).bind(header)
        } else {
            val produto = produtos[position] // Adjust for header
            (holder as ItemViewHolder).bind(produto)
        }
    }

    override fun getItemCount(): Int {
        return produtos.size
    }


}