package com.example.umc_flo.ui.main.locker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_flo.data.entities.Album
import com.example.umc_flo.databinding.ItemLockerBinding

class LockerRVAdapter(private val albumList: ArrayList<Album>) :
    RecyclerView.Adapter<LockerRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLockerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.lockerCoverImg.setImageResource(albumList[position].coverImg!!)
        holder.binding.lockerTitle.text = albumList[position].title
        holder.binding.lockerSinger.text = albumList[position].singer

        holder.binding.lockerSetting.setOnClickListener {
            clickListener.OnRemoveItem(position)
        }
    }

    override fun getItemCount(): Int = albumList.size

    class ViewHolder(val binding: ItemLockerBinding): RecyclerView.ViewHolder(binding.root)

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