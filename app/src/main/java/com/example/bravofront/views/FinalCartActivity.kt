package com.example.bravofront.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bravofront.R
import com.example.bravofront.api.ARQUIVO_LOGIN
import com.example.bravofront.databinding.ActivityFinalCartBinding
import com.example.bravofront.model.CartItem

class FinalCartActivity : AppCompatActivity() {

    lateinit var binding: ActivityFinalCartBinding
    lateinit var sp: SharedPreferences

    var itensCart: List<CartItem>? = null
    var listItems = arrayListOf<CartItem>()
//    lateinit var adapter: FinalCartAdapter

//    lateinit var listEnderecos: ArrayList<Endereco>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinalCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sp = getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE)

        itensCart = getFinalCart(this)
        if (itensCart == null) {
            makeToast("Carrinho vazio", this)
            finish()
        }

        var sum = 0.0
        itensCart?.let { it ->
            sum = it.sumOf { it.price.toDouble() }
        }

        binding.txtFinalPrice.text = formatPrice(sum, this.getString(R.string.country_currency), this.getString(R.string.language_currency))

        binding.btnBuyFinal.setOnClickListener {
            goBack()
        }
    }

    private fun goBack() {
        val i = Intent(this, MainActivity::class.java)
        i.putExtra("frag",intent.getIntExtra("screen", R.id.home))
        startActivity(i)
        finish()
    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()

        goBack()
    }
}