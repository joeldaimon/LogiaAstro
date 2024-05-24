package com.joguco.logiaastro.tabs.horoscopo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.joguco.logiaastro.databinding.ItemHoroscopoBinding
import com.joguco.logiaastro.model.Horoscope
import com.joguco.logiaastro.interfaces.OnHoroscopeClick

class HoroscopeRecyclerView(
    private val horoscopos: List<Horoscope>,
    private val listener: OnHoroscopeClick?
) : RecyclerView.Adapter<HoroscopeRecyclerView.ViewHolder>() {

    //Creamos ViewHolder para cada item de la lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemHoroscopoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    //Método que personaliza datos segun posición
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Cargando datos
        val sign = horoscopos[position]
        holder.tvSign.text = sign.element

        //Conversión de imágen
        val context: Context = holder.ivHoroscopo.context
        val imageName = sign.image.split(".")[0]
        val id = context.resources.getIdentifier(
            imageName,
            "raw",
            context.packageName
        )

        holder.ivHoroscopo.setImageResource(id)

        holder.itemView.tag = sign

        holder.itemView.setOnClickListener(holder)
    }

    //Método que devuelve tamaño de la lista
    override fun getItemCount(): Int = horoscopos.size

    /*
    * RecyclerView Holder
    *   Inner class
    */
    inner class ViewHolder(binding: ItemHoroscopoBinding)
        : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        //Cargando vista de item_horoscopo.xml
        val tvSign: TextView = binding.tvSign
        val ivHoroscopo: ImageView = binding.ivHoroscopo


        //Método onCLick de Horoscope
        override fun onClick(v: View?) {
            val sign = v?.tag as Horoscope

            listener?.onHoroscopeClick(sign)
        }
    }
}