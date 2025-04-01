package com.example.umc_flo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc_flo.databinding.FragmentAlbumBinding
import com.example.umc_flo.ui.AlbumVPAdapter
import com.google.android.material.tabs.TabLayoutMediator

class AlbumFragment: Fragment() {

    val albumBinding by lazy{
        FragmentAlbumBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return albumBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AlbumVPAdapter(this)
        albumBinding.albumVp.adapter = adapter

        //tabLayout과 viewPager2연결
        TabLayoutMediator(albumBinding.albumTab, albumBinding.albumVp){ tab, position ->
            tab.text = when(position){
                0 -> "수록곡"
                1 -> "상세정보"
                2 -> "영상"

                else -> "수록곡"
            }
        }.attach()

        albumBinding.songBackBtn.setOnClickListener {
            replaceFragment(HomeFragment())
        }
    }
    private fun replaceFragment(fragment: Fragment){
        parentFragmentManager.beginTransaction()
            .replace(R.id.frameMain, fragment)
            .commit()
    }
}