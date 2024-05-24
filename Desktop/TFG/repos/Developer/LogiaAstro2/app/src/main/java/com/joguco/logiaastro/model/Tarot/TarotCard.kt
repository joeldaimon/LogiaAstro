package com.joguco.logiaastro.model.Tarot


import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.joguco.logiaastro.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.Type

data class TarotCard(
    val id: Int,
    val nombre:String,
    val etiquetas: Array<String>,

    val loveAdvice: QuestionTheme,
    val loveFuture: QuestionTheme,
    val loveSpecial: QuestionTheme,

    val moneyAdvice: QuestionTheme,
    val moneyFuture: QuestionTheme,
    val moneySpecial: QuestionTheme,

    val workAdvice: QuestionTheme,
    val workFuture: QuestionTheme,
    val workSpecial: QuestionTheme,

    val friendsAdvice: QuestionTheme,
    val friendsFuture: QuestionTheme,
    val friendsSpecial: QuestionTheme,

    val spiritualityAdvice: QuestionTheme,
    val spiritualityFuture: QuestionTheme,
    val spiritualitySpecial: QuestionTheme,

    val respuesta: Int,
    val imagen: String){

    //Static object
    companion object{
        val Mazo:MutableList<TarotCard> = mutableListOf()

        /*
        * Método que carga MAZO
        *@returs    MutableList<TarotCard>
         */
        fun loadMazo(context: Context):MutableList<TarotCard>{
            //Contexto del JSON Mazo
            val raw = context.resources.openRawResource(R.raw.mazo)

            //Lector de datosa
            val rd = BufferedReader(InputStreamReader(raw))

            //Tipo de la lista
            val listType: Type = object : TypeToken<MutableList<TarotCard?>?>() {}.type

            //Creamos objeto GSON
            val gson = Gson()
            Mazo.clear()

            //Guardando datos en la lista
            var cartas:List<TarotCard> = gson.fromJson(rd, listType)

            //Mezclamos cartas
            cartas = cartas.shuffled()

            Mazo.addAll(cartas)

            return Mazo
        }
    }
}
