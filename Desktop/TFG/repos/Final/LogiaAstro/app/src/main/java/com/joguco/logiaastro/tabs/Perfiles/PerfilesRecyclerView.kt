package com.joguco.logiaastro.tabs.Perfiles

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.joguco.logiaastro.databinding.ItemUserBinding
import com.joguco.logiaastro.interfaces.OnUserClick
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class PerfilesRecyclerView(
    private val users: MutableList<Map<String,Any?>>,
    private val actualUser: String,
    private val following: ArrayList<String>,
    private val listener: OnUserClick?
) : RecyclerView.Adapter<PerfilesRecyclerView.ViewHolder>() {

    //Firebase base de datos
    private val db = Firebase.firestore

    /*
    * Método que crea ViewHoloder para cada item de la lista
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /*
    * Método que personaliza datos
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        users[position].forEach { entry ->
            when(entry.key){
                "username" -> {//Nombre del Usuario
                    holder.tvUsername.text = entry.value.toString()
                    //Tag del item
                    holder.itemView.tag = entry.value.toString()

                    if(entry.value.toString() == actualUser){
                        holder.btnFollow.isVisible = false
                    }
                    holder.btnFollow.setOnClickListener{
                        followUser(entry.value.toString())
                    }
                    if(following.contains(entry.value.toString())){
                        holder.btnFollow.isVisible = false
                    }
                }
                "image" -> { //Imagen del usuario
                    val context: Context = holder.ivUser.getContext() //Context ov ivPoster
                    if(!entry.value.toString().isEmpty()){
                        var imagen = entry.value.toString()
                        val context: Context = holder.ivUser.context
                        val id = context.resources.getIdentifier(
                            imagen.lowercase(),
                            "raw",
                            context.packageName
                        )
                        holder.ivUser.setImageResource(id)
                    } else {
                        val id = context.resources.getIdentifier(
                            "libra",
                            "raw",
                            context.packageName
                        )
                        holder.ivUser.setImageResource(id)
                    }
                }
                "followers" -> { //Seguidores
                    var lista = entry.value as ArrayList<String>
                    holder.tvFollowers.text = lista.size.toString()
                }
                "numerologyLevel" -> {
                    updateHeight(holder.vNumer, (entry.value.toString()).toInt())
                }
                "tarotLevel" -> {
                    updateHeight(holder.vTarot, (entry.value.toString()).toInt())
                }
                "astroLevel" -> {
                    updateHeight(holder.vAstro, (entry.value.toString()).toInt())
                }
                "magicLevel" -> {
                    updateHeight(holder.vMagia, (entry.value.toString()).toInt())
                }
            }

        }

        holder.itemView.setOnClickListener(holder)
    }

    /*
    * Método que sigue a un Usuario
    * @param    user
     */
    private fun followUser(user: String) {
        CoroutineScope(Dispatchers.Main).launch {
            db.collection("users").document(actualUser).get()
                .addOnSuccessListener {
                    var following = it.get("following") as ArrayList<String>
                    if (!following.contains(user)) {
                        following.add(user)
                    }
                    db.collection("users").document(actualUser).update(
                        hashMapOf(
                            "following" to following
                        ) as Map<String, ArrayList<String>>
                    )

                }
                .addOnFailureListener { exception ->
                    Log.i("LOGIA-ASTRO", "Error al actualizar usuario $actualUser en Perfiles: ", exception)
                }.await()
        }

        CoroutineScope(Dispatchers.Main).launch {
            db.collection("users").document(user).get()
                .addOnSuccessListener {
                    var followers = it.get("followers") as ArrayList<String>
                    if (!followers.contains(actualUser)) {
                        followers.add(actualUser)
                    }
                    db.collection("users").document(user).update(
                        hashMapOf(
                            "followers" to followers
                        ) as Map<String, ArrayList<String>>
                    )

                }
                .addOnFailureListener { exception ->
                    Log.i("LOGIA-ASTRO", "Error al actualizar usuario $user en Perfiles: ", exception)
                }.await()
        }
    }

    /*
    * Método actualiza altura de un View
    * @param    view, stat
     */
    private fun updateHeight(view: View, stat:Int){
        val params = view.layoutParams
        params.height = stat * 10
        view.layoutParams = params
    }

   /*
    * Método que devuelve tamaño de la lista
     */
    override fun getItemCount(): Int = users.size

    /*
    * RecyclerView Holder
    *   Inner class
    */
    inner class ViewHolder(binding: ItemUserBinding)
        : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        //Cargando datos del item_user
        val ivUser: ImageView = binding.ivUser
        val tvUsername: TextView = binding.tvUsername
        val tvFollowers: TextView = binding.tvFollowers
        val vNumer: View = binding.vNumer
        val vTarot: View = binding.vTarot
        val vAstro: View = binding.vAstro
        val vMagia: View = binding.vMagia
        val btnFollow: Button = binding.btnFollow

        /*
        * Método onClick de user
         */
        override fun onClick(v: View?) {
            val user = v?.tag as String
            listener?.onUserClick(user)
        }
    }
}
