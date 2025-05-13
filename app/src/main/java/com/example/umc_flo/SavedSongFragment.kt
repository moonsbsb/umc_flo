package com.example.umc_flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_flo.data.Song
import com.example.umc_flo.data.SongDatabase
import com.example.umc_flo.databinding.FragmentLockerSavedsongBinding
import kotlinx.coroutines.launch

class SavedSongFragment: Fragment() {
    lateinit var binding: FragmentLockerSavedsongBinding
    lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerSavedsongBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!

        binding.allSelectTxt.setOnClickListener {
            Log.d("SavedSongFragment", "눌렀다")
            val bottomSheet = BottomSheetFragment()
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        }
        binding.dislikeBtn.setOnClickListener {
            val likeList = songDB.songDao().getLikedSongs(true)
            lifecycleScope.launch {
                for(i in 0 until  likeList.size) {
                songDB.songDao().updateisLikeById(false, likeList[i].id)
                }
                val updateList = songDB.songDao().getLikedSongs(true)
                (binding.lockerSavedSongRecyclerView.adapter as SavedSongRVAdapter).addSongs(updateList as ArrayList<Song>)
            }
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerView()
    }
    private fun initRecyclerView(){
        binding.lockerSavedSongRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val songRVAdapter =  SavedSongRVAdapter()

        songRVAdapter.setMyItemClickListener(object : SavedSongRVAdapter.MyItemClickListener{
            override fun onRemoveSong(songId: Int) {
                songDB.songDao().updateisLikeById(false, songId)
            }


        })

        binding.lockerSavedSongRecyclerView.adapter = songRVAdapter
        songRVAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList<Song>)
    }
}