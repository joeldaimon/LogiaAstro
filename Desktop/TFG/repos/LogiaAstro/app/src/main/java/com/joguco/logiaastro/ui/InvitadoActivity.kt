package com.joguco.logiaastro.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.ActivityInvitadoBinding
import com.joguco.logiaastro.login.register.RegisterActivity
import com.joguco.logiaastro.tabs.contratoalmico.ContratoActivity
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.math.floor

class InvitadoActivity : AppCompatActivity() {
    //Binding
    private lateinit var binding: ActivityInvitadoBinding

    //Atributos
    private lateinit var now: LocalDateTime

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInvitadoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnTarot.setColorFilter(R.color.purple_500)
        binding.btnHoroscopo.setColorFilter(R.color.purple_500)
        binding.btnInfoMensual.setColorFilter(R.color.purple_500)
        binding.btnAngeles.setColorFilter(R.color.purple_500)
        binding.btnAmistades.setColorFilter(R.color.purple_500)

        now = LocalDateTime.now()
        faseLunar()
        initListeners()
    }

    private fun initListeners() {
        binding.btnLogia.setOnClickListener{

        }

        binding.btnServicios.setOnClickListener{
            val intent = Intent(this, ComprasActivity::class. java).apply{
                putExtra(ComprasActivity.KEY_EXTRA_COMPRA, "todo")
            }
            startActivity(intent)
        }

        binding.btnContratoAlmico.setOnClickListener{
            val intent = Intent(this, ContratoActivity::class.java)
            startActivity(intent)
        }

        binding.tvRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class. java)
            startActivity(intent)
        }
    }

    //Setea la FASE LUNAR
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

    // Calcular fase Lunar
    @RequiresApi(Build.VERSION_CODES.O)
    private fun calcularFaseLunar(year: Int, month: Int, day: Int): Int {
        val lp = 2551443
        val now = LocalDateTime.of(year, month, day, 0, 0, 0)
        val newMoon = LocalDateTime.of(1970, 1, 7, 0, 0, 0)
        val phase = ChronoUnit.SECONDS.between(newMoon, now) % lp
        return floor(phase / (24.0 * 3600.0)).toInt() + 1
    }
}