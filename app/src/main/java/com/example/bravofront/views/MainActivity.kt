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

        val currentFrag = intent.getIntExtra("frag", R.id.home)

        binding.bottomNavView.selectedItemId = currentFrag

        frag = when (currentFrag) {
            R.id.search -> SearchFragment.newInstance("", "")
            R.id.profile -> {
                if (sp.getInt("user", -1) != -1) {
                    LoggedProfileFragment.newInstance(this)
                } else {
                    UnLoggedProfileFragment.newInstance(this)
                }
            }
            else -> HomeFragment.newInstance()
        }

        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragContainer.id, frag)
            .commit()
    }

    override fun onResume() {
        super.onResume()

        binding.bottomNavView.setOnItemSelectedListener {
            frag = when (it.itemId) {
                R.id.search -> SearchFragment.newInstance("", "")
                R.id.profile -> {
                    if (sp.getString("email", "") != "" && sp.getString("password", "") != "") {
                        LoggedProfileFragment.newInstance(this)
                    } else {
                        UnLoggedProfileFragment.newInstance(this)
                    }
                }
                else -> HomeFragment.newInstance()
            }

            supportFragmentManager
                .beginTransaction()
                .replace(binding.fragContainer.id, frag)
                .commit()
            true
        }
    }

    override fun onStop() {
        super.onStop()

        val edit = sp.edit()

        if(!sp.getBoolean("keepLogin", false)) {
            edit.remove("user")
            edit.remove("email")
            edit.remove("password")
        }

        edit.apply()
    }
}