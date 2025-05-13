package com.example.umc_flo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_flo.data.Song
import com.example.umc_flo.databinding.ItemLockerBinding
import com.example.umc_flo.ui.AlbumRVAdapter
import com.example.umc_flo.ui.LockerRVAdapter

class SavedSongRVAdapter () :
    RecyclerView.Adapter<SavedSongRVAdapter.ViewHolder>() {

    private val songs = ArrayList<Song>()
    interface MyItemClickListener{
        fun onRemoveSong(songId: Int)
    }
    private lateinit var mItemClickListener : MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLockerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.lockerCoverImg.setImageResource(songs[position].coverImg!!)
        holder.binding.lockerTitle.text = songs[position].title
        holder.binding.lockerSinger.text = songs[position].singer

        holder.binding.lockerSetting.setOnClickListener {
            mItemClickListener.onRemoveSong(songs[position].id)
            removeSongs(position)
        }
    }

    override fun getItemCount(): Int = songs.size

    @SuppressLint("NotifyDataSetChanged")
    fun addSongs(songs: ArrayList<Song>){
        this.songs.clear()
        this.songs.addAll(songs)

        notifyDataSetChanged()
    }
    @SuppressLint("NotifyDataSetChanged")
    fun removeSongs(position: Int){
        songs.removeAt(position)
        notifyDataSetChanged()
    }
    inner class ViewHolder(val binding: ItemLockerBinding) : RecyclerView.ViewHolder(binding.root)
}