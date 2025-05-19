package com.example.umc_flo

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.umc_flo.data.Album

class LockerVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    lateinit var albumList: ArrayList<Album>

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SavedSongFragment()
            1 -> MusicFileFragment()
            else -> SavedAlbumFragment()
        }
    }
    interface OnItemClickListener{
        fun OnItemClick(view: View, position: Int)
        fun OnRemoveItem(position: Int)
    }

    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }

    private lateinit var clickListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.clickListener = listener
    }
}