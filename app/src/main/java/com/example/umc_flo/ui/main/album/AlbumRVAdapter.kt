package com.example.umc_flo.ui.main.album

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_flo.data.entities.Album
import com.example.umc_flo.data.entities.Song
import com.example.umc_flo.databinding.ItemAlbumBinding

class AlbumRVAdapter(private val albumList: ArrayList<Album>): RecyclerView.Adapter<AlbumRVAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun onItemClick(album: Album)
        fun onRemoveAlbum(position: Int)
        fun OnPlayMiniPlayer(song: Song)
    }

    private lateinit var mItemClikListner: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClikListner = itemClickListener
    }

    fun addItem(album: Album){
        albumList.add(album)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemAlbumBinding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.itemView.setOnClickListener { mItemClikListner.onItemClick(albumList[position]) }

        holder.binding.title.setOnClickListener { mItemClikListner.onRemoveAlbum(position) }

        holder.binding.albumPlay.setOnClickListener {
            val album = albumList[position]

            val song = Song(
                title = album.title!!,
                singer = album.singer!!,
                playtime = 1
            )
            mItemClikListner.OnPlayMiniPlayer(song)
        }
    }

    override fun getItemCount(): Int = albumList.size

    inner class ViewHolder(val binding: ItemAlbumBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(album: Album) {
            binding.title.text = album.title
            binding.singer.text = album.singer
            //binding.coverImg.setImageResource(album.coverImg!!)

        }
    }

}