package com.example.bravofront.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bravofront.R
import com.example.bravofront.databinding.ActivityMainBinding
import com.example.bravofront.views.fragments.HomeFragment
import com.example.bravofront.views.fragments.ProfileFragment
import com.example.bravofront.views.fragments.SearchFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var frag : Fragment = HomeFragment.newInstance();
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fragContainer.id, frag)
            .commit()

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
                    frag = ProfileFragment.newInstance("", "")
                    supportFragmentManager
                        .beginTransaction()
                        .replace(binding.fragContainer.id, frag)
                        .commit()
                }
            }
            true
        }
    }
}