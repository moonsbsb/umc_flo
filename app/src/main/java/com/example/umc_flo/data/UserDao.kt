package com.example.umc_flo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.umc_flo.data.entities.User

@Dao
interface UserDao {

    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM UserTable")
    fun getUsers() : List<User>

    @Query("SELECT * FROM UserTable WHERE email = :email and password = :password")
    fun getUser(email: String, password: String): User?
}