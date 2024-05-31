package com.joguco.logiaastro.mision

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.ActivityGuessBinding
import com.joguco.logiaastro.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GuessActivity : AppCompatActivity() {
    //Binding
    private lateinit var binding: ActivityGuessBinding

    //User
    private lateinit var user: String

    //Puntuación
    private var puntos = 0
    private var intentos = 0

    //Lista de iconos
    private var lista = mutableListOf(
        "icon_water", "icon_fire","icon_eye","icon_heart",
        "icon_water", "icon_fire","icon_eye","icon_heart",
        "icon_water", "icon_fire","icon_eye","icon_heart")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityGuessBinding.inflate(layoutInflater).also { binding = it }.root)

        var sharedpreferences = getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE)
        user = sharedpreferences.getString(MainActivity.USER_KEY, null).toString()

        lista.shuffle()
        showIcon(lista[0])
        initListeners()
    }

    /*
    * Iniciando Listeners
   */
    private fun initListeners() {
        binding.btnWater.setOnClickListener{
            reset(lista[0] == "icon_water")
        }
        binding.btnFire.setOnClickListener{
            reset(lista[0] == "icon_fire")
        }
        binding.btnEye.setOnClickListener{
            reset(lista[0] == "icon_eye")
        }
        binding.btnHeart.setOnClickListener{
            reset(lista[0] == "icon_heart")
        }

        binding.btnNext.setOnClickListener{
            binding.btnNext.isEnabled = false
            lista.shuffle()
            showIcon(lista[0])
            binding.btnGuess.iconTint = ContextCompat.getColorStateList(this, R.color.rosa_palo)
        }
    }

    /*
    * Método que resetea UI
    * @param    acierto
     */
    private fun reset(acierto: Boolean) {
        if(acierto){
            binding.tvRespuesta.text = getText(R.string.minijuego_acierto)
            puntos++
        } else {
            binding.tvRespuesta.text = getText(R.string.minijuego_error)
        }
        intentos++
        binding.btnNext.isEnabled = true
        binding.btnGuess.iconTint = ContextCompat.getColorStateList(this, R.color.white)
        comprobarPuntos()
    }

    /*
     * Método que comprueba puntos
    */
    private fun comprobarPuntos() {
        if(intentos >= 15){
            binding.tvRespuesta.text = "${getText(R.string.minijuego_fin_juego)} $puntos/$intentos"
            binding.btnNext.isEnabled = false
            if(puntos > 10){
                var db = Firebase.firestore
                CoroutineScope(Dispatchers.Main).launch{
                    db.collection("users").document(user).get()
                        .addOnSuccessListener {
                            var tarotLevel = it.get("tarotLevel") as Long
                            tarotLevel++
                            db.collection("users").document(user).update(
                                hashMapOf(
                                    "tarotLevel" to tarotLevel
                                ) as Map<String, Any>
                            )
                        }
                        .addOnFailureListener { exception ->
                            Log.i("LOGIA-ASTRO", "Error al actualizar usuario en BootCamp: ", exception)
                        } .await()
                }
            }
        }
    }

    /*
    * Método que muestra icono
    * @param    guess
     */
    private fun showIcon(guess: String) {
        when(guess){
            "icon_water" -> {
                binding.btnGuess.setIconResource(R.drawable.icon_water)
            }
            "icon_fire" -> {
                binding.btnGuess.setIconResource(R.drawable.icon_fire)
            }
            "icon_eye" -> {
                binding.btnGuess.setIconResource(R.drawable.icon_eye)
            }
            "icon_heart" -> {
                binding.btnGuess.setIconResource(R.drawable.icon_heart)
            }
        }
    }

}