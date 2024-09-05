package com.example.bravofront.views

import android.view.View
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