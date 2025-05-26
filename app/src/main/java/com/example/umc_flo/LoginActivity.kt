package com.example.umc_flo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.umc_flo.data.SongDatabase
import com.example.umc_flo.data.User
import com.example.umc_flo.databinding.ActivityLoginBinding

class LoginActivity: AppCompatActivity(), LoginView {
    val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.join.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.loginBtn.setOnClickListener {
            login()
        }
    }
    private fun login(){
        if(binding.emailTxt.text.toString().isEmpty() || binding.emailBackTxt.text.toString().isEmpty()){
            Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.passwordTxt.text.toString().isEmpty()){
            Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            return
        }
        val email : String = binding.emailTxt.text.toString() + "@" + binding.emailBackTxt.text.toString()
        val pwd : String = binding.passwordTxt.text.toString()

        /*val songDB = SongDatabase.getInstance(this)!!
        val user = songDB.userDao().getUser(email, pwd)

        if(user == null) {
            Toast.makeText(this, "회원정보가 존재하지 않습니다", Toast.LENGTH_SHORT).show()
            return
        }

        user?.let {
            Log.d("LOGIN_ACT/GET_USER", "userId: ${user.id}, $user")
            //saveJwt(user.id)
            startMainActivity()
        }*/
        //
        //
        val authService = AuthService()
        authService.setLoginView(this)

        authService.login(User(email, pwd, ""))

    }
    private fun saveJwt(jwt: Int){
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putInt("saveJwt_jwt", jwt)
        editor.apply()
    }
    private fun saveJwt2(jwt: String){
        val spf = getSharedPreferences("auth2", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putString("saveJwt2_jwt", jwt)
        editor.apply()
    }
    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onSignUPSuccess(code: String, result: Result) {
        Log.d("LOGIN_CHECK", "로그인 성공~ JWT: ${result.jwt}")
        when(code){
            "200" -> {
                saveJwt2(result.jwt)
                startMainActivity()
            }else -> {
                onSignUpFailure(code)
            }
        }
    }

    override fun onSignUpFailure(code: String) {
        Toast.makeText(this, "로그인 실패: $code", Toast.LENGTH_SHORT).show()
    }
}