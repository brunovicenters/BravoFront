package com.example.bravofront.api

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class APICookieJar: CookieJar {
    private val cookieStore: MutableMap<String, List<Cookie>> = HashMap()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore[url.host] = cookies
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url.host] ?: ArrayList()
    }

    fun getCookies(url: HttpUrl): List<Cookie>? {
        return cookieStore[url.host]
    }
}