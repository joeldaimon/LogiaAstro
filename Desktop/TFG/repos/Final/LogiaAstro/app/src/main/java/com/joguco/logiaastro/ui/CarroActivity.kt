package com.joguco.logiaastro.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.ActivityCarroBinding
import com.joguco.logiaastro.login.register.RegisterActivity
import com.joguco.logiaastro.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CarroActivity : AppCompatActivity() {
    //Binding
    private lateinit var binding: ActivityCarroBinding

    //Atributos
    private val db = Firebase.firestore
    private lateinit var compra: String
    private lateinit var username: String
    private lateinit var userVM: ViewModel
    private var otros = false
    private var categoria = "general"
    private lateinit var fecha: String
    private lateinit var hora: String
    private lateinit var lugar: String

    companion object{
        const val KEY_EXTRA_CARRO = "KEY_EXTRA_CARRO"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Inflando layout
        setContentView(ActivityCarroBinding.inflate(layoutInflater).also { binding = it }.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //Recibimos String que nos indica que tipo de compra es
        compra = intent?.getStringExtra(KEY_EXTRA_CARRO)!!

        //Cargamos nombre de usuario logueado
        var sharedpreferences = getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE)
        username = sharedpreferences.getString(MainActivity.USER_KEY, null).toString()

        //Inciamos ViewModel - Lo usaremos para validaciones
        userVM = ViewModelProvider(this)[ViewModel::class.java]

        //Cargamos directamente la compra con datos de usuario
        var arr = compra.split(";")
        categoria = arr[1]
        procesarServicio()

        initListeners()
    }

    /*
    * Iniciando Listeners
     */
    private fun initListeners() {
        binding.btnMisDatos.setOnClickListener{
            procesarServicio()
            otros = false
        }
        binding.btnOtrosDatos.setOnClickListener{
            procesarServicio()
            otros = true
        }
        binding.btnFinalizar.setOnClickListener{
            var video = false
            if(compra.contains("Vídeo")){
                video = true
            }

            var compraId = 0

            if(validarCompra()){
                var arr = compra.split(";")
                //Cargamos compra a DB
                CoroutineScope(Dispatchers.Main).launch{
                    db.collection("compras").get()
                        .addOnSuccessListener { documents ->
                            compraId = documents.size()
                            db.collection("compras").document(documents.size().toString()).set(
                                hashMapOf(
                                    "username" to username,
                                    "video" to video,
                                    "categoria" to categoria,
                                    "compra" to arr[0],
                                    "nombre" to binding.etNombre.text.toString(),
                                    "fecha" to if(binding.etFecha.text.toString().isEmpty()) fecha else binding.etFecha.text.toString(),
                                    "hora" to if(binding.etHora.text.toString().isEmpty()) hora else binding.etHora.text.toString(),
                                    "lugar" to if(binding.etLugar.text.toString().isEmpty()) lugar else binding.etLugar.text.toString(),
                                    "tema" to binding.etTema.text.toString(),
                                    "pregunta" to binding.etPregunta.text.toString()
                                )
                            )
                        }
                        .addOnFailureListener { exception ->
                            Log.i("LOGIA-ASTRO", "Error guardando compra: ", exception)
                        } .await()


                    db.collection("users").document(username).get()
                        .addOnSuccessListener {
                            var lista = it.get("compras") as ArrayList<Int>
                            lista.add(compraId)
                            //Actualizamos usuario con id de compra
                            db.collection("users").document(username).update(
                                hashMapOf(
                                    "compras" to lista
                                ) as Map<String, ArrayList<Int>>
                            )
                        }
                        .addOnFailureListener { exception ->
                            Log.i("LOGIA-ASTRO", "Error al actualizar usuario: ", exception)
                        } .await()

                    binding.cvDatos.isVisible = false
                    binding.llButtons.isVisible = false
                    binding.tvCompra.text = getText(R.string.util_compra_end).toString()
                }
            }
        }
    }

    /*
    * Método que valida la compra
    * @return   Boolean
     */
    private fun validarCompra(): Boolean {
        var cont = true
        if(otros){
            if(!userVM.isValidName(binding.etNombre.text.toString()) || binding.etNombre.text!!.isEmpty()){
                binding.etNombre.error = getText(R.string.validation_name)
                cont = false
            }
            when(categoria){
                //Nombre
                "general" -> {
                    if(binding.etTema.text!!.isEmpty()){
                        binding.etTema.error = getText(R.string.validation_tema)
                        cont = false
                    }

                    if(binding.etPregunta.text!!.isEmpty()){
                        binding.etPregunta.error = getText(R.string.validation_pregunta)
                        cont = false
                    }
                }
                "astrology" -> {
                    // timezone TEMA
                    if(!userVM.isValidDate(binding.etFecha.text.toString()) || binding.etFecha.text!!.isEmpty()){
                        binding.etFecha.error = getText(R.string.account_date_error)
                        cont = false
                    }

                    if(!userVM.isValidHour(binding.etHora.text.toString()) || binding.etHora.text!!.isEmpty()){
                        binding.etHora.error = getText(R.string.validation_hour)
                        cont = false
                    }

                    if(!userVM.isValidName(binding.etLugar.text.toString()) || binding.etLugar.text!!.isEmpty()){
                        binding.etLugar.error = getText(R.string.validation_place)
                        cont = false
                    }
                }
                "numerology" -> {
                    if(!userVM.isValidDate(binding.etFecha.text.toString()) || binding.etFecha.text!!.isEmpty()){
                        binding.etFecha.error = getText(R.string.account_date_error)
                        cont = false
                    }
                }
            }

        } else {
            db.collection("users").document(username).get()
                .addOnSuccessListener {
                    var dia = it.get("day") as Long
                    var mes = it.get("month") as Long
                    var year = it.get("year") as Long
                    fecha = "$dia/$mes/$year"
                    hora = it.get("hour") as String
                    lugar = it.get("place") as String
                }
                .addOnFailureListener { exception ->
                    Log.i("LOGIA-ASTRO", "Error al cargar datos de usuario: ", exception)
                }

            when(categoria){
                "general" -> {
                    if(binding.etTema.text!!.isEmpty()){
                        binding.etTema.error = getText(R.string.validation_tema)
                        cont = false
                    }

                    if(binding.etPregunta.text!!.isEmpty()){
                        binding.etPregunta.error = getText(R.string.validation_pregunta)
                        cont = false
                    }
                }
                "astrology" -> {
                    if(binding.etTema.text!!.isEmpty()){
                        binding.etTema.error = getText(R.string.validation_tema)
                        cont = false
                    }
                }
                "soulcontract" -> {
                    if(!userVM.isValidName(binding.etNombre.text.toString()) || binding.etNombre.text!!.isEmpty()){
                        binding.etNombre.error = getText(R.string.validation_name)
                        cont = false
                    }
                }
            }
        }

        return cont
    }

    /*
    * Método que procesa un servicio enviado por el usuario
    * @param    split, otros
     */
    private fun procesarServicio() {
        if(!otros){
            binding.tvTitle.text = getText(R.string.util_account)
        } else {
            binding.tvTitle.text = getText(R.string.util_otros_datos)
        }
        when(categoria){
            "general" -> {
                binding.llFecha.visibility = View.GONE
                binding.llHora.visibility = View.GONE
                binding.llLugar.visibility = View.GONE
                binding.tvDescription.visibility = View.VISIBLE

                binding.llPregunta.visibility = View.VISIBLE
                binding.llTema.visibility = View.VISIBLE
            }
            "astrology" -> {
                binding.llPregunta.visibility = View.GONE

                if(!otros){
                    binding.llFecha.visibility = View.GONE
                    binding.llHora.visibility = View.GONE
                    binding.llLugar.visibility = View.GONE
                } else {
                    binding.llFecha.visibility = View.VISIBLE
                }
            }
            "numerology" -> {
                binding.llTema.visibility = View.GONE
                binding.llPregunta.visibility = View.GONE
                binding.tvDescription.visibility = View.VISIBLE
                if(!otros){
                    binding.llFecha.visibility = View.GONE
                    binding.llHora.visibility = View.GONE
                    binding.llLugar.visibility = View.GONE
                } else {
                    binding.llFecha.visibility = View.VISIBLE
                    binding.llHora.visibility = View.VISIBLE
                    binding.llLugar.visibility = View.VISIBLE
                }

            }
            "soulcontract" -> {
                binding.llFecha.visibility = View.GONE
                binding.llHora.visibility = View.GONE
                binding.llLugar.visibility = View.GONE
                binding.llTema.visibility = View.GONE
                binding.llPregunta.visibility = View.GONE
                binding.tvDescription.visibility = View.VISIBLE
            }
        }
    }


}