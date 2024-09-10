package com.example.bravofront.views

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.text.NumberFormat
import java.util.*

fun turnOnLoading(swiper: SwipeRefreshLayout, progressBar: View, container: View) {
    swiper.isRefreshing = true
    progressBar.visibility = View.VISIBLE
    container.visibility = View.INVISIBLE
}
fun turnOffLoading(swiper: SwipeRefreshLayout, progressBar: View, container: View) {
    swiper.isRefreshing = false
    progressBar.visibility = View.GONE
    container.visibility = View.VISIBLE
}

fun formatPrice(price: Double) : String {
    val format = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 2

    // TODO: change according to idiom
    format.currency = Currency.getInstance("BRL")

    return format.format(price)
}

fun makeToast(message: String, ctx: Context) {
    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
}