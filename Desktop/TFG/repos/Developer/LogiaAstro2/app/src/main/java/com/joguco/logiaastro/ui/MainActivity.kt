package com.joguco.logiaastro.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.ActivityMainBinding
import com.joguco.logiaastro.login.LoginActivity
import com.joguco.logiaastro.menu.AccountActivity
import com.joguco.logiaastro.menu.SettingsActivity

class MainActivity : AppCompatActivity() {
    //Binding
    private lateinit var binding: ActivityMainBinding

    //Usuario
    private lateinit var username: String

    //Preferencias (Usuario logueado)
    private lateinit var sharedpreferences: SharedPreferences

    companion object{
        const val SHARED_PREFS = "shared_prefs"
        const val USER_KEY = "user_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Inflando layout
        setContentView(ActivityMainBinding.inflate(layoutInflater).also { binding = it }.root)

        //Conectando BottomNav
        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?)!!.navController

        NavigationUI.setupWithNavController(binding.bottomNavView, navController);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        //Recibimos User
        username = sharedpreferences.getString(USER_KEY, null).toString()
    }

    /*
    * Método que cambia el título de la barra
    */
    fun setActionBarTitle(title: String?) {
        this.title = title
    }

    /*
    * Método que devuelve el Usuario logueado
    */
    fun getUser():String {
        return username
    }

    /*
    * Método que crea el menú
    *@param     menu
    * @return   boolean
    */
    override fun onCreateOptionsMenu (menu: Menu?): Boolean {
        menuInflater.inflate (R.menu.settings, menu)
        return true
    }

    /*
    * Método que determina funcionalidad de cada opción del menú
    * @param    item
    * @return   boolean
    */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_account -> {
                val intent = Intent(this, AccountActivity::class. java).apply{
                    putExtra(USER_KEY, username)
                }
                startActivity(intent)

            }
            R.id.acion_settings -> {
                val intent = Intent(this, SettingsActivity::class. java)
                startActivity(intent)
            }
            R.id.action_logout -> {
                val editor = sharedpreferences.edit()
                editor.clear()
                editor.apply()

                val intent = Intent(this, LoginActivity::class. java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}