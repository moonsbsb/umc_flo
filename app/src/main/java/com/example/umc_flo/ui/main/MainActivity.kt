package com.example.umc_flo.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.umc_flo.R
import com.example.umc_flo.data.entities.Album
import com.example.umc_flo.data.entities.Song
import com.example.umc_flo.data.SongDatabase
import com.example.umc_flo.databinding.ActivityMainBinding
import com.example.umc_flo.ui.main.home.HomeFragment
import com.example.umc_flo.ui.main.locker.LockerFragment
import com.example.umc_flo.ui.main.look.LookFragment
import com.example.umc_flo.ui.song.SongActivity
import com.google.gson.Gson
import com.kakao.sdk.common.util.Utility

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

        inputDummySongs()
        inputDummyAlbum()
        replaceFragment(HomeFragment())

        var keyHash = Utility.getKeyHash(this)
        Log.d("키해시: ", "$keyHash")

        //val song = Song(binding.miniTitle.text.toString(), binding.miniSinger.text.toString(), 0, 60, false, "music_lilac")
        binding.miniPlayerFrame.setOnClickListener {
            val editor = getSharedPreferences( "song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.homeItem -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.lookItem -> {
                    replaceFragment(LookFragment())
                    true
                }
                R.id.storageItem ->{
                    replaceFragment(LockerFragment())
                    true
                }
                else -> false
            }
        }
        Log.d("MAIN/JWT_TO_SERVER", getJwt().toString())
    }

    fun setMiniPlayer(song: Song?){
        if (song != null) {
            Log.d("MiniPlayer", "Song: ${song.title}, ${song.singer}, ${song.playtime}")
            binding.miniTitle.text = song.title
            binding.miniSinger.text = song.singer
            binding.mainSeekbar.progress = (song.second*100)/song.playtime
        } else {
            binding.miniPlayerFrame.visibility = View.GONE
            return
        }
    }

    override fun onStart() {
        super.onStart()
//        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
//        val songJson = sharedPreferences.getString("songData", null)
//
//        song = if(songJson == null){
//            Song("라일락", "아이유(IU)", 0, 60, false, "music_lilac")
//        }else{
//            gson.fromJson(songJson, Song::class.java)
//        }
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        val songDB = SongDatabase.getInstance(this)!!

        song = if(songId == 0){
            songDB.songDao().getSong(1)
        }else{
            songDB.songDao().getSong(songId)
        }
        //Log.d("song ID", song.id.toString())
        setMiniPlayer(song)
    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameMain, fragment)
            .commit()
    }
    private fun getJwt(): String?{
        val spf = this.getSharedPreferences("auth2", AppCompatActivity.MODE_PRIVATE)
        val jwt = spf.getString("jwt", "")
        Log.d("JWT_CHECK", "JWT: $jwt")
        return jwt
    }
    private fun inputDummySongs(){
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if (songs.isNotEmpty()) return

        songDB.songDao().insertSong(
            Song(
                "Lilac",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_lilac",
                R.drawable.img_album_exp2,
                false,
            )
        )

        songDB.songDao().insertSong(
            Song(
                "Flu",
                "아이유 (IU)",
                0,
                200,
                false,
                "music_flu",
                R.drawable.img_album_exp2,
                false,
            )
        )

        songDB.songDao().insertSong(
            Song(
                "Butter",
                "방탄소년단 (BTS)",
                0,
                190,
                false,
                "music_butter",
                R.drawable.img_album_exp,
                false,
            )
        )

        songDB.songDao().insertSong(
            Song(
                "Next Level",
                "에스파 (AESPA)",
                0,
                210,
                false,
                "music_next",
                R.drawable.img_album_exp3,
                false,
            )
        )


        songDB.songDao().insertSong(
            Song(
                "Boy with Luv",
                "music_boy",
                0,
                230,
                false,
                "music_lilac",
                R.drawable.img_album_exp4,
                false,
            )
        )


        songDB.songDao().insertSong(
            Song(
                "BBoom BBoom",
                "모모랜드 (MOMOLAND)",
                0,
                240,
                false,
                "music_bboom",
                R.drawable.img_album_exp5,
                false,
            )
        )

        //val _songs = songDB.songDao().getSongs()
        //Log.d("DB data", _songs.toString())
    }

    private fun inputDummyAlbum(){
        val albumDB = SongDatabase.getInstance(this)!!

        val albums = albumDB.albumDao().getAlbum()

        if (albums.isNotEmpty()) return

        albumDB.albumDao().insertAlbum(
            Album(1, "Butter", "방탄소년 (BTS)", R.drawable.img_album_exp)
        )
        albumDB.albumDao().insertAlbum(
            Album(2, "Lilac", "아이유 (IU)", R.drawable.img_album_exp2)
        )
        albumDB.albumDao().insertAlbum(
            Album(3, "Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3)
        )
        albumDB.albumDao().insertAlbum(
            Album(4, "Boy With Love", "방탄소년단 (BTS)", R.drawable.img_album_exp4)
        )
        albumDB.albumDao().insertAlbum(
            Album(5, "BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5)
        )
        albumDB.albumDao().insertAlbum(
            Album(6, "Weekend", "태연 (TAEYEON)", R.drawable.img_album_exp6)
        )
    }

}