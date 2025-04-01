package com.example.umc_flo.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.umc_flo.AlbumFragment
import com.example.umc_flo.DetailFragment
import com.example.umc_flo.TracklistFragment
import com.example.umc_flo.VideoFragment

class AlbumVPAdapter(fragment: AlbumFragment): FragmentStateAdapter(fragment) {

    // 탭 개수
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> TracklistFragment()
            1 -> DetailFragment()
            2 -> VideoFragment()

            else -> TracklistFragment()
        }
    }
}