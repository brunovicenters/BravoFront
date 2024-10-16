package com.example.bravofront.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bravofront.R
import com.example.bravofront.databinding.FragmentShopCartBinding
import com.example.bravofront.model.CartItem
import com.example.bravofront.views.adapters.ShopCartAdapter
import com.example.bravofront.views.turnOnLoading

class ShopCartFragment : Fragment() {

    lateinit var binding: FragmentShopCartBinding

    lateinit var shopCartAdapter: ShopCartAdapter
    val listShopCart = arrayListOf<CartItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShopCartBinding.inflate(inflater, container, false)

//        turnOnLoading()

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ShopCartFragment()
    }
}