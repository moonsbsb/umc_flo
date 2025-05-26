package com.example.umc_flo

import com.example.umc_flo.data.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRetrofitInterface {
    @POST("/join")
    fun signUp(@Body user: User): Call<AuthResponse>
    @POST("/login")
    fun login(@Body user: User): Call<AuthResponse>
}