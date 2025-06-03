package com.example.umc_flo.data.local

import android.util.Log
import com.example.umc_flo.ui.signin.LoginView
import com.example.umc_flo.ui.signup.SignUpView
import com.example.umc_flo.data.entities.User
import com.example.umc_flo.util.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView

    fun setSignUpView(signUpView: SignUpView){
        this.signUpView = signUpView
    }
    fun setLoginView(loginView: LoginView){
        this.loginView = loginView
    }
    fun signUp(user : User){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        authService.signUp(user).enqueue(object: Callback<AuthResponse> {
            //응답이 왔을 때
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUP/SUCCESS", response.toString())
                val resp: AuthResponse? = response.body()
                when(resp?.code){
                    "200" -> signUpView.onSignUPSuccess()
                    else -> {
                        signUpView.onSignUPSuccess()
                    }
                }
            }
            // 네트워크 연결 실패 시
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUP/FAILURE", t.toString())
            }
        })
        Log.d("SIGNUP", "Hello")
    }
    fun login(user: User){
        val authService = getRetrofit().create(AuthRetrofitInterface::class.java)
        authService.login(user).enqueue(object: Callback<AuthResponse>{
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("LOGIN/SUCCESS", response.toString())
                val resp: AuthResponse? = response.body()
                when(val code = resp?.code){
                    "200" -> loginView.onSignUPSuccess(code, resp.result!!)
                    else -> loginView.onSignUpFailure(code!!)
                }
            }
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("LOGIN/FAILURE", t.toString())
            }


        })
    }
}