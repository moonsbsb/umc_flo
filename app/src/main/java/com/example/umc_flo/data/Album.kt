package com.example.umc_flo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "AlbumTable")
data class Album(
    @PrimaryKey (autoGenerate = false) var albumIdx: Int? = null,
    var title: String? = null,
    var singer: String? = null,
    var coverImg: Int? = null
)