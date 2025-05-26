package com.example.umc_flo

interface LoginView {
    fun onSignUPSuccess(code: String, result: Result)
    fun onSignUpFailure(code: String)
}