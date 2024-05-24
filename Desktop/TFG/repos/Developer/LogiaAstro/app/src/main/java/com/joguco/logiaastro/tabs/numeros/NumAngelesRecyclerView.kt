package com.joguco.logiaastro.tabs.numeros

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.ItemAngelBinding
import com.joguco.logiaastro.interfaces.OnNumAngelClick
import com.joguco.logiaastro.model.NumAngeles

class NumAngelesRecyclerView(

    private val numAngeles: List<NumAngeles>,
    private val listener: OnNumAngelClick?
) : RecyclerView.Adapter<NumAngelesRecyclerView.ViewHolder>() {

    //Atributos para cambiar BackgroundColor de CardView
    private val colores = arrayOf(
        R.color.bg_body,
        R.color.rosa_palo,
        R.color.pastel_blue,
        R.color.pastel_green,
        R.color.light_salmon,
        R.color.pastel_violet,
        R.color.beige,
        R.color.light_blue,
        R.color.light_green,
        R.color.light_orange)

    private var contador = 0
    private lateinit var contextHolder: Context

    //Creamos ViewHolder para cada item de la lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        contextHolder = parent.context
        return ViewHolder(
            ItemAngelBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    //Función que personaliza datos segun posición
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Changing data
        val num = numAngeles[position]
        holder.tvNumEspejo.text = num.id.toString()

        //Cambiamos color del background del CardView
        holder.cvNumAngel.setCardBackgroundColor(ContextCompat.getColor(contextHolder, colores[contador]))
        contador++

        if(contador == (colores.size - 1)){
            contador = 0
        }

        //Tag del item
        holder.itemView.tag = num
        //Listener del item
        holder.itemView.setOnClickListener(holder)
    }

    //Función que devuelve tamaño de la lista
    override fun getItemCount(): Int = numAngeles.size

    /*
    * RecyclerView Holder
    *   Inner class
    */
    inner class ViewHolder(binding: ItemAngelBinding)
        : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        //Cargando vista de item_angel.xml
        val tvNumEspejo: TextView = binding.tvNumEspejo
        val cvNumAngel: CardView = binding.cvNumAngel

        //Función onCLick de NumAngel
        override fun onClick(v: View?) {
            val numAngel = v?.tag as NumAngeles

            listener?.onNumAngelClick(numAngel)
        }
    }
}