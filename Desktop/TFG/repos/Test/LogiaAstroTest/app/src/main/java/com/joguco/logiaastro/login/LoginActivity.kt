package com.joguco.logiaastro.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.ActivityLoginBinding
import com.joguco.logiaastro.login.register.RegisterActivity
import com.joguco.logiaastro.ui.InvitadoActivity
import com.joguco.logiaastro.ui.MainActivity
import com.joguco.logiaastro.viewmodel.UserViewModel

class LoginActivity : AppCompatActivity() {
    //Binding
    private lateinit var binding: ActivityLoginBinding

    //ViewModel
    private lateinit var userVM: UserViewModel

    //Firebase base de datos
    //private val db = Firebase.firestore

    //Atributos
    private lateinit var sharedpreferences: SharedPreferences

    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val USER_KEY = "user_key"
    }

    //Constructor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Iniciamos ViewModel
        userVM = ViewModelProvider(this)[UserViewModel::class.java]

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        initUI()
    }

    /*
    * Método que devuelve la base de datos de Firebase

    fun getFirebaseDB(): FirebaseFirestore {
        return db
    }*/

    /*
    * Método que inicia la UI
     */
    private fun initUI() {
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
                var acceso = userVM.userExists(binding.etUser.text.toString(), binding.etPass.text.toString())
                when(acceso){
                    1 -> { // SUCCESS
                        val editor = sharedpreferences.edit()
                        editor.putString(USER_KEY, binding.etUser.text.toString())
                        editor.apply()

                        val intent = Intent(this, MainActivity::class. java).apply{
                        }
                        startActivity(intent)
                    }
                    0 -> { // PWD incorrecta
                        binding.etPass.error = getText(R.string.login_contra_error)
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