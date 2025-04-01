package com.example.umc_flo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AlbumDao {

    @Insert
    fun insertAlbum(album: Album)

    @Delete
    fun deleteAlbum(album: Album)

    @Update
    fun updateAlbum(album: Album)

    @Query("SELECT * FROM albumtable")
    fun getAllalbum() : List<Album>
}