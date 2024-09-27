package com.example.bravofront.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.bravofront.model.ProdutoIndex
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.NumberFormat
import java.util.*

// Turn on and off loading
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

// Format price
fun formatPrice(price: Double, country: String, language: String) : String {
    val format = NumberFormat.getCurrencyInstance(Locale(language, country))
    format.maximumFractionDigits = 2


    return format.format(price)
}


// Make toast
fun makeToast(message: String, ctx: Context) {
    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
}

// Add cpf mask
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

// Store and get Recently viewed items
fun setRecentlyViewed(list:ArrayList<ProdutoIndex>, ctx: Context){
    val editor = ctx.getSharedPreferences("ProfileShow", Context.MODE_PRIVATE).edit()

    val gson = Gson()
    val json = gson.toJson(list)
    editor.putString("RecentlyViewed",json)
    editor.commit()
}

fun getRecentlyViewed(ctx: Context):ArrayList<ProdutoIndex>? {
    val preferences = ctx.getSharedPreferences("ProfileShow", Context.MODE_PRIVATE)

    val gson = Gson()
    val json = preferences.getString("RecentlyViewed",null)
    val type = object :TypeToken<ArrayList<ProdutoIndex>>(){}.type//converting the json to list
    return gson.fromJson(json,type)//returning the list
}