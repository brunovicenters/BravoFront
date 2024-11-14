package com.example.bravofront.views

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bravofront.R
import com.example.bravofront.api.ARQUIVO_LOGIN
import com.example.bravofront.databinding.ActivityMainBinding
import com.example.bravofront.views.fragments.*

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    lateinit var frag : Fragment

    lateinit var sp : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sp = this.getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE)

        val currentFrag = intent.getIntExtra("frag", R.id.home)

        binding.bottomNavView.selectedItemId = currentFrag

        changeScreen(currentFrag)

        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragContainer.id, frag)
            .commit()
    }

    override fun onResume() {
        super.onResume()

        binding.bottomNavView.setOnItemSelectedListener {
            changeScreen(it.itemId)

            supportFragmentManager
                .beginTransaction()
                .replace(binding.fragContainer.id, frag)
                .commit()
            true
        }
    }

    private fun changeScreen(screen: Int) {
        frag = when (screen) {
            R.id.search -> SearchFragment.newInstance()
            R.id.profile -> {
                if (sp.getString("email", "") != "" && sp.getString("password", "") != "") {
                    LoggedProfileFragment.newInstance(this)
                } else {
                    UnLoggedProfileFragment.newInstance(this)
                }
            }
            R.id.shopcart -> ShopCartFragment.newInstance()
            else -> HomeFragment.newInstance(this)
        }
    }
}