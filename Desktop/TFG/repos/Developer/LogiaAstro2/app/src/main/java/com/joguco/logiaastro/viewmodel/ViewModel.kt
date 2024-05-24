package com.joguco.logiaastro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.joguco.logiaastro.R
import java.util.regex.Pattern

/**
 * Clase para VALIDACIONES de usuario
 */
class ViewModel (application: Application): AndroidViewModel(application) {
    //Contexto
    val context = application

    /*
    * Valida Email
    * @email    email
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

    /*
    * Valida timezone
    * @email    timezone
    * @return   boolean
    */
    fun isValidTimezone(timezone:String):Boolean{
        return Pattern.compile(
            "^-?[0-9]\\d*(\\.\\d+)?\$"
        ).matcher(timezone).matches()
    }
}