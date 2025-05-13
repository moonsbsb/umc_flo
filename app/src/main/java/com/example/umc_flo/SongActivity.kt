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

class SongActivity : AppCompatActivity() {

    var repeatStatus = "inactive"
    var shuffleStatus = "inactive"
    var playStatus = "inactive"

    private val songBinding by lazy {
        ActivitySongBinding.inflate(layoutInflater)
    }

    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null
    private var gson: Gson = Gson()

    val songs = arrayListOf<Song>()
    // lateinit var songDB: SongDatabase
    var nowPos = 0

    private lateinit var fireDB: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(songBinding.root)

        fireDB = FirebaseDatabase.getInstance().reference

        //val myRef = fireDB.getReference("message")

        //myRef.setValue("Hello, World!")

        initPlayList()
        initSong()
        setPlayer(songs[nowPos])
        initClickListener()

        val title = intent.getStringExtra("title")
        val singer = intent.getStringExtra("singer")
        songBinding.songTitle.text = title
        songBinding.songSinger.text = singer
    }

    private fun initSong() {
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)
        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun setPlayer(song: Song) {
        songBinding.songTitle.text = song.title
        songBinding.songSinger.text = song.singer
        songBinding.startTime.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        songBinding.endTime.text = String.format("%02d:%02d", song.playtime / 60, song.playtime % 60)
        song.coverImg?.let { songBinding.songAlbumImg.setImageResource(it) }

        val musicResId = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, musicResId)

        if (song.isLike) {
            songBinding.songLike.setImageResource(R.drawable.ic_my_like_on)
        } else {
            songBinding.songLike.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(song.isPlaying)
    }

    private fun setPlayerStatus(isPlaying: Boolean) {
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if (isPlaying) {
            songBinding.playMoreBtn.visibility = View.GONE
            songBinding.relatedBtn.visibility = View.VISIBLE
            mediaPlayer?.start()
        } else {
            songBinding.playMoreBtn.visibility = View.VISIBLE
            songBinding.relatedBtn.visibility = View.GONE
            mediaPlayer?.pause()
        }
    }

    private fun startTimer() {
        timer = Timer(songs[nowPos].playtime, songs[nowPos].isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true) : Thread() {
        private var second: Int = songs[nowPos].second
        private var mills: Float = second * 1000f

        override fun run() {
            try {
                while (true) {
                    if (second >= playTime) {
                        if (repeatStatus == "active") {
                            songs[nowPos].second = 0
                            second = 0
                            mills = 0f
                            mediaPlayer?.seekTo(0)
                            if (songs[nowPos].isPlaying) mediaPlayer?.start()
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

                    if (isPlaying) {
                        sleep(50)
                        mills += 50
                        runOnUiThread {
                            songBinding.seekBar.progress = ((mills / playTime) * 100).toInt()
                        }
                        if (mills % 1000 == 0f) {
                            runOnUiThread {
                                songBinding.startTime.text =
                                    String.format("%02d:%02d", second / 60, second % 60)
                            }
                            second++
                        }
                    }
                }
            } catch (e: InterruptedException) {
                Log.d("Song", "쓰레드 종료: ${e.message}")
            }
        }
    }

    override fun onPause() {
        super.onPause()
        setPlayerStatus(false)
        songs[nowPos].second = ((songBinding.seekBar.progress * songs[nowPos].playtime) / 100) / 1000

        val spf = getSharedPreferences("song", MODE_PRIVATE).edit()
        val songJson = gson.toJson(songs[nowPos])
        spf.putString("songData", songJson).apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun initPlayList() {
        // songDB = SongDatabase.getInstance(this)!!
        // songs.addAll(songDB.songDao().getSongs())

        songs.add(Song("Lilac", "아이유 (IU)", 0, 230, false, "music_lilac", R.drawable.img_album_exp2, false))
        songs.add(Song("BBoom BBoom", "모모랜드 (MOMOLAND)", 0, 230, false, "music_lilac", R.drawable.img_album_exp5, false))
        songs.add(Song("Boy With Love", "방탄소년단 (BTS)", 0, 240, false, "music_lilac", R.drawable.img_album_exp4, false))
    }

    private fun setLike(isLike: Boolean) {
        val song = songs[nowPos]
        song.isLike = !isLike

        // songDB.songDao().updateisLikeById(!isLike, song.id)

        // Firebase에 저장
        val likePath = "likes/${song.title}_${song.singer}"
        fireDB.child(likePath).setValue(song)

        if (!isLike) {
            songBinding.songLike.setImageResource(R.drawable.ic_my_like_on)
        } else {
            songBinding.songLike.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun initClickListener() {
        songBinding.nextBtn.setOnClickListener { moveSong(+1) }
        songBinding.previousBtn.setOnClickListener { moveSong(-1) }

        songBinding.downBtn.setOnClickListener {
            val title = songBinding.songTitle.text.toString()
            val resultIntent = Intent().apply {
                putExtra("title", title)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        songBinding.songLike.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }

        songBinding.repeatBtn.setOnClickListener {
            repeatStatus = if (repeatStatus == "inactive") {
                songBinding.repeatBtn.setImageResource(R.drawable.repeat_active)
                "active"
            } else {
                songBinding.repeatBtn.setImageResource(R.drawable.nugu_btn_repeat_inactive)
                "inactive"
            }
        }

        songBinding.shffleBtn.setOnClickListener {
            shuffleStatus = if (shuffleStatus == "inactive") {
                songBinding.shffleBtn.setImageResource(R.drawable.btn_actionbar_instagram)
                "active"
            } else {
                songBinding.shffleBtn.setImageResource(R.drawable.nugu_btn_random_inactive)
                "inactive"
            }
        }

        songBinding.songPlay.setOnClickListener {
            playStatus = if (playStatus == "inactive") {
                songBinding.songPlay.setImageResource(R.drawable.btn_miniplay_pause)
                songs[nowPos].isPlaying = true
                timer.isPlaying = true
                "active"
            } else {
                songBinding.songPlay.setImageResource(R.drawable.btn_miniplayer_play)
                songs[nowPos].isPlaying = false
                timer.isPlaying = false
                "inactive"
            }
        }
    }

    private fun moveSong(direct: Int) {
        if (nowPos + direct < 0) {
            CustomSnackBar.make(songBinding.root, "first song").show()
            return
        }
        if (nowPos + direct >= songs.size) {
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
