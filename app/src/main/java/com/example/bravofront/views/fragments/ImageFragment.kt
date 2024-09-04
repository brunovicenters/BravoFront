package com.example.bravofront.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.bravofront.R

class ImageFragment : Fragment() {

    companion object {
        private const val ARG_IMAGE_RES = "image_res"

        fun newInstance(imageRes: Int): ImageFragment {
            val fragment = ImageFragment()
            val args = Bundle()
            args.putInt(ARG_IMAGE_RES, imageRes)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image, container, false)
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val imageRes = arguments?.getInt(ARG_IMAGE_RES) ?: 0
        imageView.setImageResource(imageRes)
        return view
    }
}
