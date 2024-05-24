package com.joguco.logiaastro.tabs.horoscopo

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
import com.joguco.logiaastro.model.Horoscope
import com.joguco.logiaastro.interfaces.OnHoroscopeClick

// Parameters
private const val ARG_COLUMN_COUNT = "ARG_COLUMN_COUNT"

/**
 * LIST OF HOROSCOPES
 */
class HoroscopoListFragment : Fragment() {
    //Atributes
    private var columnCount = 2
    var listener: OnHoroscopeClick? = null //Click Listener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflamos layout
        val view = inflater.inflate(R.layout.fragment_horoscopo_list, container, false)

        //Setting LayoutManager y RecyclerView
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

                adapter = HoroscopeRecyclerView(Horoscope.loadHoroscope(context),listener)
            }
        }
        return view
    }

    /*
    * Función une la actividad e implementa onHoroscopeClick
    */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnHoroscopeClick) {
            listener = context
        }
    }

    /*
    * Función que despega el onHoroscopeClick
     */
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @JvmStatic
        fun newInstance(columnCount: Int) =
            HoroscopoListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}