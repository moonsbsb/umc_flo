package com.example.umc_flo.ui.splash

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.umc_flo.ui.main.home.HomeFragment
import com.example.umc_flo.R

class BannerVPAdapter(fragment: HomeFragment): FragmentStateAdapter(fragment) {

    private val fragmentList = arrayListOf(
        BannerFragment(R.drawable.img_home_viewpager_exp),
        BannerFragment(R.drawable.img_home_viewpager_exp2)
    )

    override fun getItemCount(): Int = fragmentList.size

    // 각 페이지를 생성하는 역할
    override fun createFragment(position: Int): Fragment = fragmentList[position]


}