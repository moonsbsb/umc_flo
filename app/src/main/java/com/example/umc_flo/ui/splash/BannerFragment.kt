package com.example.umc_flo.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.umc_flo.databinding.FragmentBannerBinding

class BannerFragment(private val imageRes: Int): Fragment() {

    val bannerBinding by lazy{
        FragmentBannerBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bannerBinding.bannerImg.setImageResource(imageRes)
        return bannerBinding.root
    }
}