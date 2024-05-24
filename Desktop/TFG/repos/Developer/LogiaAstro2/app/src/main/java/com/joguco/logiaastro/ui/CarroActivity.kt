package com.joguco.logiaastro.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.ActivityCarroBinding
import com.joguco.logiaastro.login.register.RegisterActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CarroActivity : AppCompatActivity() {
    //Binding
    private lateinit var binding: ActivityCarroBinding

    //Atributos
    private lateinit var compra: String
    private lateinit var username: String

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

        //Cargamos directamente la compra con datos de usuario
        var arr = compra.split(";")
        procesarServicio(arr[1], false)

        initListeners()
    }

    /*
    * Iniciando Listeners
     */
    private fun initListeners() {
        binding.btnMisDatos.setOnClickListener{
            var arr = compra.split(";")
            procesarServicio(arr[1], false)
        }
        binding.btnOtrosDatos.setOnClickListener{
            var arr = compra.split(";")
            procesarServicio(arr[1], true)
        }
        binding.btnFinalizar.setOnClickListener{
            var arr = compra.split(";")
            var video = false
            if(compra.contains("Vídeo")){
                video = true
            }

            val db = Firebase.firestore
            var compraId = 0

            //Cargamos compra a DB
            CoroutineScope(Dispatchers.Main).launch{
                db.collection("compras").get()
                    .addOnSuccessListener { documents ->
                        compraId = documents.size()
                        db.collection("compras").document(documents.size().toString()).set(
                            hashMapOf(
                                "username" to username,
                                "video" to video,
                                "categoria" to arr[1],
                                "compra" to arr[0],
                                "nombre" to binding.etNombre.text.toString(),
                                "fecha" to binding.etFecha.text.toString(),
                                "hora" to binding.etHora.text.toString(),
                                "lugar" to binding.etLugar.text.toString(),
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

    /*
    * Método que procesa un servicio enviado por el usuario
    * @param    split, otros
     */
    private fun procesarServicio(split: String, otros: Boolean) {
        if(!otros){
            binding.tvTitle.text = getText(R.string.util_account)
        } else {
            binding.tvTitle.text = getText(R.string.util_otros_datos)
        }
        when(split){
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
                    binding.llHora.visibility = View.VISIBLE
                    binding.llLugar.visibility = View.VISIBLE
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