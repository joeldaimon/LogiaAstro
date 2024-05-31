package com.joguco.logiaastro.tabs.Perfiles

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.ActivityPerfilesBinding
import com.joguco.logiaastro.interfaces.OnUserClick
import com.joguco.logiaastro.ui.MainActivity

class PerfilesActivity : AppCompatActivity(), OnUserClick {
    //Binding
    private lateinit var binding: ActivityPerfilesBinding

    //Layouts
    private val layoutList: FrameLayout by lazy { findViewById(R.id.containerListPerfiles) } //List fragment
    private val layoutDetail: FrameLayout? by lazy { findViewById(R.id.containerDetailPerfiles) } //Detail fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityPerfilesBinding.inflate(layoutInflater).also { binding = it }.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        loadRecyclerView()
        //Comprobamos si la vista está en Landscape
        if (isLandScape()){
            loadDetailFragment(true, "joel")
        }
    }

    fun updateMessage(list: ArrayList<String>, user: String){
        if(user!=null){
            val db = Firebase.firestore
            db.collection("users").document(user).update(
                hashMapOf(
                    "mensajes" to list
                ) as Map<String, ArrayList<String>>
            )
        }
    }

    /*
    * Método que carga el RecyclerView
     */
    private fun loadRecyclerView() {
        //Recibimos User
        var sharedpreferences = getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE)
        var username = sharedpreferences.getString(MainActivity.USER_KEY, null).toString()

        supportFragmentManager.beginTransaction()
            .replace(layoutList.id, PerfilesListFragment.newInstance(1, username))
            .addToBackStack(null)
            .commit()
    }

    /*
    * Método que comprueba si la vista es en landscape
    * @return   boolean
     */
    private fun isLandScape():Boolean {
        return layoutDetail != null
    }

    /*
    * Método que carga detalles del fragmento al hacer click
    * @param    user
     */
    override fun onUserClick(user: String) {
        loadDetailFragment(isLandScape(), user)
    }

    /*
    * Método que carga detalles
    * @param    twoPane, signId
     */
    private fun loadDetailFragment(twoPane:Boolean, user:String){
        val id = layoutDetail?.id?:layoutList.id

        supportFragmentManager.beginTransaction()
            .replace(id, PerfilesDetailFragment.newInstance(user))
            .addToBackStack(null)
            .commit()
    }


}