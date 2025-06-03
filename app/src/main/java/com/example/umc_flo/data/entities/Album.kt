package com.example.umc_flo.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumTable")
data class Album(
    @PrimaryKey (autoGenerate = false) var albumIdx: Int? = null,
    var title: String? = null,
    var singer: String? = null,
    var coverImg: Int? = null
)