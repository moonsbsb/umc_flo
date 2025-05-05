package com.example.umc_flo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.umc_flo.data.Song
import com.example.umc_flo.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity: AppCompatActivity() {

    val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    private var song: Song = Song()
    private var gson: Gson = Gson()

    private val songActivityResult =  registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->
        if(result.resultCode == RESULT_OK){
            val title = result.data?.getStringExtra("title")
            Toast.makeText(this, "앨범제목: ${title}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        replaceFragment(HomeFragment())

        //val song = Song(binding.miniTitle.text.toString(), binding.miniSinger.text.toString(), 0, 60, false, "music_lilac")
        binding.miniPlayerFrame.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java).apply{
                putExtra("title", song.title)
                putExtra("singer", song.singer)
                putExtra("second", song.second)
                putExtra("playTime", song.playtime)
                putExtra("isPlaying", song.isPlaying)
                putExtra("music", song.music)
            }
            songActivityResult.launch(intent)
        }
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.homeItem -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.storageItem ->{
                    replaceFragment(LockerFragment())
                    true
                }
                else -> false
            }
        }
    }

    fun setMiniPlayer(song: Song){
        binding.miniTitle.text = song.title
        binding.miniSinger.text = song.singer
        binding.mainSeekbar.progress = (song.second*100)/song.playtime
    }

    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songJson = sharedPreferences.getString("songData", null)

        song = if(songJson == null){
            Song("라일락", "아이유(IU)", 0, 60, false, "music_lilac")
        }else{
            gson.fromJson(songJson, Song::class.java)
        }

        setMiniPlayer(song)

    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameMain, fragment)
            .commit()
    }
}