package com.example.umc_flo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumTable")
class Album(
    @PrimaryKey
    var id: Int = 0,
    var singer: String,
    var title: String,
    var albumCover: Int
) {
}