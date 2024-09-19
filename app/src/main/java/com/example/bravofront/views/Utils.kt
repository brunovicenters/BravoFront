package com.example.bravofront.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
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

fun formatPrice(price: Double, country: String, language: String) : String {
    val format = NumberFormat.getCurrencyInstance(Locale(language, country))
    format.maximumFractionDigits = 2


    return format.format(price)
}

fun makeToast(message: String, ctx: Context) {
    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
}

fun EditText.addCpfMask() {
    this.addTextChangedListener(object : TextWatcher {
        var isUpdating: Boolean = false

        override fun afterTextChanged(editable: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (isUpdating) {
                isUpdating = false
                return
            }

            val string = s.toString().replace(Regex("[^\\d]"), "")
            val maskedString = when (string.length) {
                in 1..3 -> string
                in 4..6 -> "${string.substring(0, 3)}.${string.substring(3)}"
                in 7..9 -> "${string.substring(0, 3)}.${string.substring(3, 6)}.${string.substring(6)}"
                in 10..11 -> "${string.substring(0, 3)}.${string.substring(3, 6)}.${string.substring(6, 9)}-${string.substring(9)}"
                else -> "${string.substring(0, 3)}.${string.substring(3, 6)}.${string.substring(6, 9)}-${string.substring(9, 11)}"
            }

            isUpdating = true
            this@addCpfMask.setText(maskedString)
            this@addCpfMask.setSelection(maskedString.length)
        }
    })
}