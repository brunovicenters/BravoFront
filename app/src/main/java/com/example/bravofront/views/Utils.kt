package com.example.bravofront.views

import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

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