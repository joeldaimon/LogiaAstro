package com.joguco.logiaastro.tabs.tarot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.ActivityTarotBinding
import com.joguco.logiaastro.databinding.CardItemBinding
import com.joguco.logiaastro.interfaces.OnCardClick
import com.joguco.logiaastro.model.Tarot.TarotCard

class TarotActivity : AppCompatActivity(), OnCardClick {
    //Atributos
    private lateinit var binding: ActivityTarotBinding
    private var lista = ArrayList<TarotCard>()
    private var mostrarDatos = 0
    private var tematica:String = ""
    private var opcion:String = ""

    private val layoutList: FrameLayout by lazy { findViewById(R.id.containerList) } //List fragment
    private lateinit var itemBinding: CardItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTarotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Iniciamos item card Binding
        itemBinding = CardItemBinding.inflate(layoutInflater)

        //Iniciamos opciones
        initListeners()
    }

    /*
    *   Iniciamos Listeners
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
                    binding.datos.text = if (result == 3) "sí, ¡vas bien!"  else (if (result == 2) "Hay dudas, debes reconducir tus pensamientos." else "No, por ahí no es.")
                } else {
                    var result = lista[0].respuesta + lista[1].respuesta + lista[2].respuesta
                    binding.datos.text = if (result == 3) "sí, ¡definitivamente!"  else (if (result == 2) "Es probable que suceda." else "Lo siento, pero mejor no.")
                }
            }
            "advice" -> {
                if(tematica == "love"){
                    binding.datos.text = "Debes actuar "+lista[0].loveAdvice.inicio+" y "+lista[1].loveAdvice.nudo+" para lograr "+lista[2].loveAdvice.final+"."
                }
                if(tematica == "money"){
                    binding.datos.text = "Debes actuar "+lista[0].moneyAdvice.inicio+" y "+lista[1].moneyAdvice.nudo+" para lograr "+lista[2].moneyAdvice.final+"."
                }
                if(tematica == "work"){
                    binding.datos.text = "Debes actuar "+lista[0].workAdvice.inicio+" y "+lista[1].workAdvice.nudo+" para lograr "+lista[2].workAdvice.final+"."
                }
                if(tematica == "friends"){
                    binding.datos.text = "Debes actuar "+lista[0].friendsAdvice.inicio+" y "+lista[1].friendsAdvice.nudo+" para lograr "+lista[2].friendsAdvice.final+"."
                }
                if(tematica == "spirituality"){
                    binding.datos.text = "Debes actuar "+lista[0].spiritualityAdvice.inicio+" y "+lista[1].spiritualityAdvice.nudo+" para lograr "+lista[2].spiritualityAdvice.final+"."

                }
            }
            "future" -> {
                if(tematica == "love"){
                    binding.datos.text = "Una relación "+lista[0].loveFuture.inicio+" obtendrá "+lista[1].loveFuture.nudo+" y el resultado será "+lista[2].loveFuture.final+"."
                }
                if(tematica == "money"){
                    binding.datos.text = lista[0].moneyFuture.inicio+" que "+lista[1].moneyFuture.nudo+" y el resultado será "+lista[2].moneyFuture.final+"."
                }
                if(tematica == "work"){
                    binding.datos.text = lista[0].workFuture.inicio+" que "+lista[1].workFuture.nudo+" y el resultado será "+lista[2].workFuture.final+"."
                }
                if(tematica == "friends"){
                    binding.datos.text = "Una amistad "+lista[0].friendsFuture.inicio+" que "+lista[1].friendsFuture.nudo+" y el resultado será "+lista[2].friendsFuture.final+"."
                }
                if(tematica == "spirituality"){
                    binding.datos.text = "Estás "+lista[0].spiritualityFuture.inicio+", debes cruzar "+lista[1].spiritualityFuture.nudo+" y el resultado será "+lista[2].spiritualityFuture.final+"."
                }
            }
            "special" -> {
                if(tematica == "love"){
                    binding.datos.text = "Siente "+lista[0].loveSpecial.inicio+", piensa "+lista[1].loveSpecial.nudo+" y actuará contigo "+lista[2].loveSpecial.final+"."
                }
                if(tematica == "money"){
                    binding.datos.text = "El primer camino "+lista[0].moneySpecial.inicio+", el segundo camino "+lista[1].moneySpecial.nudo+". El tarot te aconseja "+lista[2].moneySpecial.final+"."
                }
                if(tematica == "work"){
                    binding.datos.text = "El primer camino "+lista[0].workSpecial.inicio+", el segundo camino "+lista[1].workSpecial.nudo+". El tarot te aconseja "+lista[2].workSpecial.final+"."
                }
                if(tematica == "friends"){
                    binding.datos.text = "Siente "+lista[0].friendsSpecial.inicio+", piensa "+lista[1].friendsSpecial+" y actuará contigo "+lista[2].friendsSpecial+"."
                }
                if(tematica == "spirituality"){
                    binding.datos.text = "Tu misión ahora es "+lista[0].spiritualitySpecial.inicio+", transitas en él "+lista[1].spiritualitySpecial.nudo+" y al final encontarás "+lista[2].spiritualitySpecial.final+"."
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