package com.example.bravofront.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bravofront.api.API
import com.example.bravofront.databinding.ActivityLoginBinding
import com.example.bravofront.model.ApiResponse
import com.example.bravofront.model.Login
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnEntrar.setOnClickListener {

            val email = binding.inputEmail
            val password = binding.inputPassword

            if (email.text.toString().isEmpty() || password.text.toString().isEmpty()) {
                makeToast("Preencha todos os campos!", this)
                return@setOnClickListener
            }

            if (email.error != null || password.error != null) {
                makeToast("E-mail ou senha inválidos!", this)
                return@setOnClickListener
            }

            val callback = object : Callback<ApiResponse<Login>> {
                override fun onResponse(p0: Call<ApiResponse<Login>>, p1: Response<ApiResponse<Login>>) {
                    TODO("Not yet implemented")
                }

                override fun onFailure(p0: Call<ApiResponse<Login>>, p1: Throwable) {
                    TODO("Not yet implemented")
                }

            }

            API(null).login.login(email.text.toString(), password.text.toString()).enqueue(callback)

            finish()
        }
    }
}