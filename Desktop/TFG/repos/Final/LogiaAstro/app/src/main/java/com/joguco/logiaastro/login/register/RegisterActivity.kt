package com.joguco.logiaastro.login.register

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.ActivityRegisterBinding
import com.joguco.logiaastro.login.LoginActivity
import com.joguco.logiaastro.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegisterActivity : AppCompatActivity() {
    //Binding
    private lateinit var binding: ActivityRegisterBinding

    //ViewModel
    private lateinit var userVM: ViewModel

    //Firebase base de datos
    private val db = Firebase.firestore

    //Objetos compartidos
    private lateinit var spRegister: SharedPreferences

    //Constructor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spRegister = getSharedPreferences(LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE)

        //Inciamos ViewModel - Lo usaremos para validaciones
        userVM = ViewModelProvider(this)[ViewModel::class.java]
        initUI()
    }


    /*
    * Método inicia UI
    */
    private fun initUI() {
        //Al hacer click en FINALIZAR
        binding.btnFinalizar.setOnClickListener {

            //Comprobamos nombre de user primero
            CoroutineScope(Dispatchers.Main).launch{
                val querySnapshot = db.collection("users")
                    .get()
                    .await()
                if(querySnapshot.documents.toString().contains(binding.etUser.text.toString())){
                    binding.etUser.error = getText(R.string.validation_user)
                } else { //Si el nombre de usuario NO está repetido, continuamos con registro
                    registro()
                }
            }
        }
    }

    /*
    * Método genera un registro en Firebase con validaciones
    */
    private fun registro() {
        var dia = 1
        var mes = 1
        var year = 1994
        var cont = true

        if(binding.etDay.text!!.isNotEmpty() ){ //Comprobando el DIA
            dia = Integer.parseInt(binding.etDay.text.toString())
            if(dia > 31 || dia < 1){
                binding.etDay.error = getText(R.string.validation_day)
                cont = false
            }
        } else {
            binding.etDay.error = getText(R.string.obligado_day)
            cont = false
        }

        if(binding.etMonth.text!!.isNotEmpty()){ //Comprobando el MES
            mes = Integer.parseInt(binding.etMonth.text.toString())
            if(mes > 12 || mes < 1){
                binding.etMonth.error = getText(R.string.validation_month)
                cont = false
            }
        } else {
            binding.etMonth.error = getText(R.string.obligado_month)
            cont = false
        }

        if(binding.etYear.text!!.isNotEmpty()){ //Comprobando el AÑO
            year = Integer.parseInt(binding.etYear.text.toString())
            if(binding.etYear.text!!.length > 4){
                binding.etYear.error = getText(R.string.validation_year)
                cont = false
            }
        } else {
            binding.etYear.error = getText(R.string.obligado_year)
            cont = false
        }

        if(binding.etUser.text.toString().isEmpty() && binding.etUser.text!!.isEmpty()){ //Comprobando el USER
            binding.etUser.error = getText(R.string.obligado_user)
            cont = false
        } else if(!userVM.isValidName(binding.etUser.text.toString())){
            binding.etUser.error = getText(R.string.validation_user)
            cont = false
        }
        if(binding.etPwd.text!!.isEmpty()){ //Comprobando la PWD
            binding.etPwd.error = getText(R.string.obligado_pwd)
            cont = false
        } else if(binding.etPwd.text.toString().length < 8){
            binding.etPwd.error = getText(R.string.validation_pwd)
            cont = false
        }

        if(binding.etMail.text!!.isEmpty()){ //Comprobando el CORREO
            binding.etMail.error = getText(R.string.obligado_mail)
            cont = false
        }
        if(!userVM.isValidEmail(binding.etMail.text.toString())){
            binding.etMail.error = getText(R.string.validation_mail)
            cont = false
        }

        if(binding.etHour.text!!.isEmpty()){ //Comprobando la HORA
            binding.etHour.error = getText(R.string.obligado_hour)
            cont = false
        }
        if(!userVM.isValidHour(binding.etHour.text.toString())){
            binding.etHour.error = getText(R.string.validation_hour)
            cont = false
        }

        if(binding.etPlace.text!!.isEmpty()){ //Comprobando el LUGAR
            binding.etPlace.error = getText(R.string.obligado_place)
            cont = false
        } else if(!userVM.isValidPlace(binding.etPlace.text.toString())){
            binding.etPlace.error = getText(R.string.validation_place)
            cont = false
        }

        if(binding.etTimezone.text!!.isEmpty()){
            binding.etTimezone.error = getText(R.string.obligado_timezone)
            cont = false
        }

        if(cont){ //Si continuar es true guardamos usuario
            val pwd = userVM.encryptPwd(binding.etPwd.text.toString())
            CoroutineScope(Dispatchers.Main).launch{
                db.collection("users").get()
                    .addOnSuccessListener { documents ->
                        db.collection("users").document(binding.etUser.text.toString()).set(
                            hashMapOf(
                                "userId" to documents.size(),
                                "username" to binding.etUser.text.toString(),
                                "password" to pwd,
                                "mail" to binding.etMail.text.toString(),
                                "day" to dia,
                                "month" to mes,
                                "year" to year,
                                "hour" to binding.etHour.text.toString(),
                                "place" to binding.etPlace.text.toString(),
                                "timezone" to binding.etTimezone.text.toString().toDouble(),
                                "image" to "",
                                "tarotLevel" to 1,
                                "numerologyLevel" to 1,
                                "astroLevel" to 1,
                                "magicLevel" to 1,
                                "followers" to arrayListOf<Int>(),
                                "following" to arrayListOf<Int>(),
                                "chartList" to arrayListOf<String>(),
                                "compras" to arrayListOf<Int>(),
                                "post" to "",
                                "mensajes" to arrayListOf<String>()
                            )
                        )
                    }
                    .addOnFailureListener { exception ->
                        Log.i("LOGIA-ASTRO", "Error al guardar usuario: ", exception)
                    } .await()

                    goToLogin()
            }

    }
}
    /*
    * Método que navega al login
     */
    private fun goToLogin() {
        //Volvemos al login
        val intent = Intent(this, LoginActivity::class. java)
        startActivity(intent)
    }
}