package com.joguco.logiaastro.tabs.tarot

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.joguco.logiaastro.databinding.CardItemBinding
import com.joguco.logiaastro.interfaces.OnCardClick
import com.joguco.logiaastro.model.Tarot.TarotCard


class TarotCardAdapter(
    private val cartaList: List<TarotCard>,
    private val listener: OnCardClick?
) : RecyclerView.Adapter<TarotCardAdapter.ViewHolder>() {

    //Creamos ViewHolder para cada item de la lista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            CardItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,

                false
            )
        )

    }

    //Función que personaliza datos segun posición
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val carta = cartaList[position]

        //Conversión de la imágen
        val context: Context = holder.ivCard.getContext()
        val id = context.resources.getIdentifier(
            "cardback",
            "drawable",
            context.packageName
        )

        holder.ivCard.setImageResource(id)

        //Tag del item
        holder.itemView.tag = carta

        //Listener del item
        holder.itemView.setOnClickListener(holder)
    }

    //Function that returns the Size of the serieList
    override fun getItemCount(): Int = cartaList.size


    /*
    * RecyclerView Holder
    *   Inner class
    */
    inner class ViewHolder(binding: CardItemBinding)
        : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        //Cargando vista de item_card.xml
        val ivCard: ImageView = binding.ivCard

        //Función onCLick de TaroCard
        override fun onClick(v: View?) {

            val carta = v?.tag as TarotCard

            listener?.OnCardClick(carta)

            val id = ivCard.getContext().resources.getIdentifier(
                "cardbackclicked",
                "drawable",
                ivCard.getContext().packageName
            )

            ivCard.setImageResource(id)
        }
    }
}