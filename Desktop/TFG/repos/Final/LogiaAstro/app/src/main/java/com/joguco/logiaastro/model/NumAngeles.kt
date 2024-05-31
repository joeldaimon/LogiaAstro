package com.joguco.logiaastro.model

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.joguco.logiaastro.R
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.Type

data class NumAngeles(
    val id: Int,
    val categoria: String,
    val mensaje: String,
    val short: String
){
    //Static object
    companion object{
        val numeros:MutableList<NumAngeles> = mutableListOf()
        var angeles:MutableList<NumAngeles> = mutableListOf()
        var espejo:MutableList<NumAngeles> = mutableListOf()
        var maestros:MutableList<NumAngeles> = mutableListOf()

        /*
        * Carga MutableList
         */
        fun loadNumAngeles(context: Context){
            //Cogemos contexto de datos JSON
            val raw = context.resources.openRawResource(R.raw.numangeles)

            //Lector de datos
            val rd = BufferedReader(InputStreamReader(raw))

            //Tipo de datos
            val listType: Type = object : TypeToken<MutableList<NumAngeles?>?>() {}.type

            //Creamos objeto GSON
            val gson = Gson()
            numeros.clear()

            val nums:List<NumAngeles> = gson.fromJson(rd, listType)

            //Guardamos numeros en MutableList
            numeros.addAll(nums)

            angeles = numeros.filter { it.categoria == "angeles"  } as MutableList<NumAngeles>
            maestros = numeros.filter { it.categoria == "maestro"  } as MutableList<NumAngeles>
            espejo = numeros.filter { it.categoria == "espejo"  } as MutableList<NumAngeles>

        }

        fun getAngeles(context: Context): MutableList<NumAngeles>{
            loadNumAngeles(context)
            return angeles
        }

        fun getMaestros(context: Context): MutableList<NumAngeles>{
            loadNumAngeles(context)
            return maestros
        }

        fun getEspejo(context: Context): MutableList<NumAngeles>{
            loadNumAngeles(context)
            return espejo
        }

        /*
        * Método que devuelve NumAngeles por ID
         */
        fun getNumAngelesById(context: Context,id:Int?): NumAngeles?{
            loadNumAngeles(context)
            val num = numeros.filter{
                it.id == id
            }

            return if(num.isNotEmpty()) num[0] else null
        }

    }
}
