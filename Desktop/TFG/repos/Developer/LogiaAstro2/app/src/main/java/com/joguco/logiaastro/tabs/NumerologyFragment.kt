package com.joguco.logiaastro.tabs

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.FragmentNumerologyBinding
import com.joguco.logiaastro.tabs.mapa.MapaActivity
import com.joguco.logiaastro.tabs.mapa.MapaActivity.Companion.KEY_EXTRA_MAPA
import com.joguco.logiaastro.ui.MainActivity
import com.joguco.logiaastro.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Math.abs
import java.time.LocalDateTime

/**
 * NUMEROLOGÍA
 */
class NumerologyFragment : Fragment() {
    //Binding
    private lateinit var binding: FragmentNumerologyBinding

    //Hora y fecha actual
    private lateinit var now: LocalDateTime

    //Nombre de usuario
    private lateinit var user: String

    //Atributos
    private var day = 1
    private var month = 1
    private var year = 1992
    private lateinit var mapa: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflando el layout
        return FragmentNumerologyBinding.inflate(inflater, container, false).also { binding = it}.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Título de la barra
        (activity as MainActivity?)?.setActionBarTitle("Numerología")
        //Cogiendo Usuario logueado desde Main

        user = (activity as MainActivity?)?.getUser()!!

        //Iiciando variables y métodos necesarios
        now = LocalDateTime.now()
        initListeners()

