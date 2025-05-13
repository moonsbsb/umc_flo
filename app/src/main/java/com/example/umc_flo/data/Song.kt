package com.example.umc_flo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity (tableName = "songTable")
data class Song (
    val title: String = "",
    val singer: String = "",
    var second: Int = 0,
    var playtime: Int = 0,
    var isPlaying: Boolean = false,
    var music: String = "",
    var coverImg: Int? = null,
    var isLike: Boolean = false
) : Serializable {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
