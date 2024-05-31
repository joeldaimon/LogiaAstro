package com.joguco.logiaastro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.joguco.logiaastro.R
import java.security.MessageDigest
import java.util.regex.Pattern

/**
 * Clase para VALIDACIONES
 */
class ViewModel (application: Application): AndroidViewModel(application) {
    //Contexto
    val context = application

    /*
    * Valida nombre de usuario y lugar de nacimiento
    * @param    name
    * @return   boolean
    */
    fun isValidName(name: String): Boolean {
        return Pattern.compile(
            "^[A-Za-z]*$"
        ).matcher(name).matches()
    }

    /*
    * Valida Email
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
    * @param    hour
    * @return   boolean
    */
    fun isValidHour(hour:String):Boolean{
        return Pattern.compile(
            "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$"
        ).matcher(hour).matches()
    }

    /*
    * Valida fecha
    * @param    date
    * @return   boolean
    */
    fun isValidDate(date:String):Boolean{
        return Pattern.compile(
            "^(3[01]|[12][0-9]|0[1-9]|[1-9])/(1[0-2]|0[1-9]|[1-9])/[0-9]{4}$"
        ).matcher(date).matches()
    }

    /*
    * Valida lugar de nacimiento
    * @param    place
    * @return   boolean
    */
    fun isValidPlace(place: String): Boolean {
        return Pattern.compile(
            "^[A-Za-z]+([,][ ][A-Za-z]+)?$"
        ).matcher(place).matches()
    }


    /*
    * Encripta contraseña
    * @param    pwd
    * @return   ByteArray
    */
    fun encryptPwd(pwd:String):ArrayList<Long>{
        val data = pwd.toByteArray()
        val salt = "-jdlogiaastrotest".toByteArray()

        val sha256 = MessageDigest.getInstance("SHA-256")
        sha256.update(salt)
        val hashValue = sha256.digest(data)
        val list:ArrayList<Long> = arrayListOf()

        for(byte in hashValue){
            list.add(byte.toLong())
        }
        return list
    }

    /*
    * Desencripta contraseña
    * @param    pwd
    * @return   ByteArray
    */
    fun decryptPwd(original:ArrayList<Long>, typed: String):Boolean{
        val data = encryptPwd(typed)
        return original == data
    }
}