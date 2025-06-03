package com.example.umc_flo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.umc_flo.data.entities.Album
import com.example.umc_flo.data.entities.Like
import com.example.umc_flo.data.entities.Song
import com.example.umc_flo.data.entities.User

@Database(entities = [Song::class, User::class, Like::class, Album::class], version = 1)
abstract class SongDatabase : RoomDatabase(){
    abstract fun songDao() : SongDao
    abstract fun userDao() : UserDao
    abstract fun albumDao() : AlbumDao

    /*
    companion object{

        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context) : SongDatabase? {
            if(instance == null){
                synchronized(SongDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database"
                    ).allowMainThreadQueries().build()
                }
            }
            return  instance
        }
    }
    */
    companion object{
        @Volatile
        private var INSTANCE: SongDatabase? = null

        fun getInstance(context: Context) : SongDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext, SongDatabase::class.java, "song-database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }
}