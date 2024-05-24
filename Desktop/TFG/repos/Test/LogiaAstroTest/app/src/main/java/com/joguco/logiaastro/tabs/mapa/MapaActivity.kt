package com.joguco.logiaastro.tabs.mapa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.ActivityMapaBinding
import com.joguco.logiaastro.menu.AccountActivity
import com.joguco.logiaastro.ui.ComprasActivity
import com.joguco.logiaastro.ui.MainActivity

class MapaActivity : AppCompatActivity() {
    //Binding
    private lateinit var binding: ActivityMapaBinding

    private lateinit var mapa: ArrayList<Int>

    companion object{
        const val KEY_EXTRA_MAPA = "KEY_EXTRA_MAPA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Inflando layout
        setContentView(ActivityMapaBinding.inflate(layoutInflater).also { binding = it }.root)

        //Recibimos String que nos indica que tipo de compra es
        mapa = intent?.getIntegerArrayListExtra(KEY_EXTRA_MAPA)!!
        initMapa()
        initListeners()
    }

    /*
    * Función que inicia LISTENERS
     */
    private fun initListeners() {
        binding.fab.setOnClickListener{
            val intent = Intent (this, ComprasActivity::class.java).apply{
                putExtra(ComprasActivity.KEY_EXTRA_COMPRA, "numerology")
            }
            startActivity(intent)
        }

        //Mochila
        binding.mKarma.setOnClickListener{
            binding.tvMapTitle.text = getString(R.string.numerology_karma)
        }
        binding.mPersonalidad.setOnClickListener{
            binding.tvMapTitle.text = getString(R.string.numerology_pers)
        }
        binding.mVidaPasada.setOnClickListener{
            binding.tvMapTitle.text = getString(R.string.numerology_vida_pasada)
        }
        binding.mAscendente.setOnClickListener{
            binding.tvMapTitle.text = getString(R.string.numerology_ascendente)
        }

        //Realizaciones
        binding.rOne.setOnClickListener{
            binding.tvMapTitle.text = getString(R.string.numerology_realizacion)+" 1"
        }
        binding.rTwo.setOnClickListener{
            binding.tvMapTitle.text = getString(R.string.numerology_realizacion)+" 2"
        }
        binding.rThree.setOnClickListener{
            binding.tvMapTitle.text = getString(R.string.numerology_realizacion)+" 3"
        }
        binding.rFour.setOnClickListener{
            binding.tvMapTitle.text = getString(R.string.numerology_realizacion)+" 4"
        }

        //Desafíos
        binding.dOne.setOnClickListener{
            binding.tvMapTitle.text = getString(R.string.numerology_desafio)+" 1"
        }
        binding.dTwo.setOnClickListener{
            binding.tvMapTitle.text = getString(R.string.numerology_desafio)+" 2"
        }
        binding.dThree.setOnClickListener{
            binding.tvMapTitle.text = getString(R.string.numerology_desafio)+" 3"
        }
        binding.dFour.setOnClickListener{
            binding.tvMapTitle.text = getString(R.string.numerology_desafio)+" 4"
        }
    }

    /*
    * Función que inicia el Mapa numerológico
     */
    private fun initMapa() {
        if(mapa.size > 10){
            binding.mKarma.text = mapa[0].toString()
            binding.mPersonalidad.text = mapa.get(1).toString()
            binding.mVidaPasada.text = mapa[2].toString()
            binding.mAscendente.text = mapa[3].toString()

            binding.rOne.text = mapa[4].toString()
            binding.rTwo.text = mapa[5].toString()
            binding.rThree.text = mapa[6].toString()
            binding.rFour.text = mapa[7].toString()

            binding.dOne.text = mapa[8].toString()
            binding.dTwo.text = mapa[9].toString()
            binding.dThree.text = mapa[10].toString()
            binding.dFour.text = mapa[11].toString()
        }
    }
}