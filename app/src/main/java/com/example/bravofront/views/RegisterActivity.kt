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
import com.example.bravofront.databinding.ActivityRegisterBinding
import com.example.bravofront.model.ApiResponse
import com.example.bravofront.model.Login
import com.example.bravofront.model.RegisterRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                if (intent.getBooleanExtra("shopcart", false)) {
                    i.putExtra("shopcart", true)
                }
                startActivity(i)
                finish()
                return@setOnClickListener
            }

            if (intent.getBooleanExtra("shopcart", false)) {
                makeToast("Você precisa logar para acessar o carrinho", ctx)
                val i = Intent(ctx, MainActivity::class.java)
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
            if (intent.getBooleanExtra("shopcart", false)) {
                i.putExtra("shopcart", true)
            }
            startActivity(i)
            finish()
        }

        binding.btnCadastrar.setOnClickListener {
            val name = binding.inputName
            val email = binding.inputEmail
            val password = binding.inputPassword
            val confirmPassword = binding.inputPasswordConfirmation
            val cpf = binding.inputCPF

            if (name.text.toString().isEmpty() || email.text.toString().isEmpty() || password.text.toString().isEmpty() || confirmPassword.text.toString().isEmpty() || cpf.text.toString().isEmpty()) {
                makeToast(getString(R.string.fill_all_fields), ctx)
                return@setOnClickListener
            }

            if (password.text.toString() != confirmPassword.text.toString()) {
                makeToast(getString(R.string.password_match), ctx)
                return@setOnClickListener
            }

            if (cpf.text.toString().length != 14) {
                makeToast(getString(R.string.invalid_cpf), ctx)
                return@setOnClickListener
            }

            if (name.error != null || email.error != null || password.error != null || confirmPassword.error != null || cpf.error != null) {
                makeToast("${name.error}${email.error}${password.error}${confirmPassword.error}${cpf.error}", ctx)
                return@setOnClickListener
            }

            if (!binding.swtTermAndConditions.isChecked) {
                makeToast(getString(R.string.failed_term_and_conditions), ctx)
                return@setOnClickListener
            }

            val registerRequest = RegisterRequest(
                name.text.toString(),
                email.text.toString(),
                cpf.text.toString(),
                password.text.toString(),
                confirmPassword.text.toString()
            )

            val callback = object : Callback<ApiResponse<Login>> {
                override fun onResponse(
                    c: Call<ApiResponse<Login>>,
                    res: Response<ApiResponse<Login>>
                ) {

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
                        }

                    } else {
                        if (res.code() == 404 || res.code() == 401) {

                            makeToast(getString(R.string.error_on_register), ctx)

                            Log.e("ERROR", res.errorBody().toString())
                        }
                    }

                }

                override fun onFailure(c: Call<ApiResponse<Login>>, t: Throwable) {
                    makeToast(getString(R.string.failed_connect_server), ctx)

                    Log.e("ERROR", getString(R.string.failed_execute_server), t)
                }

            }

            API(null).login.register(registerRequest).enqueue(callback)
        }
    }
}