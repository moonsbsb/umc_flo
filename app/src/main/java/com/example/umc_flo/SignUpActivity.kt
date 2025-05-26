package com.example.umc_flo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_flo.data.SongDatabase
import com.example.umc_flo.data.User
import com.example.umc_flo.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity: AppCompatActivity(), SignUpView {
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
        val name : String = binding.signupName.text.toString()

        return User(email, pwd, name)
    }
    /*private fun signUp(){
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
    }*/
    private fun signUp(){
        if(binding.signupEmailTxt.text.toString().isEmpty() || binding.signupEmailBackTxt.text.toString().isEmpty()){
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.signupName.text.toString().isEmpty()){
            Toast.makeText(this, "이름 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.signupPasswordTxt.text.toString() != binding.singupPasswordCheckTxt.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return

        }

        val authService = AuthService()
        authService.setSignUpView(this)

        authService.signUp(getUser())
    }

    override fun onSignUPSuccess() {
        finish()
    }

    override fun onSignUpFailure() {
        Toast.makeText(this, "회원가입실패", Toast.LENGTH_SHORT).show()
    }

}