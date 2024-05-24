package com.joguco.logiaastro.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.joguco.logiaastro.database.entities.UserEntity

@Database(entities = arrayOf(UserEntity::class), version = 1)
@TypeConverters(DataConverter::class)
abstract class UserDatabase : RoomDatabase() {
    abstract fun taskDao(): UserDao

    companion object{ //Singleton Pattern
        private var instance:UserDao? = null

        fun getInstance(context: Context):UserDao{
            return instance ?: Room.databaseBuilder(context, UserDatabase::class.java, "logia-astro-db").build().taskDao().also { instance = it }
        }
    }
}