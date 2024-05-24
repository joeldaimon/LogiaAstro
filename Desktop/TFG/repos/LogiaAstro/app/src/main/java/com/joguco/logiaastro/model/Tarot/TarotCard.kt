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


    //Function toString
    override fun toString(): String {
        return "($this.id) $this.name"
    }

    //Static object
    companion object{
        //We create a List of Series
        val Mazo:MutableList<TarotCard> = mutableListOf()

        /*
        * Function that loads the Cartas
        * Returns a MutableList<TarotCard>
         */
        fun loadSeries(context: Context):MutableList<TarotCard>{
            //We get the context of the data file - datos_json
            val raw = context.resources.openRawResource(R.raw.mazo)

            //Reader of data
            val rd = BufferedReader(InputStreamReader(raw))

            //Type of list we are going to use ot save the data
            val listType: Type = object : TypeToken<MutableList<TarotCard?>?>() {}.type

            //We create a gson Object
            val gson = Gson()
            Mazo.clear()

            //Saving data from json to variable TarotCard
            var cartas:List<TarotCard> = gson.fromJson(rd, listType)

            cartas = cartas.shuffled()

            //Saving TarotCard data to original MutableList
            Mazo.addAll(cartas)


            //Returning
            return Mazo

        }

        /*
        * Function that returns TarotCard by its ID
         */
        fun getCartaById(id:Int?): TarotCard?{
            val carta = Mazo.filter{
                it.id == id
            }

            return if(carta.isNotEmpty()) carta[0] else null
        }

    }
}
