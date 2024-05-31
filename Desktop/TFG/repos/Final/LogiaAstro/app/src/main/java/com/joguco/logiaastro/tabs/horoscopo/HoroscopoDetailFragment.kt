package com.joguco.logiaastro.tabs.horoscopo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.joguco.logiaastro.R
import com.joguco.logiaastro.model.Horoscope

// Parameters
private const val ARG_HOROSCOPE = "ARG_HOROSCOPE"

/**
 * DETALLE DE HOROSCOPOS
 */
class HoroscopoDetailFragment : Fragment() {
    //Atributos
    private var signId: Int? = null
    private var sign: Horoscope? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            signId = it.getInt(ARG_HOROSCOPE)
            sign = Horoscope.getHoroscopeById(signId)
        }
    }

    /*
    * Método que rellena el fragmento cuando se crea
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Datos para cada item
        view.findViewById<TextView>(R.id.tvSummary).text = sign?.summary
        view.findViewById<TextView>(R.id.tvElemento).text  = getText(R.string.horoscopo_elemento).toString()+": "+ sign?.element
        view.findViewById<TextView>(R.id.tvEnergia).text  = getText(R.string.horoscopo_energia).toString()+": "+sign?.energy
        view.findViewById<TextView>(R.id.tvColor).text  = getText(R.string.horoscopo_color).toString()+": "+sign?.color
        view.findViewById<TextView>(R.id.tvRegente).text  = getText(R.string.horoscopo_regente).toString()+": "+sign?.regent
        view.findViewById<TextView>(R.id.tvExaltacion).text  = getText(R.string.horoscopo_exaltacion).toString()+": "+sign?.exaltation
        view.findViewById<TextView>(R.id.tvCaida).text  = getText(R.string.horoscopo_caida).toString()+": "+sign?.fall
        view.findViewById<TextView>(R.id.tvGrados).text  = getText(R.string.horoscopo_grado).toString()+": "+sign?.degree
        view.findViewById<TextView>(R.id.tvTarotCard).text  = getText(R.string.horoscopo_tarot).toString()+": "+sign?.tarotCard



        //Convirtiendo Array de etiquetas en String
        var tags:String = getText(R.string.horoscopo_tags).toString()+":"
        for (genre in sign?.tags!!){
            tags += "\n$genre"
        }

        view.findViewById<TextView>(R.id.tvTags).text  = tags

        //Conversión de la imágen
        val context: Context = view.findViewById<ImageView>(R.id.ivPoster).context
        val imageName = sign?.image?.split(".")?.get(0)
        val id = context?.resources?.getIdentifier(
            imageName,
            "raw",
            context.packageName
        )
        if (id != null) {
            view.findViewById<ImageView>(R.id.ivPoster).setImageResource(id)
        }

        makeDetailsVisible(view)
    }

    /*
     * Método que hace visibles los detalles al hacer click en FAB
     */
    fun makeDetailsVisible(view: View){
        var fab: FloatingActionButton = view.findViewById(R.id.fab)

        //Listener para el FAB
        fab.setOnClickListener{
            //Aparece un snackbar
            Snackbar.make(view, R.string.horoscopo_snackbar, Snackbar.LENGTH_LONG)
                .setAction(R.string.util_aceptar){
                    //Hacemos visibles los detalles
                    view.findViewById<LinearLayout>(R.id.lyDetails).visibility = View.VISIBLE
                }
                .show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_horoscopo_detail, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(signId: Int) =
            HoroscopoDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_HOROSCOPE, signId)
                }
            }
    }
}