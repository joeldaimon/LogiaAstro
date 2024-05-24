package com.joguco.logiaastro.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.joguco.logiaastro.model.NatalChart

@Entity(tableName = "user_entity")
data class UserEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var username:String = "",
    var password: String = "",
    var mail: String = "example@correo.com",
    var day:Int = 0,
    var month: Int = 0,
    var year: Int = 0,
    var hour: String = "0:00",
    var place: String = "Valencia",
    var image: String = "",
    var tarotLevel: Int = 1,
    var numerologyLevel: Int = 1,
    var astroLevel: Int = 1,
    val magicLevel: Int = 1,
    var followers: ArrayList<UserEntity> = ArrayList(),
    var following: ArrayList<UserEntity> = ArrayList(),
    var friends: ArrayList<UserEntity> = ArrayList(),
    var chartList: ArrayList<NatalChart> = ArrayList()
)