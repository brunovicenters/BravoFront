package com.example.bravofront.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bravofront.R
import com.example.bravofront.views.fragments.ImageFragment

class ImagePagerAdapter(fragment: Fragment, private val images: List<Int>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = images.size

    override fun createFragment(position: Int): Fragment {
        return ImageFragment.newInstance(images[position])
    }
}
