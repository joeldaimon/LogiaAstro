package com.joguco.logiaastro.tabs.tarot

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.ActivityTarotBinding
import com.joguco.logiaastro.databinding.CardItemBinding
import com.joguco.logiaastro.interfaces.OnCardClick
import com.joguco.logiaastro.model.Tarot.TarotCard
import com.joguco.logiaastro.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TarotActivity : AppCompatActivity(), OnCardClick {
    //Binding
    private lateinit var binding: ActivityTarotBinding

    //Usuario
    private lateinit var user: String

    //Lista de cartas
    private var lista = ArrayList<TarotCard>()

    //Atributos
    private var mostrarDatos = 0
    private var tematica:String = ""
    private var opcion:String = ""

    private val layoutList: FrameLayout by lazy { findViewById(R.id.containerList) } //List fragment
    private lateinit var itemBinding: CardItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTarotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var sharedpreferences = getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE)
        user = sharedpreferences.getString(MainActivity.USER_KEY, null).toString()

        //Iniciamos item card Binding
        itemBinding = CardItemBinding.inflate(layoutInflater)

        initListeners()
    }

    /*
    *   Método que inicia Listeners
    */
    private fun initListeners() {
        //Temática
        binding.btnLove.setOnClickListener{
            setTheme("love")
        }
        binding.btnMoney.setOnClickListener{
            setTheme("money")
        }
        binding.btnWork.setOnClickListener{
            setTheme("work")
        }
        binding.btnFriends.setOnClickListener{
            setTheme("friends")
        }
        binding.btnSpirituality.setOnClickListener{
            setTheme("spirituality")
            var db = Firebase.firestore
            CoroutineScope(Dispatchers.Main).launch{
                db.collection("users").document(user).get()
                    .addOnSuccessListener {
                        var tarotLevel = it.get("tarotLevel") as Long
                        tarotLevel++
                        db.collection("users").document(user).update(
                            hashMapOf(
                                "tarotLevel" to tarotLevel
                            ) as Map<String, Any>
                        )
                    }
                    .addOnFailureListener { exception ->
                        Log.i("LOGIA-ASTRO", "Error al actualizar usuario en Tarot: ", exception)
                    } .await()
            }
        }

        //Opciones
        binding.btnYesNo.setOnClickListener{
            setOption("yesno")
        }
        binding.btnAdvice.setOnClickListener{
            setOption("advice")
        }
        binding.btnFuture.setOnClickListener{
            setOption("future")
        }
        binding.btnSpecial.setOnClickListener{
            setOption("special")
        }
    }


    /*
    *   Método que setea la temática de la tirada de TAROT
    *   @param      tematica
    */
    private fun setTheme(tematica:String) {
        this.tematica = tematica
        binding.lyThematics.visibility = View.GONE

        when(tematica){
            "love" -> {
                binding.btnSpecial.text = resources.getString(R.string.special_love_tarot)
            }
            "money" -> {
                binding.btnSpecial.text = resources.getString(R.string.special_money_tarot)
            }
            "work" -> {
                binding.btnSpecial.text = resources.getString(R.string.special_work_tarot)
            }
            "friends" -> {
                binding.btnSpecial.text = resources.getString(R.string.special_friends_tarot)
            }
            "spirituality" -> {
                binding.btnSpecial.text = resources.getString(R.string.special_spirituality_tarot)
            }
        }

        binding.tvTitle.text = resources.getString(R.string.options_tarot)
        binding.lyOptions.visibility = View.VISIBLE
    }


    /*
    *   Método que setea la opción de la tirada de TAROT
    *   @param      opcion
    */
    private fun setOption(opcion:String){
        this.opcion = opcion
        binding.lyOptions.visibility = View.GONE

        binding.tvTitle.text = resources.getString(R.string.title_tarot)

        //Cargamos RecyclerView de Cartas
        loadRecyclerView()
    }


    /*
    * Carga el RecylcerView
    */
    private fun loadRecyclerView() {
        supportFragmentManager.beginTransaction()
            .replace(layoutList.id, TarotFragment.newInstance(6))
            .addToBackStack(null)
            .commit()
    }

    /*
     * Añade carta clickada a la lista
     * @param   carta
     */
    private fun addCarta(carta: TarotCard){
        lista.add(carta)
        mostrarDatos++
        if(mostrarDatos == 3)(mostrarDatos())
    }

    /*
     * Muestra datos tras elegir 3 cartas
     */
    private fun mostrarDatos(){
        var id = getImage(lista[0].imagen)
        binding.ivOne.setImageResource(id)
        id = getImage(lista[1].imagen)
        binding.ivTwo.setImageResource(id)
        id = getImage(lista[2].imagen)
        binding.ivThree.setImageResource(id)

        binding.nombres.text = lista[0].nombre+" "+lista[1].nombre+" "+lista[2].nombre

        when(opcion){
            "yesno" -> {
                if(tematica == "spirituality"){
                    var result = lista[0].respuesta + lista[1].respuesta + lista[2].respuesta
                    binding.datos.text = if (result == 3) getText(R.string.tarot_si)  else (if (result == 2) getText(R.string.tarot_nose) else getText(R.string.tarot_no))
                } else {
                    var result = lista[0].respuesta + lista[1].respuesta + lista[2].respuesta
                    binding.datos.text = if (result == 3) getText(R.string.tarot_esp_si)  else (if (result == 2) getText(R.string.tarot_esp_nose) else getText(R.string.tarot_esp_no))
                }
            }
            "advice" -> {
                if(tematica == "love"){
                    binding.datos.text = "${getText(R.string.ta_inicio)} "+lista[0].loveAdvice.inicio+
                            " ${getText(R.string.ta_nudo)} "+lista[1].loveAdvice.nudo+
                            " ${getText(R.string.ta_final)} "+lista[2].loveAdvice.final+"."
                }
                if(tematica == "money"){
                    binding.datos.text = "${getText(R.string.ta_inicio)} "+lista[0].moneyAdvice.inicio+
                            " ${getText(R.string.ta_nudo)} "+lista[1].moneyAdvice.nudo+
                            " ${getText(R.string.ta_final)} "+lista[2].moneyAdvice.final+"."
                }
                if(tematica == "work"){
                    binding.datos.text = "${getText(R.string.ta_inicio)} "+lista[0].workAdvice.inicio+
                            " ${getText(R.string.ta_nudo)} "+lista[1].workAdvice.nudo+
                            " ${getText(R.string.ta_final)}"+lista[2].workAdvice.final+"."
                }
                if(tematica == "friends"){
                    binding.datos.text = "${getText(R.string.ta_inicio)} "+lista[0].friendsAdvice.inicio+
                            " ${getText(R.string.ta_nudo)} "+lista[1].friendsAdvice.nudo+
                            " ${getText(R.string.ta_final)} "+lista[2].friendsAdvice.final+"."
                }
                if(tematica == "spirituality"){
                    binding.datos.text = "${getText(R.string.ta_inicio)} "+lista[0].spiritualityAdvice.inicio+
                            " ${getText(R.string.ta_nudo)} "+lista[1].spiritualityAdvice.nudo+
                            " ${getText(R.string.ta_final)} "+lista[2].spiritualityAdvice.final+"."
                }
            }
            "future" -> {
                if(tematica == "love"){
                    binding.datos.text = "${getText(R.string.tl_inicio)} "+lista[0].loveFuture.inicio+
                            " ${getText(R.string.tl_nudo)} "+lista[1].loveFuture.nudo+
                            " ${getText(R.string.tl_final)} "+lista[2].loveFuture.final+"."
                }
                if(tematica == "money"){
                    binding.datos.text = lista[0].moneyFuture.inicio+
                            " ${getText(R.string.tf_nudo)} "+lista[1].moneyFuture.nudo+
                            " ${getText(R.string.tl_final)} "+lista[2].moneyFuture.final+"."
                }
                if(tematica == "work"){
                    binding.datos.text = lista[0].workFuture.inicio+
                            " ${getText(R.string.tf_nudo)} "+lista[1].workFuture.nudo+
                            " ${getText(R.string.tl_final)} "+lista[2].workFuture.final+"."
                }
                if(tematica == "friends"){
                    binding.datos.text = "${getText(R.string.tf_inicio)} " +lista[0].friendsFuture.inicio+
                            " ${getText(R.string.tf_nudo)} "+lista[1].friendsFuture.nudo+
                            " ${getText(R.string.tl_final)} "+lista[2].friendsFuture.final+"."
                }
                if(tematica == "spirituality"){
                    binding.datos.text = "${getText(R.string.tfe_inicio)} "+lista[0].spiritualityFuture.inicio+
                            "${getText(R.string.tfe_nudo)} "+lista[1].spiritualityFuture.nudo+
                            " ${getText(R.string.tl_final)} "+lista[2].spiritualityFuture.final+"."
                }
            }
            "special" -> {
                if(tematica == "love"){
                    binding.datos.text = "${getText(R.string.tlt_inicio)} "+lista[0].loveSpecial.inicio+
                            "${getText(R.string.tlt_nudo)} "+lista[1].loveSpecial.nudo+
                            " ${getText(R.string.tlt_final)} "+lista[2].loveSpecial.final+"."
                }
                if(tematica == "money"){
                    binding.datos.text = "${getText(R.string.tt_inicio)} "+lista[0].moneySpecial.inicio+
                            "${getText(R.string.tt_nudo)} "+lista[1].moneySpecial.nudo+
                            "${getText(R.string.tt_final)} "+lista[2].moneySpecial.final+"."
                }
                if(tematica == "work"){
                    binding.datos.text = "${getText(R.string.tt_inicio)} "+lista[0].workSpecial.inicio+
                            "${getText(R.string.tt_nudo)} "+lista[1].workSpecial.nudo+
                            "${getText(R.string.tt_final)} "+lista[2].workSpecial.final+"."
                }
                if(tematica == "friends"){
                    binding.datos.text = "${getText(R.string.tlt_inicio)} "+lista[0].friendsSpecial.inicio+
                            "${getText(R.string.tlt_nudo)} "+lista[1].friendsSpecial.nudo+
                            " ${getText(R.string.tlt_final)} "+lista[2].friendsSpecial.final+"."
                }
                if(tematica == "spirituality"){
                    binding.datos.text = "${getText(R.string.tet_inicio)} "+lista[0].spiritualitySpecial.inicio+
                            "${getText(R.string.tet_nudo)} "+lista[1].spiritualitySpecial.nudo+
                            " ${getText(R.string.tet_final)} "+lista[2].spiritualitySpecial.final+"."
                }
            }
        }

        binding.containerList.isVisible = false
    }

    /*
     * Carga imagen de la carta
     * @param   image
     */
    fun getImage(image: String): Int {
        val imageName = image.split(".")[0]
        val resId = resources.getIdentifier(
            imageName,
            "drawable",
            applicationInfo.packageName
        )

        return resId
    }

    /*
     * Carga detalles de la carta
     * @param   carta
     */
    override fun OnCardClick(carta: TarotCard) {
        addCarta(carta)
    }
}