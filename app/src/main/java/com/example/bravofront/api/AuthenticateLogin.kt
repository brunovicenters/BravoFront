package com.example.bravofront.api

import android.content.Context
import android.util.Log
import com.example.bravofront.model.LoginRequest
import okhttp3.*

const val ARQUIVO_LOGIN = "login"

class AuthenticateToken(private val ctx: Context): Interceptor, Authenticator {

    override fun intercept(chain: Interceptor.Chain): Response {
        val prefs = ctx.getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE)
        val user = prefs.getInt("user", -1)

        val request = chain.request().newBuilder()
            .addHeader("user", user.toString())
            .build()

        return chain.proceed(request)
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        val prefs = ctx.getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE)

        Log.d("AuthenticateToken", "I'M HERE")

        val email = prefs.getString("email", "") as String
        val password = prefs.getString("password", "") as String

        val loginRequest = LoginRequest(email, password)

        val resRetrofit = API(null).login.login(loginRequest).execute()
        val user = resRetrofit.body()?.data?.user

        Log.d("AuthenticateToken", user.toString())

        if (resRetrofit.isSuccessful && user != null) {
            prefs.edit().putInt("user", user).apply()

            return response.request.newBuilder().addHeader("user", user.toString()).build()
        }
        return null
    }

}