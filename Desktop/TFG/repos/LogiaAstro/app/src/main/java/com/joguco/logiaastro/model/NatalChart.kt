package com.joguco.logiaastro.model

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.joguco.logiaastro.R
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Type

data class NatalChart(
    @SerializedName("0") val ascendant: Planet,
    @SerializedName("1") val sun: Planet,
    @SerializedName("2") val moon: Planet,
    @SerializedName("3") val mars: Planet,
    @SerializedName("4") val mercury: Planet,
    @SerializedName("5") val jupiter: Planet,
    @SerializedName("6") val venus: Planet,
    @SerializedName("7") val saturn: Planet,
    @SerializedName("8") val rahu: Planet,
    @SerializedName("9") val ketu: Planet,
    @SerializedName("10") val uranus: Planet,
    @SerializedName("11") val neptune: Planet,
    @SerializedName("12") val pluto: Planet
){
    //Static object
    companion object{
        val chart:MutableList<NatalChart> = mutableListOf()

        /*
        * Devuelve a MutableList<NatalChart>
         */
        fun loadChart(data: String):MutableList<NatalChart>{
            val inputStream: InputStream = data.byteInputStream()
            //Lector de datos
            val rd = BufferedReader(InputStreamReader(inputStream))

            //Tipo de lista donde guadaremos datos
            val listType: Type = object : TypeToken<MutableList<NatalChart?>?>() {}.type

            //Creamos objeto GSON
            val gson = Gson()
            chart.clear()

            //Guardamos datos
            val planets:List<NatalChart> = gson.fromJson(data, listType)

            //Guardando signos en MutableList - NatalChart
            chart.addAll(planets)

            return chart

        }


    }
}

