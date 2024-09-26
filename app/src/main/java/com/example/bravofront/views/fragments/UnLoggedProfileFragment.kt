package com.example.bravofront.views.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bravofront.R
import com.example.bravofront.databinding.FragmentUnLoggedProfileBinding
import com.example.bravofront.views.LoginActivity
import com.example.bravofront.views.RegisterActivity
import com.example.bravofront.views.TermAndConditionsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class UnLoggedProfileFragment(val ctx: Context) : Fragment() {

    lateinit var binding: FragmentUnLoggedProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUnLoggedProfileBinding.inflate(inflater, container, false)

        binding.btnLogin.setOnClickListener {
            val intent = Intent(ctx, LoginActivity::class.java)
            intent.putExtra("frag", R.id.profile)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(ctx, RegisterActivity::class.java)
            intent.putExtra("frag", R.id.profile)
            startActivity(intent)
        }

        binding.btnTermAndCond.setOnClickListener {
            val intent = Intent(ctx, TermAndConditionsActivity::class.java)
            startActivity(intent)
        }

        binding.btnSAC.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:11990225294"))
            startActivity(intent)
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(ctx: Context) =
            UnLoggedProfileFragment(ctx)
    }
}