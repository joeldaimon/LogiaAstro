package com.joguco.logiaastro.tabs.Perfiles

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.joguco.logiaastro.R
import com.joguco.logiaastro.interfaces.OnUserClick
import com.joguco.logiaastro.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val ARG_COLUMN_COUNT = "ARG_COLUMN_COUNT"
private const val ARG_ACTUAL_USER = "ARG_ACTUAL_USER"

/**
 * PerfilesList Fragment
 */

class PerfilesListFragment : Fragment() {
    //Atributos
    private var columnCount = 2
    private var actualUser = ""
    private lateinit var following: ArrayList<String>
    var listener: OnUserClick? = null //Click Listener
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            actualUser = it.getString(ARG_ACTUAL_USER).toString()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.let{
                val intent = Intent (it, MainActivity::class.java)
                it.startActivity(intent)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflamos layout
        val view = inflater.inflate(R.layout.fragment_perfiles_list, container, false)

        //Setting LayoutManager y RecyclerView
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

                //Lista de seguidos de actualUser
                var userList: MutableList<Map<String,Any?>> = mutableListOf()

                //Datos de usuarios con Firebase
                CoroutineScope(Dispatchers.Main).launch{
                    db.collection("users").document(actualUser).get().addOnSuccessListener {
                        following = it.get("following") as ArrayList<String>
                    }.await()
                    db.collection("users").get()
                        .addOnSuccessListener { documents ->
                            for (user in documents) {
                                userList.add(user.data)
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.i("JOELDAIMON", "Error al cargar lista de usuarios: ", exception)
                        } .await()

                    adapter = PerfilesRecyclerView(userList,actualUser,following,listener)

                }
            }
        }
        return view
    }

    /*
    * Método une la actividad e implementa onUserClick
    */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnUserClick) {
            listener = context
        }
    }

    /*
    * Método que despega el onUserClick
     */
    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    companion object {
        @JvmStatic
        fun newInstance(columnCount: Int, actualUser: String) =
            PerfilesListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putString(ARG_ACTUAL_USER, actualUser)
                }
            }
    }
}