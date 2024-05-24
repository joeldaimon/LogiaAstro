package com.joguco.logiaastro.mision

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.ActivityVelasBinding
import com.joguco.logiaastro.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class VelasActivity : AppCompatActivity() {
    //Binding
    private lateinit var binding: ActivityVelasBinding

    //User
    private lateinit var user: String

    //Array de velas
    private lateinit var velas: MutableList<CharSequence>

    //Contador
    private var cont = 0
    private var puntos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityVelasBinding.inflate(layoutInflater).also { binding = it }.root)

        var sharedpreferences = getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE)
        user = sharedpreferences.getString(MainActivity.USER_KEY, null).toString()

        velas = mutableListOf(
            getText(R.string.minijuego_velas_amor), getText(R.string.minijuego_velas_salud), getText(R.string.minijuego_velas_pasion),
            getText(R.string.minijuego_velas_obsesion), getText(R.string.minijuego_velas_intuicion), getText(R.string.minijuego_velas_juicios),
            getText(R.string.minijuego_velas_abundancia), getText(R.string.minijuego_velas_limpieza), getText(R.string.minijuego_velas_serenidad),
            getText(R.string.minijuego_velas_espiritual), getText(R.string.minijuego_velas_positivismo), getText(R.string.minijuego_velas_exito)
        )

        velas.shuffle()
        binding.tvVela.text = velas[cont]
        initListeners()
    }

    /*
    * Iniciando Listeners
     */
    private fun initListeners() {
        binding.velaRoja.setOnClickListener{
            compruebaVela("pasión")
        }
        binding.velaAmarilla.setOnClickListener{
            compruebaVela("abundancia")
        }
        binding.velaAzul.setOnClickListener{
            compruebaVela("espiritual")
        }
        binding.velaFucsia.setOnClickListener{
            compruebaVela("obsesión")
        }
        binding.velaVerde.setOnClickListener{
            compruebaVela("salud")
        }
        binding.velaRosa.setOnClickListener{
            compruebaVela("amor")
        }
        binding.velaMorada.setOnClickListener{
            compruebaVela("intuición")
        }
        binding.velaBlanca.setOnClickListener{
            compruebaVela("positivismo")
        }
        binding.velaNegra.setOnClickListener{
            compruebaVela("limpieza")
        }
        binding.velaMarron.setOnClickListener{
            compruebaVela("juicios")
        }
        binding.velaMarino.setOnClickListener{
            compruebaVela("serenidad")
        }
        binding.velaNaranja.setOnClickListener{
            compruebaVela("éxito")
        }
        binding.btnNext.setOnClickListener{
            binding.tvVela.text = velas[cont]
            binding.btnNext.isEnabled = false
        }
    }

    /*
    * Método que comprueba la respuesta
    * @param    String
     */
    private fun compruebaVela(ritual: String) {
        if(velas[cont].toString().contains(ritual.uppercase())){
            binding.tvVela.text = getText(R.string.minijuego_acierto)
            puntos++
        } else {
            binding.tvVela.text = getText(R.string.minijuego_error)
        }
        if(cont == 11){
            binding.tvVela.text = "${getText(R.string.minijuego_fin_juego)} $puntos"
            binding.btnNext.isEnabled = false
            compobarPuntos()
        } else {
            cont++
            binding.btnNext.isEnabled = true
        }
    }

    /*
    * Método que comprueba Puntos del usuario
     */
    private fun compobarPuntos() {
        if(puntos > 10){
            var db = Firebase.firestore
            CoroutineScope(Dispatchers.Main).launch{
                db.collection("users").document(user).get()
                    .addOnSuccessListener {
                        var magicLevel = it.get("magicLevel") as Long
                        magicLevel++
                        db.collection("users").document(user).update(
                            hashMapOf(
                                "magicLevel" to magicLevel
                            ) as Map<String, Any>
                        )
                    }
                    .addOnFailureListener { exception ->
                        Log.i("LOGIA-ASTRO", "Error al actualizar usuario en Velas: ", exception)
                    } .await()
            }
        }
    }
}