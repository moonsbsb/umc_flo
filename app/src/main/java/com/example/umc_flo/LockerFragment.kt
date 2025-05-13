package com.example.umc_flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.umc_flo.data.Album
import com.example.umc_flo.databinding.FragmentLockerBinding
import com.example.umc_flo.ui.LockerRVAdapter

class LockerFragment: Fragment() {

    private var _binding: FragmentLockerBinding? = null
    private val lockerBinding get() = _binding!!
    val datas = ArrayList<Album>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLockerBinding.inflate(inflater, container, false)

        Log.d("TEST", "binding 초기화 완료")

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