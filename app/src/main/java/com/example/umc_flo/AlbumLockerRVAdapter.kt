package com.example.umc_flo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_flo.data.Album
import com.example.umc_flo.databinding.ItemLockerBinding

class AlbumLockerRVAdapter() : RecyclerView.Adapter<AlbumLockerRVAdapter.ViewHolder>() {

    private val albums =  ArrayList<Album>()

    override fun getItemCount(): Int {
        return albums.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLockerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.lockerCoverImg.setImageResource(albums[position].coverImg!!)
        holder.binding.lockerTitle.text = albums[position].title
        holder.binding.lockerSinger.text = albums[position].singer
    }
    inner class ViewHolder(val binding: ItemLockerBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun addAlbums(albums: ArrayList<Album>){
        this.albums.clear()
        this.albums.addAll(albums)
        notifyDataSetChanged()
    }
}