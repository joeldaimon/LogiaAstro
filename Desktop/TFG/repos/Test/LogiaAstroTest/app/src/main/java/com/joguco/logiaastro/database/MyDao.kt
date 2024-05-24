package com.joguco.logiaastro.database

import com.joguco.logiaastro.database.entities.UserEntity

interface MyDao {
    fun getAllUsers(): MutableList<UserEntity>

    fun addUser(userEntity : UserEntity):Long //Id del nuevo User

    fun getUserById(id: Long): UserEntity

    fun userExists(name: String): UserEntity

    fun updateUser(userEntity: UserEntity):Int //Numero de filas afectadas

    fun deleteUser(userEntity: UserEntity):Int //Numero de filas afectadas
}