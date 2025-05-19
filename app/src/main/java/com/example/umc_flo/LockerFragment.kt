package com.example.umc_flo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.umc_flo.data.SongDatabase
import com.example.umc_flo.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class LockerFragment : Fragment() {
    private var _binding: FragmentLockerBinding? = null
    private val binding get() = _binding!!

    private lateinit var songDB: SongDatabase
    private lateinit var adapter: LockerVPAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLockerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        songDB = SongDatabase.getInstance(requireContext())!!

        initViewPager()
        initTabLayout()
        initButtons()
        updateLoginUI()
    }

    private fun initViewPager() {
        adapter = LockerVPAdapter(this)
        binding.lockerVP.adapter = adapter
    }

    private fun initTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.lockerVP) { tab, position ->
            tab.text = when (position) {
                0 -> "저장한 곡"
                1 -> "음악 파일"
                2 -> "저장 앨범"
                else -> ""
            }
        }.attach()
    }

    private fun initButtons() {
        binding.allSelectTxt.setOnClickListener {
            BottomSheetFragment().show(parentFragmentManager, "bottom_sheet")
        }

        binding.dislikeBtn.setOnClickListener {
            lifecycleScope.launch {
                songDB.songDao().getLikedSongs(true).forEach {
                    songDB.songDao().updateisLikeById(false, it.id)
                }
            }
        }

        binding.lockerLogin.setOnClickListener {
            val jwt = getJwt()
            if (jwt == 0) {
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
            } else {
                logout()
                startActivity(Intent(requireActivity(), MainActivity::class.java))
            }
        }
    }

    private fun updateLoginUI() {
        binding.lockerLogin.text = if (getJwt() == 0) "로그인" else "로그아웃"
    }

    private fun getJwt(): Int {
        val spf = requireActivity().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf.getInt("jwt", 0)
    }

    private fun logout() {
        val spf = requireActivity().getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        spf.edit().remove("jwt").apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
