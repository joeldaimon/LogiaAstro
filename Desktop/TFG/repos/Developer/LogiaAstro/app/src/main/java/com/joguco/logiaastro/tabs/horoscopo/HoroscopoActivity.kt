package com.joguco.logiaastro.tabs.horoscopo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.ActivityHoroscopoBinding
import com.joguco.logiaastro.model.Horoscope
import com.joguco.logiaastro.interfaces.OnHoroscopeClick

class HoroscopoActivity : AppCompatActivity(), OnHoroscopeClick {
    //Binding
    private lateinit var binding: ActivityHoroscopoBinding

    //Layouts
    private val layoutList: FrameLayout by lazy { findViewById(R.id.containerList) } //List fragment
    private val layoutDetail: FrameLayout? by lazy { findViewById(R.id.containerDetail) } //Detail fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityHoroscopoBinding.inflate(layoutInflater).also { binding = it}.root)

        loadRecyclerView()

        //Si es landscape
        if (isLandScape()){
            //Cargamos detalles también
            loadDetailFragment(true, 1)
        }
    }

    /*
    * Función que carga RecyclerView
     */
    private fun loadRecyclerView() {
        supportFragmentManager.beginTransaction()
            .replace(layoutList.id, HoroscopoListFragment.newInstance(1))
            .addToBackStack(null)
            .commit()
    }

    /*
    * Función que determina si el dispositivo está en landscape
    * @returns  boolean
     */    private fun isLandScape():Boolean {
        return layoutDetail != null
    }

    /*
    * Función para onClick de Horoscope
    * @param    horoscopo
     */
    override fun onHoroscopeClick(horoscopo: Horoscope) {
        loadDetailFragment(isLandScape(), horoscopo.id)
    }

    /*
    * Función que carga detalles
     */
    private fun loadDetailFragment(twoPane:Boolean, signId:Int){
        val id = layoutDetail?.id?:layoutList.id

        supportFragmentManager.beginTransaction()
            .replace(id, HoroscopoDetailFragment.newInstance(signId))
            .addToBackStack(null)
            .commit()
    }
}