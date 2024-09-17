package com.example.bravofront.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bravofront.R
import com.example.bravofront.api.ARQUIVO_LOGIN
import com.example.bravofront.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding

    lateinit var sp : SharedPreferences

    private var ctx: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sp = this.getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE)

        binding.inputCPF.addCpfMask()

        binding.backBtn.setOnClickListener {
            if (intent.getBooleanExtra("login", false)) {
                val i = Intent(ctx, LoginActivity::class.java)
                startActivity(i)
            }
            finish()
        }

        binding.txtTermAndConditions.setOnClickListener {
            val i = Intent(ctx, TermAndConditionsActivity::class.java)
            startActivity(i)
        }

        binding.txtLogin.setOnClickListener {
            val i = Intent(ctx, LoginActivity::class.java)
            i.putExtra("register", true)
            startActivity(i)
            finish()
        }

    }
}