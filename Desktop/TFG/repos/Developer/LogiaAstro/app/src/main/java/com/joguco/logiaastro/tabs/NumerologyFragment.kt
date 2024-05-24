package com.joguco.logiaastro.tabs

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.joguco.logiaastro.R
import com.joguco.logiaastro.database.entities.UserEntity
import com.joguco.logiaastro.databinding.FragmentNumerologyBinding
import com.joguco.logiaastro.tabs.mapa.MapaActivity
import com.joguco.logiaastro.tabs.mapa.MapaActivity.Companion.KEY_EXTRA_MAPA
import com.joguco.logiaastro.ui.ComprasActivity
import com.joguco.logiaastro.ui.MainActivity
import java.time.LocalDateTime

/**
 * NUMEROLOGÍA
 */
class NumerologyFragment : Fragment() {
    //Binding
    private lateinit var binding: FragmentNumerologyBinding

    //Atributos
    private lateinit var now: LocalDateTime
    private lateinit var user: UserEntity
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
            initNumerology()
        }
    }


    /*
    * Función que inicia Listeners
     */
    private fun initListeners() {
        binding.btnCompra.setOnClickListener{
            activity?.let{
                val intent = Intent (it, ComprasActivity::class.java).apply{
                    putExtra(ComprasActivity.KEY_EXTRA_COMPRA, "numerology")
                }
                it.startActivity(intent)
            }
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
    * Función que inicia datos numerológicos
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initNumerology() {
        //Energias generales
        binding.tvDia.text = reducir(reducir(now.dayOfMonth) + reducir(now.monthValue) + reducir(now.year)).toString()
        binding.tvMes.text = "${reducir(reducir(now.monthValue) + reducir(now.year))}"

        //Mochila
        var karma = reducir(user.month)
        binding.tvKarma.text = "${getText(R.string.numerology_karma)}: $karma"
        var personal = reducir(user.day)
        binding.tvPersonal.text = "${getText(R.string.numerology_pers)}: $personal"
        var num1: Int = (user.year / 100) / 10
        var num2: Int = user.year - (num1*100)
        var vidaPasada = reducir(reducir(num1)+reducir(num2))
        binding.tvPasado.text = "${getText(R.string.numerology_vida_pasada)}: $vidaPasada"
        var ascendente = reducir(reducir(user.day)+reducir(user.month)+vidaPasada)
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
        var herencia = reducir(Math.abs(reducir(karma - personal)))+(Math.abs(reducir((karma - personal)) + Math.abs((personal - vidaPasada))))
        binding.tvHerencia.text = "${getText(R.string.numerology_herencia)}:  $herencia"
        var consciente = reducir(herencia - Math.abs(reducir(karma - vidaPasada)))
        binding.tvConsciente.text = "${getText(R.string.numerology_consciente)}: $consciente"
        binding.tvLatente.text = "${getText(R.string.numerology_latente)}: ${(reducir(herencia+consciente))}"

        //Pilares
        binding.tvPareja.text = "${getText(R.string.numerology_pareja)}: ${reducir( reducir(user.month)+ reducir(now.year))}"
        binding.tvReto.text = "${getText(R.string.numerology_reto)}: ${reducir( reducir(user.day)+ reducir(now.year))}"
        binding.tvSocial.text = "${getText(R.string.numerology_social)}: ${reducir( reducir(user.year)+ reducir(now.year))}"
        binding.tvTrabajo.text = "${getText(R.string.numerology_trabajo)}: ${reducir( ascendente+ reducir(now.year))}"

    }

    /*
    * Función que reduce un número a una cifra
    * @param    num
    * @return   int
     */
    private fun reducir(num: Int): Int{
        var final = num
        if(num > 9 && !isMaestro(num)){
            final = 0
            for(decimal in num.toString()){
                final += decimal.digitToInt()
            }

            if(final > 9 && !isMaestro(final)) //Comprobar si NO es número maestro
                final = reducir(final)
        }
        return final
    }

    /*
    * Función que comprueba si un número es maestro
    * @param    num
    * @return   Boolean
     */
    private fun isMaestro(num: Int): Boolean {
        return num==11 || num==22 || num==33 || num==44
    }
}