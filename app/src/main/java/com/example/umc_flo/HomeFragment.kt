package com.example.umc_flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.umc_flo.databinding.FragmentHomeBinding
import com.example.umc_flo.ui.BannerVPAdapter
import com.example.umc_flo.ui.PanelVPAdapter

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

    private var currentPage = 0
    private val slideHandler = Handler(Looper.getMainLooper())

    private val slideRunnable = object : Runnable {
        override fun run() {
            val adapter = homeBinding.panelVP.adapter
            val itemCount = adapter?.itemCount ?: 0

            if (itemCount > 0) {
                currentPage = (currentPage + 1) % itemCount
                homeBinding.panelVP.setCurrentItem(currentPage, true)
                slideHandler.postDelayed(this, 3000)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeBinding.albumImg.setOnClickListener {
            replaceFragment(AlbumFragment())
        }
        // 배너 뷰페이저
        homeBinding.bannerVP.adapter = BannerVPAdapter(this)

        // 판넬 뷰페이저
        val adapter = PanelVPAdapter()
        homeBinding.panelVP.adapter = adapter
        homeBinding.indicator.setViewPager(homeBinding.panelVP)

        // 자동 슬라이드
        slideHandler.postDelayed(slideRunnable, 3000)

    }
    private fun replaceFragment(fragment: Fragment){
        parentFragmentManager.beginTransaction()
            .replace(R.id.frameMain ,fragment)
            .commit()
    }
}