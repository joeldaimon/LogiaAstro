package com.joguco.logiaastro.tabs.amistades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.ActivityAmistadesPlanetasBinding

class AmistadesPlanetasActivity : AppCompatActivity() {
    //Binding
    private lateinit var binding: ActivityAmistadesPlanetasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Inflando layout
        setContentView(ActivityAmistadesPlanetasBinding.inflate(layoutInflater).also { binding = it }.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        initListeners()
        loadData("saturno")
    }

    /*
    * Método que inicia listeners
     */
    private fun initListeners() {
        binding.ivSol.setOnClickListener{
            loadData("sol")
        }
        binding.ivLuna.setOnClickListener{
            loadData("luna")
        }
        binding.ivMarte.setOnClickListener{
            loadData("marte")
        }
        binding.ivMercurio.setOnClickListener{
            loadData("mercurio")
        }
        binding.ivJupiter.setOnClickListener{
            loadData("jupiter")
        }
        binding.ivVenus.setOnClickListener{
            loadData("venus")
        }
        binding.ivSaturno.setOnClickListener{
            loadData("saturno")
        }
    }

    /*
    * Método que carga datos
    * @param    s
     */
    private fun loadData(s: String) {
        when(s){
            "sol" -> {
                binding.tvPlanetName.text = getText(R.string.astrology_sun).toString()
                binding.tvMejoresAmigos.text =
                    "${getText(R.string.astrology_moon)}, ${getText(R.string.astrology_mars)}, ${getText(R.string.astrology_jupiter)}"
                binding.tvAmigos.text = getText(R.string.amistades_none).toString()
                binding.tvNeutros.text = getText(R.string.astrology_mercury).toString()
                binding.tvEnemigos.text = getText(R.string.amistades_none).toString()
                binding.tvArchienemigos.text =
                    "${getText(R.string.astrology_venus)}, ${getText(R.string.astrology_saturn)}"
            }
            "luna" -> {
                binding.tvPlanetName.text = getText(R.string.astrology_moon).toString()
                binding.tvMejoresAmigos.text = getText(R.string.astrology_sun).toString()
                binding.tvAmigos.text = getText(R.string.astrology_mercury).toString()
                binding.tvNeutros.text = getText(R.string.amistades_all).toString()
                binding.tvEnemigos.text = getText(R.string.amistades_none).toString()
                binding.tvArchienemigos.text = getText(R.string.amistades_none).toString()
            }
            "marte" -> {
                binding.tvPlanetName.text = getText(R.string.astrology_mars).toString()
                binding.tvMejoresAmigos.text =
                    "${getText(R.string.astrology_sun)}, ${getText(R.string.astrology_jupiter)}"
                binding.tvAmigos.text = getText(R.string.astrology_moon).toString()
                binding.tvNeutros.text =
                    "${getText(R.string.astrology_venus)}, ${getText(R.string.astrology_saturn)}"
                binding.tvEnemigos.text = getText(R.string.astrology_mercury).toString()
                binding.tvArchienemigos.text = getText(R.string.amistades_none).toString()
            }
            "mercurio" -> {
                binding.tvPlanetName.text = getText(R.string.astrology_mercury).toString()
                binding.tvMejoresAmigos.text = getText(R.string.astrology_venus).toString()
                binding.tvAmigos.text = getText(R.string.astrology_sun).toString()
                binding.tvNeutros.text =
                    "${getText(R.string.astrology_mars)}, ${getText(R.string.astrology_jupiter)}, ${getText(R.string.astrology_saturn)}"
                binding.tvEnemigos.text = getText(R.string.astrology_moon).toString()
                binding.tvArchienemigos.text = getText(R.string.amistades_none).toString()
            }
            "jupiter" -> {
                binding.tvPlanetName.text = getText(R.string.astrology_jupiter).toString()
                binding.tvMejoresAmigos.text =
                    "${getText(R.string.astrology_sun)}, ${getText(R.string.astrology_mars)}"
                binding.tvAmigos.text = getText(R.string.astrology_moon).toString()
                binding.tvNeutros.text = getText(R.string.astrology_saturn).toString()
                binding.tvEnemigos.text =
                    "${getText(R.string.astrology_venus)}, ${getText(R.string.astrology_mercury)}"
                binding.tvArchienemigos.text = getText(R.string.amistades_none).toString()
            }
            "venus" -> {
                binding.tvPlanetName.text = getText(R.string.astrology_venus).toString()
                binding.tvMejoresAmigos.text =
                    "${getText(R.string.astrology_saturn)}, ${getText(R.string.astrology_mercury)}"
                binding.tvAmigos.text = getText(R.string.amistades_none).toString()
                binding.tvNeutros.text =
                    "${getText(R.string.astrology_jupiter)}, ${getText(R.string.astrology_mars)}"
                binding.tvEnemigos.text = getText(R.string.astrology_moon).toString()
                binding.tvArchienemigos.text = getText(R.string.astrology_sun).toString()
            }
            "saturno" -> {
                binding.tvPlanetName.text = getText(R.string.astrology_saturn).toString()
                binding.tvMejoresAmigos.text = getText(R.string.astrology_venus).toString()
                binding.tvAmigos.text = getText(R.string.astrology_mercury).toString()
                binding.tvNeutros.text = getText(R.string.astrology_jupiter).toString()
                binding.tvEnemigos.text =
                    "${getText(R.string.astrology_moon)}, ${getText(R.string.astrology_mars)}"
                binding.tvArchienemigos.text = getText(R.string.astrology_sun).toString()
            }
        }
    }
}