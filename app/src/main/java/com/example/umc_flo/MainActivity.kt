package com.example.umc_flo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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

        binding.miniPlayerFrame.setOnClickListener {
            val intent = Intent(this, SongActivity::class.java).apply{
                putExtra("title", binding.miniTitle.text.toString())
                putExtra("singer", binding.miniSinger.text.toString())
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