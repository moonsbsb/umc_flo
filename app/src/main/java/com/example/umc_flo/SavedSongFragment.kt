package com.example.umc_flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_flo.data.Song
import com.example.umc_flo.data.SongDatabase
import com.example.umc_flo.databinding.FragmentLockerSavedsongBinding

class SavedSongFragment : Fragment() {
    private var _binding: FragmentLockerSavedsongBinding? = null
    private val binding get() = _binding!!

    private lateinit var songDB: SongDatabase
    private lateinit var savedSongAdapter: SavedSongRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLockerSavedsongBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!
        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        savedSongAdapter = SavedSongRVAdapter()

        savedSongAdapter.setMyItemClickListener(object : SavedSongRVAdapter.MyItemClickListener {
            override fun onRemoveSong(songId: Int) {
                songDB.songDao().updateisLikeById(false, songId)
                loadLikedSongs()
            }
        })

        binding.lockerSavedSongRecyclerView.adapter = savedSongAdapter
        binding.lockerSavedSongRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadLikedSongs()
    }

    private fun loadLikedSongs() {
        val likedSongs = songDB.songDao().getLikedSongs(true)
        savedSongAdapter.addSongs(likedSongs as ArrayList<Song>)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
