package com.example.umc_flo.ui.main

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class FloApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "c16e9846a6e6ea90f6efe5ff96eeaa8f")
    }
}