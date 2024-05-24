package com.joguco.logiaastro.tabs.numeros

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joguco.logiaastro.R
import com.joguco.logiaastro.interfaces.OnNumAngelClick
import com.joguco.logiaastro.model.NumAngeles


private const val ARG_COLUMN_COUNT = "param1"
private const val ARG_TYPE_LIST = "param2"

class NumAngelesListFragment : Fragment() {
    //Atributos
    private var columnCount = 4
    private var typeList = ""
    var listener: OnNumAngelClick? = null //Click Listener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            typeList = it.getString(ARG_TYPE_LIST).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflando layout
        val view = inflater.inflate(R.layout.fragment_num_angeles_list, container, false)

        //Setting LayoutManager y RecyclerView
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

                when(typeList){
                    "angeles" -> {
                        adapter = NumAngelesRecyclerView(NumAngeles.getAngeles(context),listener)
                    }
                    "maestro" -> {
                        adapter = NumAngelesRecyclerView(NumAngeles.getMaestros(context),listener)
                    }
                    "espejo" -> {
                        adapter = NumAngelesRecyclerView(NumAngeles.getEspejo(context),listener)
                    }
                }
            }
        }
        return view
    }

    /*
    * Función une la actividad e implementa OnNumAngelClick
    */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnNumAngelClick) {
            listener = context
        }
    }

    /*
    * Función que despega el OnNumAngelClick
     */    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @JvmStatic
        fun newInstance(columnCount: Int, typeList: String) =
            NumAngelesListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putString(ARG_TYPE_LIST, typeList)
                }
            }
    }
}