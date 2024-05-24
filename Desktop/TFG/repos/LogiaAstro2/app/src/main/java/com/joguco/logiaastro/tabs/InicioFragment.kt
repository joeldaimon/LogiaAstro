package com.joguco.logiaastro.tabs

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.FragmentInicioBinding
import com.joguco.logiaastro.mision.GuessActivity
import com.joguco.logiaastro.mision.VelasActivity
import com.joguco.logiaastro.tabs.contratoalmico.ContratoActivity
import com.joguco.logiaastro.tabs.horoscopo.HoroscopoActivity
import com.joguco.logiaastro.tabs.numeros.NumAngelesActivity
import com.joguco.logiaastro.tabs.tarot.TarotActivity
import com.joguco.logiaastro.ui.ComprasActivity
import com.joguco.logiaastro.ui.MainActivity
import com.joguco.logiaastro.tabs.Perfiles.PerfilesActivity
import com.joguco.logiaastro.tabs.amistades.AmistadesPlanetasActivity
import com.joguco.logiaastro.tabs.grados.GradosActivity
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.floor

/**
 * INICIO
 */
class InicioFragment : Fragment() {
    //Binding
    private lateinit var binding: FragmentInicioBinding

    //Tiempo y fecha actual
    private lateinit var now: LocalDateTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflando layout
        return FragmentInicioBinding.inflate(inflater, container, false).also { binding = it}.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Titulo
        var title: String = getText(R.string.app_title).toString()
        (activity as MainActivity?)?.setActionBarTitle(title)

        now = LocalDateTime.now()

        faseLunar()
        initListeners()
    }

    /*
    * Método que inicia Listeners
     */
    private fun initListeners() {
        binding.btnHoroscopo.setOnClickListener{
            activity?.let{
                val intent = Intent (it, HoroscopoActivity::class.java)
                it.startActivity(intent)
            }
        }

        binding.btnTarot.setOnClickListener{
            activity?.let{
                val intent = Intent (it, TarotActivity::class.java)
                it.startActivity(intent)
            }
        }

        binding.btnGrados.setOnClickListener{
            activity?.let{
                val intent = Intent (it, GradosActivity::class.java)
                it.startActivity(intent)
            }
        }

        binding.btnServicios.setOnClickListener{
            activity?.let{
                val intent = Intent (it, ComprasActivity::class.java).apply{
                    putExtra(ComprasActivity.KEY_EXTRA_COMPRA, "todo")
                }
                it.startActivity(intent)
            }
        }

        binding.btnContratoAlmico.setOnClickListener{
            activity?.let{
                val intent = Intent (it, ContratoActivity::class.java)
                it.startActivity(intent)
            }
        }

        binding.btnAngeles.setOnClickListener{
            activity?.let{
                val intent = Intent (it, NumAngelesActivity::class.java)
                it.startActivity(intent)
            }
        }

        binding.btnUsers.setOnClickListener{
            activity?.let{
                val intent = Intent (it, PerfilesActivity::class.java)
                it.startActivity(intent)
            }
        }

        binding.btnAmistades.setOnClickListener{
            activity?.let{
                val intent = Intent (it, AmistadesPlanetasActivity::class.java)
                it.startActivity(intent)
            }
        }

        binding.ivBootcamp.setOnClickListener{
            activity?.let{
                val intent = Intent (it, GuessActivity::class.java)
                it.startActivity(intent)
            }
        }
        binding.ivVelas.setOnClickListener{
            activity?.let{
                val intent = Intent (it, VelasActivity::class.java)
                it.startActivity(intent)
            }
        }
        binding.btnCompartir.setOnClickListener{
            val intent= Intent()
            intent.action=Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,"Hey! Prueba esta App de Astrologia, Tarot y numerología: Logia Astro")
            intent.type="text/plain"
            startActivity(Intent.createChooser(intent,"Compartir con:"))
        }
        binding.btnSigueme.setOnClickListener{
            var url = Uri.parse("http://www.tiktok.com/@joeldaimon")
            var intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
    }


    /*
    * Método que inicia la fase lunar
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun faseLunar() {

        when(calcularFaseLunar(now.year, now.monthValue, now.dayOfMonth)){
            in 0..2-> {
                binding.ivFaseLunar.setImageResource(R.drawable.newmoon)
                binding.tvEvents.text = getText(R.string.index_event_nueva)
            }
            in 3..12 -> {
                binding.ivFaseLunar.setImageResource(R.drawable.halfmoon)
                binding.tvEvents.text = getText(R.string.index_event_half)
            }
            in 13..15->{
                binding.tvEvents.text = getText(R.string.index_event_casillena)
                binding.ivFaseLunar.setImageResource(R.drawable.halfmoon)

            }
            16 -> {
                binding.ivFaseLunar.setImageResource(R.drawable.full)
                binding.tvEvents.text = getText(R.string.index_event_llena)
            }
            17 -> {
                binding.tvEvents.text = getText(R.string.index_event_postllena)
                binding.ivFaseLunar.setImageResource(R.drawable.waningmoon)
            }
            30 -> {
                binding.ivFaseLunar.setImageResource(R.drawable.newmoon)
                binding.tvEvents.text = getText(R.string.index_event_nueva)
            }
            else -> {
                binding.ivFaseLunar.setImageResource(R.drawable.waningmoon)
                binding.tvEvents.text = getText(R.string.index_event_half)
            }
        }
    }

    /*
    * Método que calcula la fase lunar actual
    * @param    year, month, day
    * @return   int
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun calcularFaseLunar(year: Int, month: Int, day: Int): Int {
        val lp = 2551443
        val now = LocalDateTime.of(year, month, day, 0, 0, 0)
        val newMoon = LocalDateTime.of(1970, 1, 7, 0, 0, 0)
        val phase = ChronoUnit.SECONDS.between(newMoon, now) % lp
        return floor(phase / (24.0 * 3600.0)).toInt() + 1
    }
}