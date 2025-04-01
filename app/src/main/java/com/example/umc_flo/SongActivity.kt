package com.example.umc_flo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_flo.databinding.ActivitySongBinding

class SongActivity: AppCompatActivity() {

    var repeatStatus = "inactive"
    var shuffleStatus = "inative"
    var playStatus = "inactive"

    val songBinding by lazy {
        ActivitySongBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(songBinding.root)


        // MainActivity intent값 전달받기
        val title = intent.getStringExtra("title")
        val singer = intent.getStringExtra("singer")

        songBinding.songTitle.text = title
        songBinding.songSinger.text = singer

        songBinding.downBtn.setOnClickListener {
            val title = songBinding.songTitle.text.toString()

            val resultIntent = Intent().apply{
                putExtra("title", title)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
        songBinding.repeatBtn.setOnClickListener {
            if(repeatStatus == "inactive") {
                songBinding.repeatBtn.setImageResource(R.drawable.btn_actionbar_instagram)
                repeatStatus = "active"
            }else {
                songBinding.repeatBtn.setImageResource(R.drawable.nugu_btn_repeat_inactive)
                repeatStatus = "inactive"
            }
        }
        songBinding.shffleBtn.setOnClickListener {
            if(shuffleStatus == "inactive"){
                songBinding.shffleBtn.setImageResource(R.drawable.btn_actionbar_instagram)
                shuffleStatus = "active"
            }else{
                songBinding.shffleBtn.setImageResource(R.drawable.nugu_btn_random_inactive)
                shuffleStatus = "inactive"
            }
        }
        songBinding.songPlay.setOnClickListener {
            if(playStatus == "inactive"){
                songBinding.songPlay.setImageResource(R.drawable.btn_miniplay_pause)
                playStatus = "active"
            }else{
                songBinding.songPlay.setImageResource(R.drawable.btn_miniplayer_play)
                playStatus = "inactive"
            }
        }
    }
}