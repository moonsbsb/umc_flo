package com.example.umc_flo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlbum(album: Album)

    @Query("SELECT * FROM AlbumTable")
    fun getAlbum() : List<Album>

    @Insert
    fun likeAlbum(like: Like)

    @Query("DELETE FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun disLikeAlbum(userId: Int, albumId: Int)

    @Query("SELECT AT.* FROM LikeTable as LT LEFT JOIN AlbumTable as AT ON LT.albumId = AT.albumIdx WHERE LT.userId = :userId")
    fun getLikedAlbums(userId: Int) : List<Album>

    @Query("SELECT id FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun isLikedAlbum(userId: Int, albumId: Int): Int?
}