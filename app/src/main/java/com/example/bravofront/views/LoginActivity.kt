package com.example.bravofront.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.bravofront.R
import com.example.bravofront.api.API
import com.example.bravofront.api.ARQUIVO_LOGIN
import com.example.bravofront.databinding.ActivityLoginBinding
import com.example.bravofront.model.ApiResponse
import com.example.bravofront.model.Login
import com.example.bravofront.model.LoginRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    lateinit var sp : SharedPreferences

    private var ctx: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sp = this.getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE)

        binding.backBtn.setOnClickListener {
            if (intent.getBooleanExtra("register", false)) {
                val i = Intent(ctx, RegisterActivity::class.java)
                startActivity(i)
            }
            finish()
        }

        binding.txtCreateAccount.setOnClickListener {
            val i = Intent(ctx, RegisterActivity::class.java)
            i.putExtra("login", true)
            startActivity(i)
            finish()
        }

        binding.btnEntrar.setOnClickListener {

            val email = binding.inputEmail
            val password = binding.inputPassword

            if (email.text.toString().isEmpty() || password.text.toString().isEmpty()) {
                makeToast(getString(R.string.fill_all_fields), ctx)
                return@setOnClickListener
            }

            if (email.error != null || password.error != null) {
                makeToast(getString(R.string.login_invalid), ctx)
                return@setOnClickListener
            }

            val callback = object : Callback<ApiResponse<Login>> {
                override fun onResponse(call: Call<ApiResponse<Login>>, res: Response<ApiResponse<Login>>) {

                    if (res.isSuccessful) {

                        val user = res.body()?.data?.user

                        if (user != null) {
                            val edit = sp.edit()

                            val swi = binding.swtKeepLoggedIn
                            if (swi.isChecked) {

                                edit.putBoolean("keepLogin", true)
                            } else {
                                edit.putBoolean("keepLogin", false)
                            }

                            edit.putString("email", email.text.toString())
                            edit.putString("password", password.text.toString())
                            edit.putInt("user", user)

                            edit.apply()

                            val i = Intent(ctx, MainActivity::class.java)
                            val currentFrag = intent.getIntExtra("frag", -1)
                            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            i.putExtra("frag", currentFrag)
                            startActivity(i)
                            finish()
                        } else {
                            makeToast(getString(R.string.login_invalid), ctx)
                        }
                    } else {
                        if (res.code() == 404 || res.code() == 401) {

                            makeToast(getString(R.string.login_invalid), ctx)

                            Log.e("ERROR", res.errorBody().toString())
                        }
                    }
                }

                override fun onFailure(call: Call<ApiResponse<Login>>, t: Throwable) {
                    makeToast(getString(R.string.login_invalid), ctx)
                }

            }

            val loginRequest = LoginRequest(email.text.toString(), password.text.toString(),)

            API(null).login.login(loginRequest).enqueue(callback)
        }
    }
}