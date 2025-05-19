package com.example.umc_flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_flo.data.Album
import com.example.umc_flo.data.SongDatabase
import com.example.umc_flo.databinding.FragmentSavedalbumBinding

class SavedAlbumFragment: Fragment() {

    val binding by lazy {
        FragmentSavedalbumBinding.inflate(layoutInflater)
    }
    private lateinit var albumDB : SongDatabase
    private lateinit var adapter : AlbumLockerRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        albumDB = SongDatabase.getInstance(requireContext())!!
        initRecyclerView()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerView()
    }
    private fun initRecyclerView(){
        adapter = AlbumLockerRVAdapter()

        binding.likedAlbumRV.adapter = adapter
        binding.likedAlbumRV.layoutManager = LinearLayoutManager(requireContext())

        loadLikedAlbums()

    }
    private fun loadLikedAlbums(){
        val likedAlbum = albumDB.albumDao().getLikedAlbums(getJwt())
        adapter.addAlbums(likedAlbum as ArrayList<Album>)
    }
    private fun getJwt() : Int{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val jwt = spf!!.getInt("jwt", 0)

        return jwt
    }
}