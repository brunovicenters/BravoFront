package com.example.bravofront.views.adapters

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bravofront.R
import com.example.bravofront.databinding.CardItemBinding
import com.example.bravofront.databinding.HeaderCardItemBinding

import com.example.bravofront.model.ProdutoIndex
import com.example.bravofront.views.formatPrice
import com.squareup.picasso.Picasso


class ProdutoSectionAdapter(private val produtos: List<ProdutoIndex>, private val header: ProdutoIndex)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1

    inner class HeaderViewHolder(private val binding: HeaderCardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(produto: ProdutoIndex) {
            Log.d("HeaderViewHolder", "Binding header product: $produto")

            if (produto.nome.length > 20) {
                val newText = produto.nome.take(20) + "..."
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

    inner class ItemViewHolder(private val binding: CardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(produto: ProdutoIndex) {
            if (produto.nome.length > 10) {
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

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val layoutInflater = LayoutInflater.from(parent.context)

            val cardBinding = HeaderCardItemBinding.inflate(layoutInflater, parent, false)

            HeaderViewHolder(cardBinding)
        } else {
            val layoutInflater = LayoutInflater.from(parent.context)

            val cardBinding = CardItemBinding.inflate(layoutInflater)

            ItemViewHolder(cardBinding)
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