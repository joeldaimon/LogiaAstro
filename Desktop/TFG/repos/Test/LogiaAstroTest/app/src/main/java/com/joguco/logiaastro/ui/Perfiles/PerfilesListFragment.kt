package com.joguco.logiaastro.ui.Perfiles

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
import com.joguco.logiaastro.interfaces.OnUserClick

private const val ARG_COLUMN_COUNT = "ARG_COLUMN_COUNT"

/**
 *
 */

class PerfilesListFragment : Fragment() {
    //Atributes
    private var columnCount = 2
    var listener: OnUserClick? = null //Click Listener

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_perfiles_list, container, false)

        //Setting LayoutManager and RecyclerView
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

                var lista = (activity as BuscadorUserActivity?)?.getUserList()!!

                adapter = PerfilesRecyclerView(lista,listener)
            }
        }
        return view
    }

    //Function that attaches the activity and implements OnItemClick
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnUserClick) {
            listener = context
        }
    }

    //Function taht detaches the listener
    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    companion object {
        @JvmStatic
        fun newInstance(columnCount: Int) =
            PerfilesListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}