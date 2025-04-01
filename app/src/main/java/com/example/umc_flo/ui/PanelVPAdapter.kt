package com.example.umc_flo.ui

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.umc_flo.R

class PanelVPAdapter: RecyclerView.Adapter<PanelVPAdapter.PanelViewHolder>() {

    private val imageList = listOf(
        R.drawable.img_first_album_default,
        R.drawable.discovery_banner_aos
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PanelViewHolder {
        val imageView = ImageView(parent.context)
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return PanelViewHolder(imageView)
    }

    override fun onBindViewHolder(holder: PanelViewHolder, position: Int) {
        holder.imageView.setImageResource(imageList[position])
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class PanelViewHolder(val imageView: ImageView): RecyclerView.ViewHolder(imageView)
}