package com.example.umc_flo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_flo.data.SongDatabase
import com.example.umc_flo.data.User
import com.example.umc_flo.databinding.ActivitySignupBinding

class SignUpActivity: AppCompatActivity() {
    val binding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.singupBtn.setOnClickListener {
            signUp()
            finish()
        }
    }
    private fun getUser() : User{
        val email : String = binding.signupEmailTxt.text.toString() + "@" + binding.signupEmailBackTxt.text.toString()
        val pwd : String = binding.signupPasswordTxt.text.toString()

        return User(email, pwd)
    }
    private fun signUp(){
        if(binding.signupEmailTxt.text.toString().isEmpty() || binding.signupEmailBackTxt.text.toString().isEmpty()){
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.signupPasswordTxt.text.toString() != binding.singupPasswordCheckTxt.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return

        }
        val userDB = SongDatabase.getInstance(this)!!
        userDB.userDao().insert(getUser())

        val user = userDB.userDao().getUsers()
        Log.d("SIGNUPACT", user.toString())
    }


}