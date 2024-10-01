package com.example.bravofront.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bravofront.databinding.ActivityProductShowBinding
import com.example.bravofront.model.Image
import com.example.bravofront.model.ProdutoIndex
import com.example.bravofront.views.adapters.ProductImagesAdapter
import com.example.bravofront.views.adapters.ProdutoSectionAdapter

class ProductShowActivity : AppCompatActivity() {

    lateinit var binding: ActivityProductShowBinding
    private var qtyAvaialable: Int = 0

    lateinit var adapterImages: ProductImagesAdapter
    val listImages = arrayListOf<Image>()

    lateinit var adapterAlike: ProdutoSectionAdapter
    val listAlike = arrayListOf<ProdutoIndex>()
    val headerAlike = ProdutoIndex()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        turnOnLoading(null, binding.progressBar, binding.nstProductShow)

        binding.btnBack.setOnClickListener {
            finish()
        }

        val txtQty = binding.txtQuantity

        binding.btnMinus.setOnClickListener {

            if (txtQty.text.toString().toInt() > 1) {
                txtQty.text = (txtQty.text.toString().toInt() - 1).toString()
            }
        }

        binding.btnPlus.setOnClickListener {
            if (txtQty.text.toString().toInt() < qtyAvaialable) {
                txtQty.text = (txtQty.text.toString().toInt() + 1).toString()
            }
        }

        binding.btnAddCart.setOnClickListener {
            makeToast("Not implemented yet", this)
        }

        binding.btnBuy.setOnClickListener {
            makeToast("Not implemented yet", this)
        }

        val layotuManagerAlike = GridLayoutManager(this, 2)
        layotuManagerAlike.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0 && listAlike.size % 2 != 0) 2 else 1
            }
        }

        adapterImages = ProductImagesAdapter(listImages)
        binding.rvProductImages.adapter = adapterImages

        adapterAlike = ProdutoSectionAdapter(listAlike, headerAlike)
        binding.rvAlike.adapter = adapterAlike
    }

    override fun onResume() {
        super.onResume()

        updateProduct()
    }

    fun updateProduct() {

    }
}