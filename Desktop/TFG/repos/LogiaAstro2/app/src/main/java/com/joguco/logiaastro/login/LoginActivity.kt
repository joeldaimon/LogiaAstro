package com.joguco.logiaastro.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.ActivityLoginBinding
import com.joguco.logiaastro.login.register.RegisterActivity
import com.joguco.logiaastro.menu.SettingsActivity
import com.joguco.logiaastro.menu.dataStore
import com.joguco.logiaastro.ui.InvitadoActivity
import com.joguco.logiaastro.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    //Binding
    private lateinit var binding: ActivityLoginBinding

    //Firebase base de datos
    private val db = Firebase.firestore

    //Preferencias compartidas
    private lateinit var sharedpreferences: SharedPreferences

    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val USER_KEY = "user_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        CoroutineScope(Dispatchers.IO).launch{
            getSettings().collect{darkMode ->
                if(darkMode != null){
                    runOnUiThread{
                        enableDarkMode()
                    }
                }
            }
        }

        initListeners()
    }

    /*
    * Método que enciende el modo oscuro
     */
    private fun enableDarkMode(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        delegate.applyDayNight()
    }

    /*
     * Método que carga Settings
     * @return  Flow<Boolean?>
      */
    private fun getSettings(): Flow<Boolean?> {
        return dataStore.data.map{ preferences->
            preferences[booleanPreferencesKey(SettingsActivity.KEY_DARKMODE)] ?: false
        }
    }

    /*
    * Método inicia Listeners
    */
    private fun initListeners() {
        //Navegamos a REGISTER
        binding.tvRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class. java)
            startActivity(intent)
        }

        //Entramos como invitado
        binding.tvInvitado.setOnClickListener{
            val intent = Intent(this, InvitadoActivity::class. java)
            startActivity(intent)
        }

        //Al hacer click en Continuar
        binding.btnCont.setOnClickListener {
            try {
                //Agarramos user de Firebase
                db.collection("users").document(binding.etUser.text.toString()).get().addOnSuccessListener {
                    if(it.get("username") == binding.etUser.text.toString()){
                        if(it.get("password") == binding.etPass.text.toString()){
                            val editor = sharedpreferences.edit()
                            editor.putString(USER_KEY, binding.etUser.text.toString())
                            editor.apply()

                            val intent = Intent(this, MainActivity::class. java)
                            startActivity(intent)
                        } else {
                            binding.etPass.error = getText(R.string.login_contra_error)
                        }
                    } else {
                        binding.etUser.error = getText(R.string.login_login_error)
                    }
                }
            }catch(e: Exception){
                if(binding.etUser.text?.isEmpty() == true){ //Si USER está vacio
                    binding.etUser.error = getText(R.string.login_null_error)
                } else{ //Si no lo está - User erroneo
                    binding.etUser.error = getText(R.string.login_login_error)
                }
                if(binding.etPass.text?.isEmpty() == true){ //Si PWD está vacío
                    binding.etPass.error = getText(R.string.login_null_error)
                }
            }

        }
    }
}