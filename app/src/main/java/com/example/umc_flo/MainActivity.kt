package com.example.umc_flo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.umc_flo.data.Song
import com.example.umc_flo.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {

    val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

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


        val song = Song(binding.miniTitle.text.toString(), binding.miniSinger.text.toString(), 0, 60, false)

        binding.miniPlayerFrame.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java).apply{
                putExtra("title", song.title)
                putExtra("singer", song.singer)
                putExtra("second", song.second)
                putExtra("playTime", song.playtime)
                putExtra("isPlaying", song.isPlaying)
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
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameMain, fragment)
            .commit()
    }
}