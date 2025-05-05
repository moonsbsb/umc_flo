package com.example.umc_flo.data

import androidx.room.Entity

@Entity(tableName = "AlbumTable")
data class Album(
    var title: String? = null,
    var singer: String? = null,
    var coverImg: Int? = null,
    var songs: ArrayList<Song> = ArrayList()
) {
}