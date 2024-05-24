package com.joguco.logiaastro.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.joguco.logiaastro.databinding.ActivityCarroBinding
import com.joguco.logiaastro.databinding.ActivityComprasBinding

class CarroActivity : AppCompatActivity() {
    //Binding
    private lateinit var binding: ActivityCarroBinding

    //Atributos
    private lateinit var compra: String

    companion object{
        const val KEY_EXTRA_CARRO = "KEY_EXTRA_CARRO"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Inflando layout
        setContentView(ActivityCarroBinding.inflate(layoutInflater).also { binding = it }.root)

        //Recibimos String que nos indica que tipo de compra es
        compra = intent?.getStringExtra(KEY_EXTRA_CARRO)!!

        if(compra.contains("Vídeo")){
            binding.tvCompra.text = "Elige una categoria: $compra"
        } else{
            binding.tvCompra.text = compra
        }
    }
}