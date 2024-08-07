package com.joguco.logiaastro.tabs.contratoalmico

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.ActivityContratoBinding
import com.joguco.logiaastro.ui.ComprasActivity
import com.joguco.logiaastro.ui.ComprasActivity.Companion.KEY_EXTRA_COMPRA
import kotlin.math.round

class ContratoActivity : AppCompatActivity() {
    //Atributos
    private lateinit var binding: ActivityContratoBinding

    private val ABC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private var nombre: String = ""

    //Lista de números
    private var lista: ArrayList<Int> = ArrayList<Int>()

    //Numeros
    var karmaf: Int = 0
    var karmae = 0
    var talentof = 0
    var talentoe = 0
    var misionf = 0
    var misione = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Instanciando binding
        binding = ActivityContratoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setListeners()
    }

    private fun setListeners() {
        binding.btnCalcular.setOnClickListener{ calcular() }
        binding.btnBorrar.setOnClickListener{ borrar() }
        binding.btnCompra.setOnClickListener{
            val intent = Intent(this, ComprasActivity::class. java).apply{
                putExtra(KEY_EXTRA_COMPRA, "soulcontract")
            }
            startActivity(intent)
        }
    }

    /*
    *   Función que calcula los números a tavés del nombre
     */
    private fun calcular() {
        nombre = binding.etNombre.text.toString()

        if(nombre == ""){
            binding.tvExplicacion.setTextColor(Color.RED)
            binding.tvExplicacion.setText(R.string.contrato_error_nombre)
        }else {
            binding.btnCalcular.isEnabled = false
            binding.tvExplicacion.setTextColor(Color.WHITE)
            binding.tvExplicacion.setText(R.string.contrato_escribe_nombre)
            binding.lySignificado.isVisible = true
            binding.cv.isVisible = false
            binding.btnCompra.isVisible = true

            //Quitamos los espacios al nombre
            nombre = nombre.replace(" ","").uppercase()

            //Recorremos nombre
            for (i in 0 until nombre.length) {
                lista.add(ABC.indexOf(nombre[i]) + 1)
            }

            //Colocamos datos en cada Botón
            try {
                var i = 0 //Contador

                while(true){
                    karmaf += lista[i]
                    i++

                    karmae += lista[i]
                    i++

                    talentof += lista[i]
                    i++

                    talentoe += lista[i]
                    i++

                    misionf += lista[i]
                    i++

                    misione += lista[i]
                    i++

                    if(i == (nombre.length)) break;
                }
            } catch(e: IndexOutOfBoundsException) { }

            //Añadimos las SUMAS y llamamos al método reducir
            binding.btnOne.text = binding.btnOne.text.toString()+"> "+karmaf+"/"+reducir(karmaf)
            binding.btnTwo.text = binding.btnTwo.text.toString()+"> "+karmae+"/"+reducir(karmae)
            binding.btnThree.text = binding.btnThree.text.toString()+"> "+talentof+"/"+reducir(talentof)
            binding.btnFour.text = binding.btnFour.text.toString()+"> "+talentoe+"/"+reducir(talentoe)
            binding.btnFive.text = binding.btnFive.text.toString()+"> "+misionf+"/"+reducir(misionf)
            binding.btnSix.text = binding.btnSix.text.toString()+"> "+misione+"/"+reducir(misione)

        }
    }

    /*
    * Función que determina si un número es válido
    * @param    num
    * @return   boolean
    * validos   del 1-90 y numeros espejo
     */
    private fun numValido(num: Int): Boolean {
        if(num == 0) return false
        if(num<10) return true
        if(num==11 || num==22 || num==33 || num==44) return true

        return false
    }

    /*
    * Función que reduce números a una soal cifra o maestro
    * @param    num
    * @return   int
     */
    private fun reducir(num: Int): Int {
        var numero:Float = num/10.toFloat()
        var entero = numero.toInt()
        var decimal = round((numero - entero)*10)
        var suma = entero+decimal.toInt()
        if(!numValido(suma)) suma = reducir(suma);

        return suma;
    }

    /*
    * Método que borra y resetea la activity
     */
    private fun borrar(){
        //Reseteamos texto
        binding.btnOne.text = "1.KF: "
        binding.btnTwo.text = "2.KE: "
        binding.btnThree.text = "3.TF: "
        binding.btnFour.text = "4.TE: "
        binding.btnFive.text = "5.MF: "
        binding.btnSix.text = "6.ME: "

        //Reseteamos numeros
        karmaf = 0
        karmae = 0
        talentof = 0
        talentoe = 0
        misionf = 0
        misione = 0

        //Borramos lista
        lista.clear()

        //Reseteamos label por si le dimos a borrar al encontrar error
        binding.tvExplicacion.setTextColor(Color.WHITE)
        binding.tvExplicacion.setText(R.string.contrato_escribe_nombre)
        binding.etNombre.getText().clear()

        binding.btnCalcular.isEnabled = true
        binding.lySignificado.isVisible = false
        binding.cv.isVisible = true
        binding.btnCompra.isVisible = false
    }
}