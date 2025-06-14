package com.example.umc_flo.data.local

import com.example.umc_flo.data.entities.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRetrofitInterface {
    @POST("/join")
    fun signUp(@Body user: User): Call<AuthResponse>
    @POST("/login")
    fun login(@Body user: User): Call<AuthResponse>
}