package com.joguco.logiaastro.tabs.Perfiles

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.joguco.logiaastro.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val ARG_USER = "ARG_USER"

/**
 * Detalle de Perfiles
 */
class PerfilesDetailFragment : Fragment() {
    //Atributes
    private var user: String? = null

    //Firebase base de datos
    private val db = Firebase.firestore

    //Dialogo inflado
    private lateinit var vistaDialogo: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getString(ARG_USER)
        }
    }

    //Function that fills the fragment when its created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch{
            user?.let {
                db.collection("users").document(it).get().addOnSuccessListener {result ->

                    view.findViewById<TextView>(R.id.tvUsername).text = result.get("username").toString()
                    var followers = result.get("followers") as ArrayList<Int>
                    view.findViewById<TextView>(R.id.tvFollowers).text = followers.size.toString()
                    var following = result.get("following") as ArrayList<Int>
                    view.findViewById<TextView>(R.id.tvFollowing).text = following.size.toString()
                    view.findViewById<TextView>(R.id.tvText).text = if(result.get("post").toString().isNotEmpty()) result.get("post").toString() else "Holi, ¡soy un nuevo usuario!"

                    //Imágen
                    val imagen = result.get("image") as String
                    val context: Context = view.findViewById<ImageView>(R.id.ivProfile).context
                    if(imagen.isNotEmpty()){
                        val id = context.resources.getIdentifier(
                            imagen.lowercase(),
                            "raw",
                            context.packageName
                        )
                        view.findViewById<ImageView>(R.id.ivProfile).setImageResource(id)

                    } else {
                        val id = context.resources.getIdentifier(
                            "libra",
                            "raw",
                            context.packageName
                        )
                        view.findViewById<ImageView>(R.id.ivProfile).setImageResource(id)
                    }

                    //Mensajes
                    val mensajesUser = result.get("mensajes") as ArrayList<String>
                    val msgAdapter: ArrayAdapter<*>
                    msgAdapter = ArrayAdapter(requireContext(),
                        android.R.layout.simple_list_item_1, mensajesUser)

                    view.findViewById<ListView>(R.id.lvMensajes).adapter = msgAdapter

                    //Botón enviar Mensaje
                    view.findViewById<Button>(R.id.btnSend).setOnClickListener{

                        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                        builder
                            .setTitle(getText(R.string.numerology_load_title))
                            .setView(vistaDialogo)
                            .setPositiveButton(getText(R.string.util_aceptar)) { dialog, which ->
                                var msg: EditText = vistaDialogo.findViewById(R.id.etPost)
                                var texto = msg.text.toString()
                                if(msg.text.toString().isNotEmpty() && msg!=null){
                                    var db = Firebase.firestore
                                    CoroutineScope(Dispatchers.Main).launch{
                                        db.collection("users").document(user!!).get()
                                            .addOnSuccessListener {
                                                var mensajes = it.get("mensajes") as ArrayList<String>
                                                mensajes.add(msg.text.toString())
                                                (activity as PerfilesActivity).updateMessage(mensajes, user!!)
                                            }
                                            .addOnFailureListener { exception ->
                                                Log.i("LOGIA-ASTRO", "Error al actualizar usuario en PerfilesDetailFragment: ", exception)
                                            } .await()
                                    }
                                    Toast.makeText(requireContext(), R.string.util_si_send, Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(requireContext(), R.string.util_no_send, Toast.LENGTH_LONG).show()
                                }

                            }
                            .setNegativeButton(getText(R.string.util_cancelar)) { dialog, which ->
                                Toast.makeText(requireContext(), R.string.util_cancel_action, Toast.LENGTH_LONG).show()
                            }

                        val dialog: AlertDialog = builder.create()
                        dialog.show()
                    }
                }.await()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vistaDialogo = this.layoutInflater.inflate(R.layout.dialog_post, null)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfiles_detail, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(username: String) =
            PerfilesDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USER, username)
                }
            }
    }
}