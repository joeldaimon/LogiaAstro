package com.joguco.logiaastro.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.joguco.logiaastro.R
import com.joguco.logiaastro.database.entities.UserEntity
import com.joguco.logiaastro.databinding.ActivityMainBinding
import com.joguco.logiaastro.login.LoginActivity
import com.joguco.logiaastro.menu.AccountActivity
import com.joguco.logiaastro.model.NatalChart
import com.joguco.logiaastro.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //ViewModel
    private lateinit var userVM: UserViewModel

    //Usuario
    private lateinit var user: UserEntity

    private lateinit var sharedpreferences: SharedPreferences

    companion object{
        const val KEY_EXTRA_USER = "KEY_EXTRA_USER"
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

        //Iniciamos VM
        userVM = ViewModelProvider(this)[UserViewModel::class.java]

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        //Recibimos User
        var username = sharedpreferences.getString(USER_KEY, null)
        user = username?.let { userVM.getByName(it) }!!
    }



    //Metodo que cambia el título de la barra
    fun setActionBarTitle(title: String?) {
        this.title = title
    }

    //Devuelve el Usuario logueado
    fun getUser():UserEntity {
        return user
    }

    //Creación de menú
    override fun onCreateOptionsMenu (menu: Menu?): Boolean {
        menuInflater.inflate (R.menu.settings, menu)
        return true
    }

    //Opciones del menú y acciones
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_account -> {
                val intent = Intent(this, AccountActivity::class. java).apply{
                    putExtra(KEY_EXTRA_USER, user.username)
                }
                startActivity(intent)

            }
            R.id.action_logout -> {
                val editor = sharedpreferences.edit()
                editor.clear()
                editor.apply()

                val intent = Intent(this, LoginActivity::class. java)
                startActivity(intent)
            }
            R.id.action_settings -> {
                //Settings
            }
        }
        return super.onOptionsItemSelected(item)
    }
}