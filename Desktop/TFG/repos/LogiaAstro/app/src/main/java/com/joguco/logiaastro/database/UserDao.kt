package com.joguco.logiaastro.database

import androidx.room.*
import com.joguco.logiaastro.database.entities.UserEntity

@Dao
interface UserDao:MyDao {
    @Query("SELECT * FROM user_entity")
    override fun getAllUsers(): MutableList<UserEntity>

    @Insert
    override fun addUser(userEntity : UserEntity):Long

    @Query("SELECT * FROM user_entity WHERE id LIKE :id")
    override fun getUserById(id: Long): UserEntity

    @Query("SELECT * FROM user_entity WHERE username LIKE :name")
    override fun userExists(name: String): UserEntity

    @Update
    override fun updateUser(userEntity: UserEntity):Int

    @Delete
    override fun deleteUser(userEntity: UserEntity):Int
}