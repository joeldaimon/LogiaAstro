package com.joguco.logiaastro.model

import android.content.Context
import com.joguco.logiaastro.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.Type
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class Horoscope(
    val id: Int,
    val sign:String,
    val element:String,
    val energy: String,
    val tags:Array<String>,
    val color:String,
    val regent:String,
    val exaltation: String,
    val fall: String,
    val tarotCard:String,
    val image:String,
    val degree: String,
    val summary:String
){
    //Static object
    companion object{
        val horoscopos:MutableList<Horoscope> = mutableListOf()

        /*
        * Devuelve a MutableList<Horoscope>
         */
        fun loadHoroscope(context: Context):MutableList<Horoscope>{
            //Cogemos contexto de datos datos_json
            val raw = context.resources.openRawResource(R.raw.horoscopo)

            //Lector de datos
            val rd = BufferedReader(InputStreamReader(raw))

            //Tipo de lista donde guadaremos datos
            val listType: Type = object : TypeToken<MutableList<Horoscope?>?>() {}.type

            //Creamos objeto GSON
            val gson = Gson()
            horoscopos.clear()

            //Guardamos datos
            val signs:List<Horoscope> = gson.fromJson(rd, listType)

            //Guardando signos en MutableList - horoscopos
            horoscopos.addAll(signs)

            return horoscopos

        }

        /*
        * Método que devuelve HOROSCOPE por ID
         */
        fun getHoroscopeById(id:Int?): Horoscope?{
            val signs = horoscopos.filter{
                it.id == id
            }

            return if(signs.isNotEmpty()) signs[0] else null
        }

    }

}
