package com.joguco.logiaastro.ui.Perfiles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.joguco.logiaastro.database.entities.UserEntity
import com.joguco.logiaastro.databinding.ItemUserBinding
import com.joguco.logiaastro.interfaces.OnUserClick

class PerfilesRecyclerView(
    private val users: MutableList<UserEntity>,
    private val listener: OnUserClick?
) : RecyclerView.Adapter<PerfilesRecyclerView.ViewHolder>() {


    //Creates a ViewHolder for every item of the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    //Function that customizes the data depending the position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Changing data
        val user = users[position]
        holder.tvUsername.text = user.username //Name of Serie

        //Image convertion
        val context: Context = holder.ivUser.getContext() //Context ov ivPoster
        val imageName = user.image.split(".")[0] //Splitting image name by dot
        val id = context.resources.getIdentifier(
            imageName,
            "raw",
            context.packageName
        )
        //Setting the image by id
        holder.ivUser.setImageResource(id)

        holder.tvFollowers.text = user.followers.size.toString()
        updateHeight(holder.vNumer, user.numerologyLevel)
        updateHeight(holder.vTarot, user.tarotLevel)
        updateHeight(holder.vAstro, user.astroLevel)
        updateHeight(holder.vMagia, user.magicLevel)

        holder.itemView.setOnClickListener(holder) //Our ViewHolder implements OnClickListener
    }

    private fun updateHeight(view: View, stat:Int){
        val params = view.layoutParams
        if(stat > 1){
            params.height = stat * 10
        }
        view.layoutParams = params
    }

    //Function that returns the Size of the serieList
    override fun getItemCount(): Int = users.size

    /*
    * RecyclerView Holder
    *   Inner class
    */
    inner class ViewHolder(binding: ItemUserBinding)
        : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {
        //Loading view of each item on list_item.xml
        val ivUser: ImageView = binding.ivUser
        val tvUsername: TextView = binding.tvUsername
        val tvFollowers: TextView = binding.tvFollowers
        val vNumer: View = binding.vNumer
        val vTarot: View = binding.vTarot
        val vAstro: View = binding.vAstro
        val vMagia: View = binding.vMagia

        //Function that does something when user clicks on an Item
        override fun onClick(v: View?) {
            //Sign by tag
            val user = v?.tag as UserEntity

            //Listener
            listener?.onUserClick(user)
        }
    }
}
