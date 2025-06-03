package com.example.umc_flo.ui.main.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_flo.R
import com.example.umc_flo.data.entities.Album
import com.example.umc_flo.data.entities.Song
import com.example.umc_flo.data.SongDatabase
import com.example.umc_flo.databinding.FragmentHomeBinding
import com.example.umc_flo.ui.main.MainActivity
import com.example.umc_flo.ui.splash.BannerVPAdapter
import com.example.umc_flo.ui.splash.PanelVPAdapter
import com.example.umc_flo.ui.main.album.AlbumFragment
import com.example.umc_flo.ui.main.album.AlbumRVAdapter
import com.google.gson.Gson

class HomeFragment: Fragment() {

    val homeBinding by lazy{
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private var albumDatas = ArrayList<Album>()

    private lateinit var songDB : SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val albumDB = SongDatabase.getInstance(requireContext())
        albumDatas = ArrayList(albumDB!!.albumDao().getAlbum())

        songDB = SongDatabase.getInstance(requireContext())!!
        albumDatas.addAll(songDB.albumDao().getAlbum())

        val albumAdapter = AlbumRVAdapter(albumDatas)
        homeBinding.albumRV.adapter = albumAdapter
        homeBinding.albumRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        albumAdapter.setMyItemClickListener(object: AlbumRVAdapter.MyItemClickListener{
            override fun onItemClick(album: Album) {
                changeAlbumFragment(albumAdapter)
            }

            override fun onRemoveAlbum(position: Int) {
                albumAdapter.removeItem(position)
            }

            override fun OnPlayMiniPlayer(song: Song) {
                val activity = activity as? MainActivity
                activity?.setMiniPlayer(song)
            }

        })

        return homeBinding.root
    }

    private fun changeAlbumFragment(albumAdapter: AlbumRVAdapter) {
        albumAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener {
            override fun onItemClick(album: Album) {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.frameMain, AlbumFragment().apply {
                        arguments = Bundle().apply {
                            val gson = Gson()
                            val albumJson = gson.toJson(album)
                            putString("album", albumJson)
                        }
                    })
                    .commitAllowingStateLoss()
            }

            override fun onRemoveAlbum(position: Int) {
                albumAdapter.removeItem(position)
            }

            override fun OnPlayMiniPlayer(song: Song) {
                val activity = activity as? MainActivity
                activity?.setMiniPlayer(song)
            }

        })
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

        //homeBinding.albumImg.setOnClickListener {
        //    replaceFragment(AlbumFragment())
        //}
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
            .replace(R.id.frameMain,fragment)
            .commit()
    }
}