        if (user != null) {
            initUser()
        }
    }


    /*
    * Método que inicia Listeners
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListeners() {
        binding.btnLoad.setOnClickListener{
            var view = this.layoutInflater.inflate(R.layout.dialog_numerology, null)
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder
                .setTitle(getText(R.string.numerology_load_title))
                .setView(view)
                .setPositiveButton(getText(R.string.util_aceptar)) { dialog, which ->
                    var fecha: EditText = view.findViewById(R.id.etFecha)
                    var userVM = ViewModelProvider(this)[ViewModel::class.java]
                    if(!userVM.isValidDate(fecha.text.toString())){
                        Toast.makeText(requireContext(), R.string.account_date_error, Toast.LENGTH_LONG).show()
                    } else {
                        var arr = fecha.text.toString().split("/")
                        day = arr[0].toInt()
                        month = arr[1].toInt()
                        year = arr[2].toInt()
                        initNumerology()
                        var db = Firebase.firestore
                        CoroutineScope(Dispatchers.Main).launch{
                            db.collection("users").document(user).get()
                                .addOnSuccessListener {
                                    var numerologyLevel = it.get("numerologyLevel") as Long
                                    numerologyLevel++
                                    db.collection("users").document(user).update(
                                        hashMapOf(
                                            "numerologyLevel" to numerologyLevel
                                        ) as Map<String, Int>
                                    )
                                }
                                .addOnFailureListener { exception ->
                                    Log.i("LOGIA-ASTRO", "Error al actualizar usuario en Numerología: ", exception)
                                } .await()
                        }
                        Toast.makeText(requireContext(), R.string.numerololgy_loaded, Toast.LENGTH_LONG).show()
                    }


                }
                .setNegativeButton(getText(R.string.util_cancelar)) { dialog, which ->
                    Toast.makeText(requireContext(), R.string.util_cancel_action, Toast.LENGTH_LONG).show()
                }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        binding.btnMapa.setOnClickListener{
            activity?.let{
                val intent = Intent (it, MapaActivity::class.java).apply{
                    putExtra(KEY_EXTRA_MAPA, mapa)
                }
                it.startActivity(intent)
            }
        }

        binding.btnRealizaciones.setOnClickListener{
            //Layouts
            binding.lyMochila.isVisible = false
            binding.lyRealizaciones.isVisible = true
            binding.lyDesafios.isVisible = false
            binding.lyInconsciente.isVisible = false
            binding.lyPilares.isVisible = false

            //Cambiamos titulo
            binding.tvTitle.text = getText(R.string.numerology_realizacion_title)
        }

        binding.btnDesafios.setOnClickListener{
            binding.lyMochila.isVisible = false
            binding.lyRealizaciones.isVisible = false
            binding.lyDesafios.isVisible = true
            binding.lyInconsciente.isVisible = false
            binding.lyPilares.isVisible = false

            //Cambiamos titulo
            binding.tvTitle.text = getText(R.string.numerology_desafio_title)
        }

        binding.btnMochila.setOnClickListener{
            binding.lyMochila.isVisible = true
            binding.lyRealizaciones.isVisible = false
            binding.lyDesafios.isVisible = false
            binding.lyInconsciente.isVisible = false
            binding.lyPilares.isVisible = false

            //Cambiamos titulo
            binding.tvTitle.text = getText(R.string.numerology_mochila)
        }

        binding.btnInconsciente.setOnClickListener{
            binding.lyMochila.isVisible = false
            binding.lyRealizaciones.isVisible = false
            binding.lyDesafios.isVisible = false
            binding.lyInconsciente.isVisible = true
            binding.lyPilares.isVisible = false

            //Cambiamos titulo
            binding.tvTitle.text = getText(R.string.numerology_inconsciente)
        }

        binding.btnPilares.setOnClickListener{
            binding.lyMochila.isVisible = false
            binding.lyRealizaciones.isVisible = false
            binding.lyDesafios.isVisible = false
            binding.lyInconsciente.isVisible = false
            binding.lyPilares.isVisible = true


            //Cambiamos titulo
            binding.tvTitle.text = getText(R.string.numerology_pilares)
        }
    }


    /*
    * Método que inicia usuario
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initUser(){
        var db = Firebase.firestore
        CoroutineScope(Dispatchers.Main).launch{
            db.collection("users").document(user).get()
                .addOnSuccessListener { user ->
                    day = (user.get("day").toString()).toInt()
                    month = (user.get("month").toString()).toInt()
                    year = (user.get("year").toString()).toInt()
                }
                .addOnFailureListener { exception ->
                    Log.i("LOGIA-ASTRO", "Error cargando numerología: ", exception)
                } .await()

            initNumerology()
        }
    }
    /*
    * Método que inicia datos numerológicos
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initNumerology() {
        //Energias generales
        binding.tvDia.text = reducir(reducir(now.dayOfMonth) + reducir(now.monthValue) + reducir(now.year)).toString()
        binding.tvMes.text = "${reducir(reducir(now.monthValue) + reducir(now.year))}"

        //Mochila
        var karma = reducir(month)
        binding.tvKarma.text = "${getText(R.string.numerology_karma)}: $karma"
        var personal = reducir(day)
        binding.tvPersonal.text = "${getText(R.string.numerology_pers)}: $personal"

        //Año
        var vidaPasada = 0
        var yearNum = procesarCeros(year)
        if(yearNum != 0){
            var num1 = 0
            var num2 = 0
            when(yearNum.toString().length){
                1 ->{
                    vidaPasada = yearNum
                }
                2 -> {
                    var num1 = (year / 100) / 10
                    vidaPasada = reducir(reducir(num1))
                } else -> {
                    num1 = (year / 100) / 10
                    num2 = year - (num1*100)
                    vidaPasada = reducir(reducir(num1)+reducir(num2))
                }
            }
        }

        binding.tvPasado.text = "${getText(R.string.numerology_vida_pasada)}: $vidaPasada"
        var ascendente = reducir(reducir(day)+reducir(month)+vidaPasada)
        binding.tvAsc.text = "${getText(R.string.numerology_ascendente)}: $ascendente"

        //Día personal del usuario
        binding.tvTuDia.text = reducir(ascendente + reducir(reducir(now.dayOfMonth) + reducir(now.monthValue) + reducir(now.year))).toString()

        //Realizaciones
        var real1 = reducir(karma + personal)
        binding.tvReal1.text = "1º ${getText(R.string.numerology_realizacion)}: $real1"
        var real2 = reducir(personal + vidaPasada)
        binding.tvReal2.text = "2º ${getText(R.string.numerology_realizacion)}: $real2"
        var real3 = reducir((karma + personal) + (personal + vidaPasada))
        binding.tvReal3.text = "3º ${getText(R.string.numerology_realizacion)}: $real3"
        var real4 = reducir(karma + vidaPasada)
        binding.tvReal4.text = "4º ${getText(R.string.numerology_realizacion)}: $real4"


        //Desafios
        var des1 = reducir(karma - personal)
        binding.tvDesafio1.text = "1º ${getText(R.string.numerology_desafio)}: $des1"
        var des2 = reducir(personal - vidaPasada)
        binding.tvDesafio2.text = "2º ${getText(R.string.numerology_desafio)}: $des2"
        var des3 = reducir((karma - personal) + (personal - vidaPasada))
        binding.tvDesafio3.text = "3º ${getText(R.string.numerology_desafio)}: $des3"
        var des4 = reducir(karma - vidaPasada)
        binding.tvDesafio4.text = "4º ${getText(R.string.numerology_desafio)}: $des4"

        mapa = arrayListOf(karma, personal, vidaPasada, ascendente, real1, real2, real3, real4, des1, des2, des3, des4)

        //Inconsciente
        var herencia = reducir(Math.abs(reducir(karma - personal)))+(abs(reducir((karma - personal)) + Math.abs((personal - vidaPasada))))
        binding.tvHerencia.text = "${getText(R.string.numerology_herencia)}:  $herencia"
        var consciente = reducir(herencia - abs(reducir(karma - vidaPasada)))
        binding.tvConsciente.text = "${getText(R.string.numerology_consciente)}: $consciente"
        binding.tvLatente.text = "${getText(R.string.numerology_latente)}: ${(reducir(herencia+consciente))}"

        //Pilares
        binding.tvPareja.text = "${getText(R.string.numerology_pareja)}: ${reducir( reducir(month)+ reducir(now.year))}"
        binding.tvReto.text = "${getText(R.string.numerology_reto)}: ${reducir( reducir(day)+ reducir(now.year))}"
        binding.tvSocial.text = "${getText(R.string.numerology_social)}: ${reducir( reducir(year)+ reducir(now.year))}"
        binding.tvTrabajo.text = "${getText(R.string.numerology_trabajo)}: ${reducir( ascendente+ reducir(now.year))}"
    }

    /*
    * Método que procesa ceros del AÑO
    * @param    year
    * @return   int
     */
    private fun procesarCeros(year: Int): Int {
        var str = ""
        for(char in year.toString()){
            if(char != '0'){
                str += char
            }
        }

        return if(str=="") 0 else str.toInt()
    }

    /*
    * Método que reduce un número a una cifra
    * @param    num
    * @return   int
     */
    private fun reducir(num: Int): Int{
        var start = abs(num)
        var final = abs(num)
        if(start > 9 && !isMaestro(start)){
            final = 0
            for(decimal in start.toString()){
                final += decimal.digitToInt()
            }

            if(final > 9 && !isMaestro(final)) //Comprobar si NO es número maestro
                final = reducir(final)
        }
        return final
    }

    /*
    * Método que comprueba si un número es maestro
    * @param    num
    * @return   Boolean
     */
    private fun isMaestro(num: Int): Boolean {
        return num==11 || num==22 || num==33 || num==44
    }
}