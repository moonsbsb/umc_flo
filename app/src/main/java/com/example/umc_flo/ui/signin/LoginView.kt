package com.example.umc_flo.ui.signin

import com.example.umc_flo.data.local.Result

interface LoginView {
    fun onSignUPSuccess(code: String, result: Result)
    fun onSignUpFailure(code: String)
}