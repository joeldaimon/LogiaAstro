package com.joguco.logiaastro.login.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.ActivityRegisterBinding
import com.joguco.logiaastro.login.LoginActivity
import com.joguco.logiaastro.viewmodel.UserViewModel

class RegisterActivity : AppCompatActivity() {
    //Binding
    private lateinit var binding: ActivityRegisterBinding

    //ViewModel
    private lateinit var userVM: UserViewModel

    //Constructor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Inciamos ViewModel
        userVM = ViewModelProvider(this)[UserViewModel::class.java]
        initUI()
    }



    //Iniciamos la UI de Register
    private fun initUI() {
        //Al hacer click en FINALIZAR
        binding.btnFinalizar.setOnClickListener {
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
            }
            if(binding.etPwd.text!!.isEmpty()){ //Comprobando la PWD
                binding.etPwd.error = getText(R.string.obligado_pwd)
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
            }

            if(cont){ //Si continuar es true guardamos usuario
                //Guardamos USER en DB
                var user = userVM.add(binding.etUser.text.toString(), binding.etPwd.text.toString(), binding.etMail.text.toString(),
                    dia, mes, year, binding.etHour.text.toString(), binding.etPlace.text.toString())

                //Registramos user en Firebase
                //val database = LoginActivity().getFirebaseDB()
/*
                database.collection("users").document(user.username).set(
                    hashMapOf(
                        "userID" to user.id,
                        "username" to user.username,
                        "password" to user.password,
                        "mail" to user.mail,
                        "day" to user.day,
                        "month" to user.month,
                        "year" to user.year,
                        "hour" to user.hour,
                        "place" to user.place
                    )
                )


                database.collection("users").document(user.username).get().addOnSuccessListener {
                    binding.tvLoginDescription.text = "${it.get("username")}, ${it.get("mail")}, ${it.get("day") }"
                }*/



                //Volvemos al login
                //val intent = Intent(this, LoginActivity::class. java)
                //startActivity(intent)
            }

        }
    }
}