package com.example.umc_flo

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_flo.data.Song
import com.example.umc_flo.data.SongDatabase
import com.example.umc_flo.databinding.ActivitySongBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson

class SongActivity: AppCompatActivity() {

    var repeatStatus = "inactive"
    var shuffleStatus = "inative"
    var playStatus = "inactive"

    val songBinding by lazy {
        ActivitySongBinding.inflate(layoutInflater)
    }
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    private lateinit var fireDB: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(songBinding.root)

        // firebase 초기화
        fireDB = FirebaseDatabase.getInstance().reference

        initPlayList()
        initSong()
        setPlayer(songs[nowPos])
        initClickListener()

        // MainActivity intent값 전달받기
        val title = intent.getStringExtra("title")
        val singer = intent.getStringExtra("singer")

        songBinding.songTitle.text = title
        songBinding.songSinger.text = singer
    }

    private fun initSong(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        //nowPos = getPlaySongPosition(songId)

        startTimer()
        setPlayer(songs[nowPos])
    }

//    private fun getPlaySongPosition(songId: Int): Int{
//        for(i in 0 .. songs.size){
//            if(songs[i].id == songId){
//                return i
//            }
//        }
//        return 0
//    }

    private fun setPlayer(song: Song){
        songBinding.songTitle.text = song.title
        songBinding.songSinger.text = song.singer
        songBinding.startTime.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        songBinding.endTime.text = String.format("%02d:%02d", song.playtime / 60, song.playtime % 60)
        songBinding.songAlbumImg.setImageResource(song.coverImg!!)
        Log.d("Song", "playtime: ${song.playtime}")
        songBinding.seekBar.progress = (song.second * 1000 / song.playtime)


        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        if(song.isLike){
            songBinding.songLike.setImageResource(R.drawable.ic_my_like_on)
        }else{
            songBinding.songLike.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(song.isPlaying)
    }
    private fun setPlayerStatus(isPlaying: Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            songBinding.playMoreBtn.visibility = View.GONE
            songBinding.relatedBtn.visibility = View.VISIBLE
            mediaPlayer?.start()
        }else{
            songBinding.playMoreBtn.visibility = View.VISIBLE
            songBinding.relatedBtn.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
        }
    }

    private fun startTimer(){
        timer = Timer(songs[nowPos].playtime,songs[nowPos].isPlaying)
        Log.d("Timer", "start at second=${songs[nowPos].playtime}, mills=${songs[nowPos].second}")

        timer.start()
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true): Thread(){

        private var second: Int = songs[nowPos].second
        private var mills: Float = songs[nowPos].second * 1000f

        override fun run(){
            super.run()
            try{
                while(true){
                    if(second >= playTime) {
                        if(repeatStatus == "active"){

                            songs[nowPos].second = 0
                            second = 0
                            mills = 0f

                            mediaPlayer?.seekTo(0)
                            if(songs[nowPos].isPlaying){
                                mediaPlayer?.start()
                            }

                            runOnUiThread {
                                songBinding.seekBar.progress = 0
                                songBinding.startTime.text = "00:00"
                                timer.interrupt()
                                startTimer()
                            }
                            break
                        }
                        break
                    }
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
    // 사용자가 포커스를 잃었을 때 음악이 중지
    override fun onPause(){
        super.onPause()

        setPlayerStatus(false)
        songs[nowPos].second = ((songBinding.seekBar.progress * songs[nowPos].playtime)/100)/1000


        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() //에디터
        val songJson = gson.toJson(songs[nowPos])
        editor.putString("songData", songJson)

        editor.apply()


    }
    override fun onDestroy(){
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() // 미디어플레이어가 갖고있던 리소스 해제
        mediaPlayer = null //미디어 플레이어 해제
    }
    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }
    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateisLikeById(!isLike, songs[nowPos].id)

        if(!isLike){
            songBinding.songLike.setImageResource(R.drawable.ic_my_like_on)
        }else{
            songBinding.songLike.setImageResource(R.drawable.ic_my_like_off)
        }

    }

    private fun initClickListener(){
        songBinding.nextBtn.setOnClickListener {
            moveSong(+1)
        }
        songBinding.previousBtn.setOnClickListener {
            moveSong(-1)
        }
        songBinding.downBtn.setOnClickListener {
            val title = songBinding.songTitle.text.toString()

            val resultIntent = Intent().apply{
                putExtra("title", title)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        songBinding.songLike.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }

        songBinding.repeatBtn.setOnClickListener {
            if(repeatStatus == "inactive") {
                songBinding.repeatBtn.setImageResource(R.drawable.repeat_active)
                repeatStatus = "active"
            }else {
                songBinding.repeatBtn.setImageResource(R.drawable.nugu_btn_repeat_inactive)
                repeatStatus = "inactive" }
        }
        songBinding.shffleBtn.setOnClickListener {
            if(shuffleStatus == "inactive"){
                songBinding.shffleBtn.setImageResource(R.drawable.btn_actionbar_instagram)
                shuffleStatus = "active"
            }else{
                songBinding.shffleBtn.setImageResource(R.drawable.nugu_btn_random_inactive)
                shuffleStatus = "inactive" }
        }
        songBinding.songPlay.setOnClickListener {
            if(playStatus == "inactive"){
                songBinding.songPlay.setImageResource(R.drawable.btn_miniplay_pause)
                playStatus = "active"
                songs[nowPos].isPlaying = true
                timer.isPlaying = true
            }else{
                songBinding.songPlay.setImageResource(R.drawable.btn_miniplayer_play)
                playStatus = "inactive"
                songs[nowPos].isPlaying = false
                timer.isPlaying = false
            }
        }
    }
    private fun moveSong(direct: Int){
        if(nowPos + direct < 0){
            //Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            CustomSnackBar.make(songBinding.root, "fist song").show()
            return
        }
        if(nowPos + direct >= songs.size){
            //Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            CustomSnackBar.make(songBinding.root, "last song").show()
            return
        }
        nowPos += direct

        timer.interrupt()
        startTimer()

        mediaPlayer?.release()
        mediaPlayer = null

        setPlayer(songs[nowPos])
    }

}