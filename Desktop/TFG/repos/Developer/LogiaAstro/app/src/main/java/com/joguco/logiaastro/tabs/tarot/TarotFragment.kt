package com.joguco.logiaastro.tabs.tarot

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joguco.logiaastro.R
import com.joguco.logiaastro.interfaces.OnCardClick
import com.joguco.logiaastro.model.Tarot.TarotCard

private const val ARG_COLUMN_COUNT = "param1"

/**
 * Fragmento TAROT
 */
class TarotFragment : Fragment() {
    //Atributos
    private var columnCount = 2
    var listener: OnCardClick? = null //Click Listener


    //Constructor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    /*
    * Función que une actividad y carga OnCardClick
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCardClick) {
            listener = context
        }
    }

    /*
    * Función que despegua Listener
     */
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflando layout
        val view = inflater.inflate(R.layout.fragment_tarot, container, false)


        //Setting LayoutManager y RecyclerView
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

                adapter = TarotCardAdapter(TarotCard.loadSeries(context),listener)
            }
        }
        return view
    }

    //Static object
    companion object {
        @JvmStatic
        fun newInstance(columnCount: Int) =
            TarotFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}