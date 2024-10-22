package com.example.bravofront.views

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bravofront.R
import com.example.bravofront.databinding.ActivityFinalCartBinding

class FinalCartActivity : AppCompatActivity() {

    lateinit var binding: ActivityFinalCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinalCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}