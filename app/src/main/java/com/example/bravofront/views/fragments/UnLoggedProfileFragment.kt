package com.example.bravofront.views.fragments

import android.content.Context
import android.content.Intent
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UnLoggedProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UnLoggedProfileFragment(val ctx: Context) : Fragment() {

    lateinit var binding: FragmentUnLoggedProfileBinding

    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }

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

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(ctx: Context) =
            UnLoggedProfileFragment(ctx)
//                .apply {
//                    arguments = Bundle().apply {
//                        putString(ARG_PARAM1, ctx)
//                    }
//                }
    }
}