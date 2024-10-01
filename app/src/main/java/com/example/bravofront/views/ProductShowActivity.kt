package com.example.bravofront.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bravofront.databinding.ActivityProductShowBinding

class ProductShowActivity : AppCompatActivity() {

    lateinit var binding: ActivityProductShowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductShowBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}