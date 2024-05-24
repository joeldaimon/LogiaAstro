package com.joguco.logiaastro.ui.Perfiles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.joguco.logiaastro.R
import com.joguco.logiaastro.database.entities.UserEntity
import com.joguco.logiaastro.tabs.horoscopo.HoroscopoDetailFragment
import com.joguco.logiaastro.ui.MainActivity


private const val ARG_USER = "ARG_USER"

/**
 *
 */
class PerfilesDetailFragment : Fragment() {
    //Atributes
    private var user: Int? = null
    private var userEnt: UserEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getInt(ARG_USER)
            var lista = (activity as MainActivity?)?.getUser()!!
            //userEnt = UserEntity.getHoroscopeById(user)
        }
    }

    //Function that fills the fragment when its created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        //Filling TextViews from fragment_detail.xml
        view.findViewById<TextView>(R.id.tvSummary).text = sign?.summary
        view.findViewById<TextView>(R.id.tvElemento).text  = getText(R.string.horoscopo_elemento).toString()+": "+ sign?.element
        view.findViewById<TextView>(R.id.tvEnergia).text  = getText(R.string.horoscopo_energia).toString()+": "+sign?.energy
        view.findViewById<TextView>(R.id.tvColor).text  = getText(R.string.horoscopo_color).toString()+": "+sign?.color
        view.findViewById<TextView>(R.id.tvRegente).text  = getText(R.string.horoscopo_regente).toString()+": "+sign?.regent
        view.findViewById<TextView>(R.id.tvExaltacion).text  = getText(R.string.horoscopo_exaltacion).toString()+": "+sign?.exaltation
        view.findViewById<TextView>(R.id.tvCaida).text  = getText(R.string.horoscopo_caida).toString()+": "+sign?.fall
        view.findViewById<TextView>(R.id.tvTarotCard).text  = getText(R.string.horoscopo_tarot).toString()+": "+sign?.tarotCard



        //Converting genres from Serie to a String for <tvGenres>
        var genresString:String = getText(R.string.horoscopo_tags).toString()+":"
        for (genre in sign?.tags!!){ //For every genre in GENRES Array<String>
            genresString += "\n$genre" //Save it in String <genresString>
        }

        //Fill <tvGenres> with <genresString>
        view.findViewById<TextView>(R.id.tvTags).text  = genresString

        //Image convertion
        val context: Context = view.findViewById<ImageView>(R.id.ivPoster).context //Context of <ivPoster>
        val imageName = sign?.image?.split(".")?.get(0) //Splitting image name by dot
        val id = context?.resources?.getIdentifier(
            imageName,
            "raw",
            context.packageName
        ) //This returns the ID of the drawable resource named imageName
        if (id != null) { //If ID is not null
            //Set the image of <ivPoster>
            view.findViewById<ImageView>(R.id.ivPoster).setImageResource(id)
        }*/

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_horoscopo_detail, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(signId: Int) =
            HoroscopoDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_USER, signId)
                }
            }
    }
}