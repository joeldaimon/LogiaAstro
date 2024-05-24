package com.joguco.logiaastro.tabs.numeros

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.ActivityNumAngelesBinding
import com.joguco.logiaastro.interfaces.OnNumAngelClick
import com.joguco.logiaastro.model.NumAngeles

class NumAngelesActivity : AppCompatActivity(), OnNumAngelClick {
    //Binding
    private lateinit var binding: ActivityNumAngelesBinding

    //Layouts
    private val layoutListAngeles: FrameLayout by lazy { findViewById(R.id.containerListNumAngeles) } //List fragment
    private val layoutListMaestros: FrameLayout by lazy { findViewById(R.id.containerListNumMaestros) } //List fragment
    private val layoutListEspejo: FrameLayout by lazy { findViewById(R.id.containerListNumEspejo) } //List fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityNumAngelesBinding.inflate(layoutInflater).also { binding = it }.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        initListeners()
        loadRecyclerView()
    }

    /*
    * Método que inicia LISTENERS
     */
    private fun initListeners() {
        binding.btAngeles.setOnClickListener{
            binding.containerListNumAngeles.visibility = View.VISIBLE
            binding.containerListNumEspejo.visibility = View.GONE
            binding.containerListNumMaestros.visibility = View.GONE
        }
        binding.btnEspejo.setOnClickListener{
            binding.containerListNumAngeles.visibility = View.GONE
            binding.containerListNumEspejo.visibility = View.VISIBLE
            binding.containerListNumMaestros.visibility = View.GONE
        }
        binding.btnMaestros.setOnClickListener{
            binding.containerListNumAngeles.visibility = View.GONE
            binding.containerListNumEspejo.visibility = View.GONE
            binding.containerListNumMaestros.visibility = View.VISIBLE
        }
    }

    /*
    * Método que carga un numEspejo by Id
    * @param    id
     */
    private fun loadNumEspejo(id: Int){
        var num = NumAngeles.getNumAngelesById(this,id)

        if (num != null) {
            binding.tvMensaje.text = num.mensaje
            binding.tvShort.text  = num.short.uppercase()
        }
    }


    /*
    * Método que carga el RecyclerView
     */
    private fun loadRecyclerView() {
        supportFragmentManager.beginTransaction()
            .replace(layoutListAngeles.id, NumAngelesListFragment.newInstance(3, "angeles"))
            .addToBackStack(null)
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(layoutListMaestros.id, NumAngelesListFragment.newInstance(3, "maestro"))
            .addToBackStack(null)
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(layoutListEspejo.id, NumAngelesListFragment.newInstance(3, "espejo"))
            .addToBackStack(null)
            .commit()
    }

    /*
    * Método onClick para numAngel
    * @param    numAngel
     */
    override fun onNumAngelClick(numAngel: NumAngeles) {
        loadNumEspejo(numAngel.id)
    }
}