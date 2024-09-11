package com.example.bravofront.views

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bravofront.R
import com.example.bravofront.api.ARQUIVO_LOGIN
import com.example.bravofront.databinding.ActivityMainBinding
import com.example.bravofront.views.fragments.HomeFragment
import com.example.bravofront.views.fragments.LoggedProfileFragment
import com.example.bravofront.views.fragments.SearchFragment
import com.example.bravofront.views.fragments.UnLoggedProfileFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    lateinit var frag : Fragment

    lateinit var sp : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sp = this.getSharedPreferences(ARQUIVO_LOGIN, Context.MODE_PRIVATE)

        frag = HomeFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragContainer.id, frag)
            .commit()
    }

    override fun onResume() {
        super.onResume()

        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    frag = HomeFragment.newInstance()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(binding.fragContainer.id, frag)
                        .commit()
                }
                R.id.search -> {
                    frag = SearchFragment.newInstance("", "")
                    supportFragmentManager
                        .beginTransaction()
                        .replace(binding.fragContainer.id, frag)
                        .commit()
                }
                R.id.profile -> {
                    if (sp.getInt("user", -1) != -1) {
                        frag = LoggedProfileFragment.newInstance()
                        supportFragmentManager
                            .beginTransaction()
                            .replace(binding.fragContainer.id, frag)
                            .commit()
                    } else {
                        frag = UnLoggedProfileFragment.newInstance(this)
                        supportFragmentManager
                            .beginTransaction()
                            .replace(binding.fragContainer.id, frag)
                            .commit()
                    }

                }
            }
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        val edit = sp.edit()
        edit.remove("user")

        if(!sp.getBoolean("keepLogin", false)) {
            edit.remove("email")
            edit.remove("password")
        }

        edit.apply()
    }
}