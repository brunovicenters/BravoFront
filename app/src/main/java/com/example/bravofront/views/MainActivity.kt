package com.example.bravofront.views

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bravofront.R
import com.example.bravofront.databinding.ActivityMainBinding
import com.example.bravofront.views.fragments.HomeFragment
import com.example.bravofront.views.fragments.LoggedProfileFragment
import com.example.bravofront.views.fragments.SearchFragment
import com.example.bravofront.views.fragments.UnLoggedProfileFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    lateinit var frag : Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                    if (false) {
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
}