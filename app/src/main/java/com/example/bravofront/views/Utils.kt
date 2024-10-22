package com.example.bravofront.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.bravofront.api.ARQUIVO_LOGIN
import com.example.bravofront.model.CartItem
import com.example.bravofront.model.ProdutoIndex
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

// Turn on and off loading
fun turnOnLoading(swiper: SwipeRefreshLayout?, progressBar: View?, container: View?) {
    swiper?.isRefreshing = true
    progressBar?.visibility = View.VISIBLE
    container?.visibility = View.INVISIBLE
}
fun turnOffLoading(swiper: SwipeRefreshLayout?, progressBar: View?, container: View?) {
    swiper?.isRefreshing = false
    progressBar?.visibility = View.GONE
    container?.visibility = View.VISIBLE
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
fun setRecentlyViewed(ctx: Context, item: ProdutoIndex) {
    val editor = ctx.getSharedPreferences("ProfileShow", Context.MODE_PRIVATE).edit()
    val user = ctx.getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE).getInt("user", -1)

    val gson = Gson()

    var list = getRecentlyViewed(ctx)?.toMutableList()
    if (list == null) {
        list = mutableListOf(item)
    } else {
        val itemId = item.id
        if (list.indexOfFirst { it.id == itemId } > -1) {
            list.removeAt(list.indexOfFirst { it.id == itemId })
            list.add(0, item)
        } else {
            list.add(0, item)
        }
    }

    if (list.size > 10) {
        list.removeLast()
    }

    val json = gson.toJson(list.toList())
    editor.putString("RecentlyViewed-$user",json)
    editor.commit()
}

fun getRecentlyViewed(ctx: Context):List<ProdutoIndex>? {
    val preferences = ctx.getSharedPreferences("ProfileShow", Context.MODE_PRIVATE)
    val user = ctx.getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE).getInt("user", -1)

    val gson = Gson()
    val json = preferences.getString("RecentlyViewed-$user",null)
    val type = object :TypeToken<ArrayList<ProdutoIndex>>(){}.type//converting the json to list
    return gson.fromJson(json,type)//returning the list
}

fun removeRecentlyViewed(ctx: Context, item: Int) {
    val editor = ctx.getSharedPreferences("ProfileShow", Context.MODE_PRIVATE).edit()
    val user = ctx.getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE).getInt("user", -1)

    val list = getRecentlyViewed(ctx)?.toMutableList()
    if (list != null) {
        if (list.indexOfFirst { it.id == item } > -1) {
            list.removeAt(list.indexOfFirst { it.id == item })
        }
        val json = Gson().toJson(list.toList())
        editor.putString("RecentlyViewed-$user",json)
        editor.commit()
    }
}

fun getFinalCart(ctx: Context):List<CartItem>? {
    val preferences = ctx.getSharedPreferences("ITEMS_BUY", Context.MODE_PRIVATE)
    val user = ctx.getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE).getInt("user", -1)

    val gson = Gson()
    val json = preferences.getString("ITEMS_BUY-$user",null)
    val type = object :TypeToken<ArrayList<CartItem>>(){}.type//converting the json to list
    return gson.fromJson(json,type)//returning the list
}

fun addToFinalCart(ctx: Context, item: CartItem) {
    val editor = ctx.getSharedPreferences("ITEMS_BUY", Context.MODE_PRIVATE).edit()
    val user = ctx.getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE).getInt("user", -1)

    val gson = Gson()
    var list = getFinalCart(ctx)?.toMutableList()
    if (list == null) {
        list = mutableListOf(item)
    } else {
        if (list.indexOfFirst { it.id == item.id } > -1) {
            list.removeAt(list.indexOfFirst { it.id == item.id })
        }
        list.add(0, item)
    }

    val json = gson.toJson(list.toList())
    editor.putString("ITEMS_BUY-$user",json)
    editor.commit()
}

fun removeFromFinalCart(ctx: Context, item: Int) {
    val editor = ctx.getSharedPreferences("ITEMS_BUY", Context.MODE_PRIVATE).edit()
    val user = ctx.getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE).getInt("user", -1)

    val list = getFinalCart(ctx)?.toMutableList()
    if (list != null) {
        if (list.indexOfFirst { it.id == item } > -1) {
            list.removeAt(list.indexOfFirst { it.id == item })
        }
        val json = Gson().toJson(list.toList())
        editor.putString("ITEMS_BUY-$user",json)
        editor.commit()
    }
}
