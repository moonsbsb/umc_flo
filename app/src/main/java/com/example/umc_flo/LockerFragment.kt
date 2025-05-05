package com.example.umc_flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_flo.data.Album
import com.example.umc_flo.databinding.FragmentLockerBinding
import com.example.umc_flo.ui.LockerRVAdapter

class LockerFragment: Fragment() {

    val lockerBinding by lazy {
        FragmentLockerBinding.inflate(layoutInflater)
    }
    val datas = ArrayList<Album>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        datas.apply {
            add(Album("Butter", "방탄소년 (BTS)", R.drawable.img_album_exp))
            add(Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
            add(Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3))
            add(Album("Boy With Love", "방탄소년단 (BTS)", R.drawable.img_album_exp4))
            add(Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5))
            add(Album("Weekend", "태연 (TAEYEON)", R.drawable.img_album_exp6))
            add(Album("How Sweet", "뉴진스 (NEWJEANS)", R.drawable.img_album_exp7))
            add(Album("Like Jennie", "제니 (JENNIE)", R.drawable.img_album_exp8))

        }

        val adapter = LockerRVAdapter(datas)
        lockerBinding.lockerRV.adapter = adapter
        lockerBinding.lockerRV.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        adapter.setOnItemClickListener(object: LockerRVAdapter.OnItemClickListener{
            override fun OnItemClick(view: View, position: Int) {
                TODO("Not yet implemented")
            }

            override fun OnRemoveItem(position: Int) {
                adapter.removeItem(position)
            }

        })

        return lockerBinding.root
    }
}