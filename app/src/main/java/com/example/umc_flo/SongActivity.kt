package com.example.umc_flo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_flo.data.Song
import com.example.umc_flo.databinding.ActivitySongBinding

class SongActivity: AppCompatActivity() {

    var repeatStatus = "inactive"
    var shuffleStatus = "inative"
    var playStatus = "inactive"

    val songBinding by lazy {
        ActivitySongBinding.inflate(layoutInflater)
    }
    lateinit var song: Song
    lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(songBinding.root)

        initSong()
        setPlayer(song)

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
                song.isPlaying = true
                timer.isPlaying = true
            }else{
                songBinding.songPlay.setImageResource(R.drawable.btn_miniplayer_play)
                playStatus = "inactive"
                song.isPlaying = false
                timer.isPlaying = false
            }
        }
    }
    private fun initSong(){
        if(intent.hasExtra("title") && intent.hasExtra("singer")){
            song = Song(
                intent.getStringExtra("title")!!,
                intent.getStringExtra("singer")!!,
                intent.getIntExtra("second", 0)!!,
                intent.getIntExtra("playTime", 0)!!,
                intent.getBooleanExtra("isPlaying", false)
            )
        }
        startTimer()
    }
    private fun setPlayer(song: Song){
        songBinding.songTitle.text = intent.getStringExtra("title")!!
        songBinding.songSinger.text = intent.getStringExtra("singer")!!
        songBinding.startTime.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        songBinding.endTime.text = String.format("%02d:%02d", song.playtime / 60, song.playtime % 60)
        Log.d("Song", "playtime: ${song.playtime}")
        songBinding.seekBar.progress = (song.second * 1000 / song.playtime)

        setPlayerStatus(song.isPlaying)
    }
    private fun setPlayerStatus(isPlaying: Boolean){
        song.isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            songBinding.playMoreBtn.visibility = View.GONE
            songBinding.relatedBtn.visibility = View.VISIBLE
        }else{
            songBinding.playMoreBtn.visibility = View.VISIBLE
            songBinding.relatedBtn.visibility = View.GONE
        }
    }

    private fun startTimer(){
        timer = Timer(song.playtime, song.isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true): Thread(){

        private var second: Int = 0
        private var mills: Float = 0f

        override fun run(){
            super.run()
            try{
                while(true){
                    if(second >= playTime)
                        break
                    if(isPlaying){
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            songBinding.seekBar.progress = ((mills / playTime)*100).toInt()
                        }
                        if(mills % 1000 == 0f){
                            runOnUiThread {
                                songBinding.startTime.text = String.format("%02d:%02d", second / 60, second % 60)
                            }
                            second++
                        }
                    }
                }
            }catch (e: InterruptedException){
                Log.d("Song", "쓰레드가 죽었습니다. ${e.message}")
            }
        }
    }
}