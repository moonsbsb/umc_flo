package com.example.umc_flo.data

data class Song (
    val title: String = "",
    val singer: String = "",
    var second: Int = 0,
    var playtime: Int = 0,
    var isPlaying: Boolean = false
)