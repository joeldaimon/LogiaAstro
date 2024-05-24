package com.joguco.logiaastro.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.joguco.logiaastro.database.entities.UserEntity
import com.joguco.logiaastro.model.NatalChart
import java.lang.reflect.Type

/*
* Converter de datos ARRAYLIST para la base de datos
 */
class DataConverter {
    @TypeConverter
    fun fromChartLangList(countryLang: ArrayList<NatalChart?>?): String? {
        if (countryLang == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<NatalChart?>?>() {}.type
        return gson.toJson(countryLang, type)
    }

    @TypeConverter
    fun toChartLangList(countryLangString: String?): ArrayList<NatalChart>? {
        if (countryLangString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<NatalChart?>?>() {}.type
        return gson.fromJson(countryLangString, type)
    }

    @TypeConverter
    fun fromUserLangList(countryLang: ArrayList<UserEntity?>?): String? {
        if (countryLang == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<UserEntity?>?>() {}.type
        return gson.toJson(countryLang, type)
    }

    @TypeConverter
    fun toUserLangList(countryLangString: String?): ArrayList<UserEntity>? {
        if (countryLangString == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<UserEntity?>?>() {}.type
        return gson.fromJson(countryLangString, type)
    }
}