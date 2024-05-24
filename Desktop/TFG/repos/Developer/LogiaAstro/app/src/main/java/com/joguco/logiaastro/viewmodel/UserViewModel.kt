package com.joguco.logiaastro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.joguco.logiaastro.database.MyDao
import com.joguco.logiaastro.database.UserDatabase
import com.joguco.logiaastro.database.entities.UserEntity
import com.joguco.logiaastro.model.NatalChart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.regex.Pattern

class UserViewModel (application: Application): AndroidViewModel(application) {
    //Contexto
    val context = application

    //Database
    var myDao: MyDao = UserDatabase.getInstance(context)

    //UserList
    var userListLD: MutableList<UserEntity> = mutableListOf()

    /*
    * Carga la lista
    * @return    MutableList
     */
    fun getAllUsers(): MutableList<UserEntity>{
        viewModelScope.launch(Dispatchers.IO) {
            userListLD = myDao.getAllUsers()
        }
        return userListLD
    }

    /*
    * Dice si un user existe
    * @param    name, pwd
    * @return    Int
     */
    fun userExists(name: String, pwd: String): Int{
        var user = UserEntity()
        var job = viewModelScope.launch(Dispatchers.IO) {
            user = myDao.userExists(name)
        }
        runBlocking {
            job.join()
        }

        if(user.username!=""){
            return if(pwd == user.password){
                1
            } else 0
        }
        return 2
    }

    /*
    * Devuelve user por nombre
    * @param    name
    * @return   UserEntity
     */
    fun getByName(name: String): UserEntity? {
        var user = UserEntity()
        var job = viewModelScope.launch(Dispatchers.IO) {
            user = myDao.userExists(name)
        }
        runBlocking {
            job.join()
        }

        return user
    }

    /*
    * Devuelve user por ID
    * @param    id
    * @return   UserEntity
     */
    fun getUserById(id: Long): UserEntity{
        var user = UserEntity()
        viewModelScope.launch(Dispatchers.IO) {
            user = myDao.getUserById(id)
        }
        return user
    }

    /*
    * Añade usuario
     */
    fun add(name: String, pwd: String, mail: String, day: Int, month: Int, year: Int, hour: String, place: String): UserEntity {
        var id: Long = 0
        viewModelScope.launch(Dispatchers.IO) {
            id = myDao.addUser(UserEntity(
                username = name,
                password = pwd,
                mail = mail,
                day = day,
                month = month,
                year = year,
                hour = hour,
                place = place,
                image = "",
                tarotLevel = 1,
                numerologyLevel = 1,
                astroLevel = 1,
                magicLevel= 1,
                followers = ArrayList(),
                following = ArrayList(),
                friends = ArrayList(),
                chartList = ArrayList()
            ))


        }

        return getUserById(id)
    }

    /*
    * Borra usuario
    * @param    user
     */
    fun delete(user: UserEntity){
        viewModelScope.launch(Dispatchers.IO) {
            myDao.deleteUser(user)
        }
    }

    /*
    * Actualiza usuario
    * @param    user
    */
    fun update(user: UserEntity){
        viewModelScope.launch(Dispatchers.IO) {
            myDao.updateUser(user)
        }
    }

    /*
    * Valida email
    * @param    email
    * @return   boolean
    */
    fun isValidEmail(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }

    /*
    * Valida hora
    * @email    hour
    * @return   boolean
    */
    fun isValidHour(hour:String):Boolean{
        return Pattern.compile(
            "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$"
        ).matcher(hour).matches()
    }

    /*
    * Valida fecha
    * @email    date
    * @return   boolean
    */
    fun isValidDate(date:String):Boolean{
        return Pattern.compile(
            "^(3[01]|[12][0-9]|0[1-9]|[1-9])/(1[0-2]|0[1-9]|[1-9])/[0-9]{4}$"
        ).matcher(date).matches()
    }
}