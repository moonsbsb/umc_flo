package com.example.umc_flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.umc_flo.databinding.FragmentHomeBinding
import com.example.umc_flo.ui.BannerVPAdapter

class HomeFragment: Fragment() {

    val homeBinding by lazy{
        FragmentHomeBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeBinding.albumImg.setOnClickListener {
            replaceFragment(AlbumFragment())
        }
        // 배너 뷰페이저
        homeBinding.bannerVP.adapter = BannerVPAdapter(this)
    }
    private fun replaceFragment(fragment: Fragment){
        parentFragmentManager.beginTransaction()
            .replace(R.id.frameMain ,fragment)
            .commit()
    }
}