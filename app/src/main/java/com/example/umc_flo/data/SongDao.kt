package com.example.umc_flo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.umc_flo.data.entities.Song

@Dao
interface SongDao {
    @Insert
    fun insertSong(song: Song)
    @Update
    fun updateSong(song: Song)
    @Delete
    fun deleteSong(song: Song)

    @Query("SELECT * FROM songTable")
    fun getSongs(): List<Song>

    @Query("SELECT * FROM songTable WHERE id = :id")
    fun getSong(id: Int): Song

    @Query("UPDATE songTable SET isLike = :isLike WHERE id = :id")
    fun updateisLikeById(isLike: Boolean, id: Int)

    @Query("SELECT * FROM songTable WHERE isLike = :isLike")
    fun getLikedSongs(isLike: Boolean): List<Song>
}