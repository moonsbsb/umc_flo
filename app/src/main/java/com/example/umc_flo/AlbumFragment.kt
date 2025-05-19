package com.example.umc_flo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.umc_flo.data.Album
import com.example.umc_flo.data.Like
import com.example.umc_flo.data.SongDatabase
import com.example.umc_flo.databinding.FragmentAlbumBinding
import com.example.umc_flo.ui.AlbumVPAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment: Fragment() {

    val albumBinding by lazy{
        FragmentAlbumBinding.inflate(layoutInflater)
    }
    private var gson: Gson = Gson()

    private var isLiked : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // home에서 넘어온 데이터 받아오기
        val albumJson = arguments?.getString("album")
        val album = gson.fromJson(albumJson, Album::class.java)

        //Home에서 넘어온 데이터를 반영
        isLiked = isLikedAlbum(album.albumIdx!!)
        setInit(album)
        setOnClickListeners(album)

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

    private fun setInit(album: Album){
        albumBinding.albumCover.setImageResource(album.coverImg!!)
        albumBinding.songAlbumTitle.text = album.title.toString()
        albumBinding.songSinger.text = album.singer.toString()

        if(isLiked){
            albumBinding.songLikeBtn.setImageResource(R.drawable.ic_my_like_on)
        }else{
            albumBinding.songLikeBtn.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun replaceFragment(fragment: Fragment){
        parentFragmentManager.beginTransaction()
            .replace(R.id.frameMain, fragment)
            .commit()
    }
    private fun getJwt(): Int{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)
    }
    private fun likeAlbum(userId: Int, albumId: Int){
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)

        songDB.albumDao().likeAlbum(like)
    }
    private fun isLikedAlbum(albumId: Int): Boolean{
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        val likeId : Int? = songDB.albumDao().isLikedAlbum(userId, albumId)

        return likeId != null
    }
    private fun disLikedAlbum(albumId: Int){
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        songDB.albumDao().disLikeAlbum(userId, albumId)
    }
    private fun setOnClickListeners(album: Album){
        val userId = getJwt()
        albumBinding.songLikeBtn.setOnClickListener {
            if(isLiked){
                albumBinding.songLikeBtn.setImageResource(R.drawable.ic_my_like_off)
                disLikedAlbum(album.albumIdx!!)
            }else{
                albumBinding.songLikeBtn.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.albumIdx!!)
            }
        }
    }
}