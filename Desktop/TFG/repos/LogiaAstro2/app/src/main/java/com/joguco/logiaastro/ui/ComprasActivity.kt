package com.joguco.logiaastro.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.joguco.logiaastro.databinding.ActivityComprasBinding

class ComprasActivity : AppCompatActivity() {
    //Binding
    private lateinit var binding: ActivityComprasBinding

    //Atributos
    private lateinit var categoria: String

    companion object{
        const val KEY_EXTRA_COMPRA = "KEY_EXTRA_COMPRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Inflando layout
        setContentView(ActivityComprasBinding.inflate(layoutInflater).also { binding = it }.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        categoria = try {
            //Recibimos String que nos indica que tipo de compra es
            intent?.getStringExtra(KEY_EXTRA_COMPRA)!!
        } catch(e: Exception){
            "todo"
        }

        initListeners()
        initLists()
        initUI()
    }

    /*
    Iniciando Listeners
     */
    private fun initListeners() {
        binding.btnServicios.setOnClickListener{
            binding.lvServicios.isVisible = binding.lvServicios.isVisible != true
        }

        binding.btnAstrologia.setOnClickListener{
            binding.lvAstrologia.isVisible = binding.lvAstrologia.isVisible != true
        }

        binding.btnNum.setOnClickListener{
            binding.lvNumerologia.isVisible = binding.lvNumerologia.isVisible != true
        }

        binding.btnContratoAlmico.setOnClickListener{
            binding.lvContratoAlmico.isVisible = binding.lvContratoAlmico.isVisible != true
        }
    }

    /*
    * Iniciando Listas
     */
    private fun initLists() {
        // SERVICIOS GENERALES
        val serviciosAdapter: ArrayAdapter<*>
        val servicios = arrayOf(
            "Vídeollamada/Vídeo 50€: una hora, todas las preguntas que quieras.",
            "Vídeo/Audiorrespuesta 8€: una pregunta, máximo 10min."
        )

        // Configurando lista Servicios
        serviciosAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, servicios)
        binding.lvServicios.adapter = serviciosAdapter


        // ASTROLOGIA
        val astroAdapter: ArrayAdapter<*>
        val astrologia = arrayOf(
            "Carta escrita básica 30€: planetas en signos, grados, casas y aspectos, con Ascendente.",
            "Carta escrita completa 80€: básica, con 12 cassa, Lilith, Quirón, Nodos y Stelliums.",
            "Dracónica escrita 100€: comparativa entre tropical y draco desde un prisma espiritual."
        )

        // Configurando lista astrologia
        astroAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, astrologia)
        binding.lvAstrologia.adapter = astroAdapter

        // NUMEROLOGIA
        val numAdapter: ArrayAdapter<*>
        val numerologia = arrayOf(
            "Carta numerológica escrita 30€: mochila, nombre, pilares anuales, aspectos, etc."
        )

        // Configurando lista numerologia
        numAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, numerologia)
        binding.lvNumerologia.adapter = numAdapter

        // CONTRATO ALMICO
        val caAdapter: ArrayAdapter<*>
        val ca = arrayOf(
            "Contrato álmico escrito 40€: karma, misión y talentos."
        )

        // Configurando lista Contrato Almico
        caAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, ca)
        binding.lvContratoAlmico.adapter = caAdapter

        if(categoria != "invitado"){
            initListListeners()
        }
    }

    /*
    * Iniciando Listeners de las Listas
     */
    private fun initListListeners(){
        binding.lvServicios.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            var intent = Intent(this, CarroActivity::class.java).apply{
                putExtra(CarroActivity.KEY_EXTRA_CARRO, "$selectedItem;general")
            }
            startActivity(intent)
        }

        binding.lvAstrologia.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            var intent = Intent(this, CarroActivity::class.java).apply{
                putExtra(CarroActivity.KEY_EXTRA_CARRO, "$selectedItem;astrology")
            }
            startActivity(intent)
        }

        binding.lvNumerologia.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            var intent = Intent(this, CarroActivity::class.java).apply{
                putExtra(CarroActivity.KEY_EXTRA_CARRO, "$selectedItem;numerology")
            }
            startActivity(intent)
        }

        binding.lvContratoAlmico.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            var intent = Intent(this, CarroActivity::class.java).apply{
                putExtra(CarroActivity.KEY_EXTRA_CARRO, "$selectedItem;soulcontract")
            }
            startActivity(intent)
        }
    }

    /*
    * Iniciando UI
     */
    private fun initUI() {
        when(categoria){
            "astrology" -> {
                binding.lvAstrologia.isVisible = true
            }
            "numerology" -> {
                binding.lvNumerologia.isVisible = true
            }
            "soulcontract" -> {
                binding.lvContratoAlmico.isVisible = true
            }
        }
    }


}