package com.joguco.logiaastro.tabs

import android.content.Intent
import android.icu.text.DecimalFormat
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.FragmentAstroBinding
import com.joguco.logiaastro.model.astrology.NatalChart
import com.joguco.logiaastro.model.astrology.Planet
import com.joguco.logiaastro.model.astrology.AstrologyApi
import com.joguco.logiaastro.model.astrology.AstrologyDataResponse
import com.joguco.logiaastro.model.astrology.ChartRequest
import com.joguco.logiaastro.model.astrology.Settings
import com.joguco.logiaastro.ui.ComprasActivity
import com.joguco.logiaastro.ui.MainActivity
import com.joguco.logiaastro.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.time.LocalDateTime

/**
 * ASTROLOGÍA
 */
class AstroFragment : Fragment() {
    //Binding
    private lateinit var binding: FragmentAstroBinding

    //Carta natal actual
    private lateinit var chart: NatalChart

    //Transitos de hoy
    private lateinit var chartTransits: AstrologyDataResponse

    //Nombre de usuario
    private lateinit var user: String

    //Firebird database
    private val db = Firebase.firestore

    //ViewModel
    private lateinit var userVM: ViewModel

    //Planetas de la carta
    private val planets: List<String> = listOf(
        "ascendant","sun","moon","mars","mercury","jupiter",
        "venus","saturn","rahu","ketu","uranus","neptune","pluto")

    private var contador: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflando layout
        return FragmentAstroBinding.inflate(inflater, container, false).also { binding = it}.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity?)?.setActionBarTitle("Astrología")

        //Obtenemos usuario
        user = (activity as MainActivity?)?.getUser()!!

        //Iniciamos VM - Lo usaremos para validaciones
        userVM = ViewModelProvider(this)[ViewModel::class.java]

        initListeners()

        initUser()

        //Carta de tránsitos
        val transits = ChartRequest(
            year = LocalDateTime.now().year,
            month = LocalDateTime.now().monthValue,
            date = LocalDateTime.now().dayOfMonth,
            hours = LocalDateTime.now().hour,
            minutes = LocalDateTime.now().minute,
            seconds = LocalDateTime.now().second,
            latitude = 39.46975,
            longitude = -0.37739,
            timezone = 1.0,
            settings = Settings(
                observation_point = "topocentric",
                ayanamsha = "sayana"
            )
        )

        getNatalChartInfo(transits,false)
    }

    /*
    * Método que inicia usuarios
     */
    private fun initUser(){
        CoroutineScope(Dispatchers.Main).launch{
            db.collection("users").document(user).get()
                .addOnSuccessListener { user ->
                    //Coordenadas
                    val coder = Geocoder(requireContext())
                    var strAddress = user.get("place").toString()
                    var address = coder.getFromLocationName(strAddress, 5)
                    var longitud = if(address?.get(0)?.latitude != null) address?.get(0)?.latitude else 39.46975
                    var latitud = if(address?.get(0)?.longitude != null) address?.get(0)?.latitude else -0.37739

                    //Carta del usuario
                    var hourArr = user.get("hour").toString().split(":")
                    var timezone = user.get("timezone") as Double
                    val request = ChartRequest(
                        year = (user.get("year").toString()).toInt(),
                        month = (user.get("month").toString()).toInt(),
                        date = (user.get("day").toString()).toInt(),
                        hours = hourArr[0].toInt(),
                        minutes = hourArr[1].toInt(),
                        seconds = 0,
                        latitude = longitud!!,
                        longitude = latitud!!,
                        timezone = timezone,
                        settings = Settings(
                            observation_point = "topocentric",
                            ayanamsha = "sayana"
                        )
                    )
                    getNatalChartInfo(request,true)
                }
                .addOnFailureListener { exception ->
                    Log.i("JOELDAIMON", "Error: ", exception)
                } .await()
        }
    }

    /*
    * Método que inicia Listeners
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListeners() {
        binding.ivIzq.setOnClickListener{
            if(contador == 0){
                contador = planets.size-1
            } else {
                contador--
            }
            createUI(chart,planets[contador])
        }
        binding.ivDer.setOnClickListener{
            if(contador == (planets.size -1)){
                contador = 0
            } else {
                contador++
            }
            createUI(chart,planets[contador])
        }
        binding.btbCompra.setOnClickListener{
            activity?.let{
                val intent = Intent (it, ComprasActivity::class.java).apply{
                    putExtra(ComprasActivity.KEY_EXTRA_COMPRA, "astrology")
                }
                it.startActivity(intent)
            }
        }
        binding.btnEvents.setOnClickListener{
            val request = ChartRequest(
                year = LocalDateTime.now().year,
                month = LocalDateTime.now().monthValue,
                date = LocalDateTime.now().dayOfMonth,
                hours = LocalDateTime.now().hour,
                minutes = LocalDateTime.now().minute,
                seconds = LocalDateTime.now().second,
                latitude = 39.46975,
                longitude = -0.37739,
                timezone = 1.0,
                settings = Settings(
                    observation_point = "topocentric",
                    ayanamsha = "sayana"
                )
            )

            getNatalChartInfo(request,true)
        }

        binding.fabAdd.setOnClickListener{
            binding.cvAddChart.visibility = View.VISIBLE
            binding.cvPlanet.visibility = View.GONE
            binding.llChartLists.visibility = View.GONE
        }
        binding.fabSearch.setOnClickListener{
            binding.llChartLists.visibility = View.VISIBLE
            binding.cvPlanet.visibility = View.GONE
            binding.cvAddChart.visibility = View.GONE
            initList()
        }
        binding.fabInfo.setOnClickListener{
            binding.cvAddChart.visibility = View.GONE
            binding.cvPlanet.visibility = View.VISIBLE
            binding.llChartLists.visibility = View.GONE
            Toast.makeText(requireContext(), R.string.util_pronto, Toast.LENGTH_LONG).show()
        }

        binding.btnAdd.setOnClickListener{
            //Validamos
            if(validateAddChart()){
                addChart()
                binding.llChartLists.visibility = View.GONE
                binding.cvAddChart.visibility = View.GONE
                binding.cvPlanet.visibility = View.VISIBLE
            }
        }
    }

    /*
    * Método que valida datos de la carta a añadir
    * @retuerns     Boolean
     */
    private fun validateAddChart(): Boolean {
        var validated = true
        if(binding.etTitle.text.toString() == ""){
            binding.etTitle.error = getText(R.string.obligado_title)
            validated = false
        } else if(!userVM.isValidName(binding.etTitle.text.toString())){
            binding.etTitle.error = getText(R.string.validation_title)
            validated = false
        }
        if(!userVM.isValidDate(binding.etFecha.text.toString()) || binding.etFecha.text.toString() == ""){
            binding.etFecha.error = getText(R.string.account_date_error)
            validated = false
        }
        if(binding.etPlace.text.toString() == ""){
            binding.etPlace.error = getText(R.string.obligado_place)
            validated = false
        } else if(!userVM.isValidName(binding.etPlace.text.toString())){
            binding.etPlace.error = getText(R.string.validation_place)
            validated = false
        }
        if(!userVM.isValidHour(binding.etHour.text.toString()) || binding.etHour.text.toString() == ""){
            binding.etHour.error = getText(R.string.validation_hour)
            validated = false
        }
        if(!userVM.isValidHour(binding.etHour.text.toString()) || binding.etHour.text.toString() == ""){
            binding.etHour.error = getText(R.string.validation_hour)
            validated = false
        }

        return validated
    }

    /*
    * Método que añade carta a base de datos de Firebird
    */
    private fun addChart() {
        //Coordenadas
        val coder = Geocoder(requireContext())
        var strAddress = binding.etPlace.text.toString()
        var address = coder.getFromLocationName(strAddress, 5)
        var longitud = if(address?.get(0)?.latitude != null) address?.get(0)?.latitude else 39.46975
        var latitud = if(address?.get(0)?.longitude != null) address?.get(0)?.latitude else -0.37739

        //Carta de tránsitos
        var arr = binding.etFecha.text.toString().split("/")
        var arrHour = binding.etHour.text.toString().split(":")
        val request = ChartRequest(
            year = arr[2].toInt(),
            month = arr[1].toInt(),
            date = arr[0].toInt(),
            hours = arrHour[0].toInt(),
            minutes = arrHour[1].toInt(),
            seconds = 0,
            latitude = latitud!!,
            longitude = longitud!!,
            timezone = binding.etTimezone.text.toString().toDouble(),
            settings = Settings(
                observation_point = "topocentric",
                ayanamsha = "sayana"
            )
        )

        val call =  getRetrofit().create(AstrologyApi::class.java).getPlanets(request)
        call.enqueue(object : retrofit2.Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    var chartResponse = parseJsonResponse(response.body().toString())
                    chart = chartResponse.chartData[0]
                    addChartToList(chartResponse)
                    createUI(chart, "sun")
                }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                println("Failure: ${t.message}")
            }
        })
    }

    /*
    * Método que inicia la Lista de cartas del Usuario
    */
    private fun initList(){
        var chartList = ArrayList<String>()

        CoroutineScope(Dispatchers.Main).launch{
            db.collection("natalcharts").get()
                .addOnSuccessListener { document ->
                    for(doc in document){
                        if(doc.get("username").toString() == user){
                            if(doc.get("title").toString() == ""){
                                chartList.add("${doc.id}- "+user)
                            } else {
                                chartList.add("${doc.id}- "+doc.get("title").toString())
                            }
                        }
                    }
                    loadList(chartList)
                }
                .addOnFailureListener { exception ->
                    Log.i("LOGIA-ASTRO", "Error al cargar cartas natales: ", exception)
                } .await()
        }
    }

    /*
    * Método que procesa la lista
    * @param    chartList
     */
    private fun loadList(chartList: ArrayList<String>) {
        val chartsAdapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_list_item_1, chartList)
        binding.lvCharts.adapter = chartsAdapter

        // Listener
        binding.lvCharts.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as String
            var arr = selectedItem.split("-")

            db.collection("natalcharts").document(arr[0]).get().addOnSuccessListener {
                var planetList: ArrayList<Planet> = arrayListOf()
                var pAsc: HashMap<String, Any?> = it.get("ascendant") as HashMap<String, Any?>
                loadPlanet(pAsc)?.let { it1 -> planetList.add(it1) }
                var pSun: HashMap<String, Any?> = it.get("sun") as HashMap<String, Any?>
                loadPlanet(pSun)?.let { it1 -> planetList.add(it1) }
                var pMoon: HashMap<String, Any?> = it.get("moon") as HashMap<String, Any?>
                loadPlanet(pMoon)?.let { it1 -> planetList.add(it1) }
                var pMars: HashMap<String, Any?> = it.get("mars") as HashMap<String, Any?>
                loadPlanet(pMars)?.let { it1 -> planetList.add(it1) }
                var pMerc: HashMap<String, Any?> = it.get("mercury") as HashMap<String, Any?>
                loadPlanet(pMerc)?.let { it1 -> planetList.add(it1) }
                var pJupiter: HashMap<String, Any?> = it.get("jupiter") as HashMap<String, Any?>
                loadPlanet(pJupiter)?.let { it1 -> planetList.add(it1) }
                var pVenus: HashMap<String, Any?> = it.get("venus") as HashMap<String, Any?>
                loadPlanet(pVenus)?.let { it1 -> planetList.add(it1) }
                var pSaturn: HashMap<String, Any?> = it.get("saturn") as HashMap<String, Any?>
                loadPlanet(pSaturn)?.let { it1 -> planetList.add(it1) }
                var pRahu: HashMap<String, Any?> = it.get("rahu") as HashMap<String, Any?>
                loadPlanet(pRahu)?.let { it1 -> planetList.add(it1) }
                var pKetu: HashMap<String, Any?> = it.get("ketu") as HashMap<String, Any?>
                loadPlanet(pKetu)?.let { it1 -> planetList.add(it1) }
                var pUranus: HashMap<String, Any?> = it.get("uranus") as HashMap<String, Any?>
                loadPlanet(pUranus)?.let { it1 -> planetList.add(it1) }
                var pNeptune: HashMap<String, Any?> = it.get("neptune") as HashMap<String, Any?>
                loadPlanet(pNeptune)?.let { it1 -> planetList.add(it1) }
                var pPluto: HashMap<String, Any?> = it.get("pluto") as HashMap<String, Any?>
                loadPlanet(pPluto)?.let { it1 -> planetList.add(it1) }

                chart = NatalChart(
                    planetList[0],planetList[1],planetList[2],planetList[3],planetList[4],planetList[5],planetList[6],
                    planetList[7],planetList[8],planetList[9],planetList[10],planetList[11],planetList[12]
                )

                createUI(chart, "sun")
            }

            //Cargamos lista
            binding.llChartLists.visibility = View.GONE
            binding.cvPlanet.visibility = View.VISIBLE

        }
    }

    /*
    * Método que procesa un mapa para crear un planeta
    * @param    map
    * @return   planet
     */
    private fun loadPlanet(map: HashMap<String, Any?>): Planet? {
        var name = ""
        var degree = 0.1
        var normDegree = 0.1
        var isRetro = "false"
        var sign = 1
        map.forEach { entry ->
            when (entry.key) {
                "name" -> {
                    name = entry.value.toString()
                }
                "degree" -> {
                    degree = entry.value.toString().toDouble()
                }
                "normDegree" -> {
                    normDegree = entry.value.toString().toDouble()
                }
                "retro" -> {
                    isRetro = entry.value.toString()
                }
                "sign" -> {
                    sign = entry.value.toString().toInt()
                }
            }
        }
        return Planet(name, degree, normDegree, isRetro, sign)
    }

    /*
    * Método que procesa la carta natal de hoy
     */
    private fun processTransits() {
        if(chartTransits != null){
            while(true){
                if(getSignName(chartTransits.chartData[0].venus.sign) == "Piscis" ||
                    getSignName(chartTransits.chartData[0].venus.sign) == "Libra"){
                    makeEvent(getText(R.string.astroevent_love) as String)
                    break
                }
                if(getSignName(chartTransits.chartData[0].moon.sign) == "Escorpio" ||
                    getSignName(chartTransits.chartData[0].moon.sign) == "Aries"){
                    makeEvent(getText(R.string.astroevent_ira) as String)
                    break
                }
                if(getSignName(chartTransits.chartData[0].mercury.sign) == "Acuario" ||
                    getSignName(chartTransits.chartData[0].mercury.sign) == "Géminis"){
                    makeEvent(getText(R.string.astroevent_estudiar) as String)
                    break
                }
                if(getSignName(chartTransits.chartData[0].mars.sign) == "Cáncer" ||
                    getSignName(chartTransits.chartData[0].mars.sign) == "Leo"){
                    makeEvent(getText(R.string.astroevent_enfado) as String)
                    break
                }
                if(getSignName(chartTransits.chartData[0].jupiter.sign) == "Virgo" ||
                    getSignName(chartTransits.chartData[0].jupiter.sign) == "Tauro"){
                    makeEvent(getText(R.string.astroevent_espiritual) as String)
                    break
                }
                if(getSignName(chartTransits.chartData[0].saturn.sign) == "Aries" ||
                    getSignName(chartTransits.chartData[0].saturn.sign) == "Cáncer"){
                    makeEvent(getText(R.string.astroevent_sanar) as String)
                    break
                }
                makeEvent(getText(R.string.astroevent) as String)
                break
            }

        }
    }

    /*
    * Método que genera un evento
    * @param    msg
     */
    private fun makeEvent(msg: String){
        binding.tvEvent.text = msg
    }

    /*
    * Método que inicia Retrofit
    * @return   Retrofit
     */
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://json.freeastrologyapi.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /*
    * Método que envia postRequest
     */
    private fun getNatalChartInfo(request: ChartRequest, userChart: Boolean) {
        val call =  getRetrofit().create(AstrologyApi::class.java).getPlanets(request)
        call.enqueue(object : retrofit2.Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    if(userChart){
                        var chartResponse = parseJsonResponse(response.body().toString())
                        chart = chartResponse.chartData[0]
                        createUI(chart,"sun")


                        CoroutineScope(Dispatchers.Main).launch{
                            db.collection("users").document(user).get()
                                .addOnSuccessListener {
                                    var lista = it.get("chartList") as ArrayList<Any>
                                  if(lista.size == 0){
                                      addChartToList(chartResponse)
                                  }

                                    var str = getSignName(chart.sun.sign)
                                    db.collection("users").document(user).update(
                                        hashMapOf(
                                            "image" to str,
                                        ) as Map<String, String>
                                    )
                                }
                                .addOnFailureListener { exception ->
                                    Log.i("LOGIA-ASTRO", "Error sacando documento: ", exception)
                                } .await()
                        }
                    } else {
                        chartTransits = parseJsonResponse(response.body().toString())
                        processTransits()
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                println("Failure: ${t.message}")
            }

        })

    }

    /*
    * Método que añade una carta a la lista del usuario
    * @param    chart
     */
    private fun addChartToList(chart: AstrologyDataResponse) {
        var aux = 0
        CoroutineScope(Dispatchers.Main).launch{
           db.collection("natalcharts").get()
               .addOnSuccessListener { documents ->
                   for(doc in documents){
                       if(doc.id.toInt() > aux){
                           aux = doc.id.toInt()
                       }
                   }
                   aux++
                   db.collection("natalcharts").document(aux.toString()).set(
                       hashMapOf(
                           "username" to user,
                           "title" to if(binding.etTitle.text.toString()!="") binding.etTitle.text.toString() else "carta natal de $user",
                           "ascendant" to chart.chartData[0].ascendant,
                           "sun" to chart.chartData[0].sun,
                           "moon" to chart.chartData[0].moon,
                           "mars" to chart.chartData[0].mars,
                           "mercury" to chart.chartData[0].mercury,
                           "jupiter" to chart.chartData[0].jupiter,
                           "venus" to chart.chartData[0].venus,
                           "saturn" to chart.chartData[0].saturn,
                           "rahu" to chart.chartData[0].rahu,
                           "ketu" to chart.chartData[0].ketu,
                           "uranus" to chart.chartData[0].uranus,
                           "neptune" to chart.chartData[0].neptune,
                           "pluto" to chart.chartData[0].pluto
                       )
                   )
               }
               .addOnFailureListener { exception ->
                   Log.i("LOGIA-ASTRO", "Error guardando carta: ", exception)
               } .await()

           db.collection("users").document(user).get()
               .addOnSuccessListener {
                   var astroLevel = it.get("astroLevel") as Long
                   astroLevel++
                   var lista = it.get("chartList") as ArrayList<String>
                   lista.add(aux.toString())
                   db.collection("users").document(user).update(
                       hashMapOf(
                           "chartList" to lista,
                           "astroLevel" to astroLevel
                       ) as Map<String, Any>
                   )
               }
               .addOnFailureListener { exception ->
                   Log.i("LOGIA-ASTRO", "Error al actualizar usuario: ", exception)
               } .await()

       }
    }

    /*
    * Método que transforma JSON en AstrologyDataResponse
    * @param    json
    * @return   AstrologyDataResponse
     */
    fun parseJsonResponse(json: String): AstrologyDataResponse {
        return Gson().fromJson(json, AstrologyDataResponse::class.java)
    }

    /*
    * Método que gestiona datos de la carta natal
    * @param    chart, name
     */
    private fun createUI(chart: NatalChart, name: String) {
        val df = DecimalFormat("#.##")

        when(name){
            "ascendant" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.aquamarine))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.aquamarine))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.aquamarine), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.ascendant)
                binding.tvNombre.text = getText(R.string.astrology_asc).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.ascendant?.sign)
                binding.tvGrado.text = df.format(chart.ascendant?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.ascendant?.isRetro == "true") getText(R.string.util_yes).toString() else getText(R.string.util_no).toString()
            }
            "sun" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.beige))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.beige))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.beige), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.solplaneta)
                binding.tvNombre.text = getText(R.string.astrology_sun).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.sun?.sign)
                binding.tvGrado.text = df.format(chart.sun?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.sun?.isRetro == "true") getText(R.string.util_yes).toString() else getText(R.string.util_no).toString()
            }
            "moon" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_blue))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_blue))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_blue), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.lunaplaneta)
                binding.tvNombre.text = getText(R.string.astrology_moon).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.moon?.sign)
                binding.tvGrado.text = df.format(chart.moon?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.moon?.isRetro == "true") getText(R.string.util_yes).toString() else getText(R.string.util_no).toString()
            }
            "mars" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_salmon))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_salmon))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_salmon), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.marte)
                binding.tvNombre.text = getText(R.string.astrology_mars).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.mars?.sign)
                binding.tvGrado.text = df.format(chart.mars?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.mars?.isRetro == "true") getText(R.string.util_yes).toString() else getText(R.string.util_no).toString()
            }
            "mercury" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_orange))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_orange))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_orange), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.mercurio)
                binding.tvNombre.text = getText(R.string.astrology_mercury).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.mercury?.sign)
                binding.tvGrado.text = df.format(chart.mercury?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.mercury?.isRetro == "true") getText(R.string.util_yes).toString() else getText(R.string.util_no).toString()
            }
            "jupiter" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_green))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_green))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_green), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.jupiter)
                binding.tvNombre.text = getText(R.string.astrology_jupiter).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.jupiter?.sign)
                binding.tvGrado.text = df.format(chart.jupiter?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.jupiter?.isRetro == "true") getText(R.string.util_yes).toString() else getText(R.string.util_no).toString()
            }
            "venus" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.rosa_palo))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.rosa_palo))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.rosa_palo), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.venus)
                binding.tvNombre.text = getText(R.string.astrology_venus).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.venus?.sign)
                binding.tvGrado.text = df.format(chart.venus?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.venus?.isRetro == "true") getText(R.string.util_yes).toString() else getText(R.string.util_no).toString()
            }
            "saturn" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_green))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_green))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_green), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.saturno)
                binding.tvNombre.text = getText(R.string.astrology_saturn).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.saturn?.sign)
                binding.tvGrado.text = df.format(chart.saturn?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.saturn?.isRetro == "true") getText(R.string.util_yes).toString() else getText(R.string.util_no).toString()
            }
            "rahu" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_brown))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_brown))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_brown), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.nodonorte)
                binding.tvNombre.text = getText(R.string.astrology_rahu).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.rahu?.sign)
                binding.tvGrado.text = df.format(chart.rahu?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.rahu?.isRetro == "true") getText(R.string.util_yes).toString() else getText(R.string.util_no).toString()
            }
            "ketu" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.blue_gray))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.blue_gray))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.blue_gray), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.nodosur)
                binding.tvNombre.text = getText(R.string.astrology_ketu).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.ketu?.sign)
                binding.tvGrado.text = df.format(chart.ketu?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.ketu?.isRetro == "true") getText(R.string.util_yes).toString() else getText(R.string.util_no).toString()
            }
            "uranus" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_green))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_green))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_green), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.urano)
                binding.tvNombre.text = getText(R.string.astrology_uranus).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.uranus?.sign)
                binding.tvGrado.text = df.format(chart.uranus?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.uranus?.isRetro == "true") getText(R.string.util_yes).toString() else getText(R.string.util_no).toString()
            }
            "neptune" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_blue))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_blue))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_blue), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.neptuno)
                binding.tvNombre.text = getText(R.string.astrology_neptune).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.neptune?.sign)
                binding.tvGrado.text = df.format(chart.neptune?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.neptune?.isRetro == "true") getText(R.string.util_yes).toString() else getText(R.string.util_no).toString()
            }
            "pluto" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_violet))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_violet))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_violet), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.pluton)
                binding.tvNombre.text = getText(R.string.astrology_pluto).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.pluto?.sign)
                binding.tvGrado.text = df.format(chart.pluto?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.pluto?.isRetro == "true") getText(R.string.util_yes).toString() else getText(R.string.util_no).toString()
            }
        }
    }

    /*
    * Método que gestiona datos de la carta natal
    * @param    sign
    * @return   string
     */
    fun getSignName(sign: Int?): String {
        if(sign != null){
            when(sign){
                1 -> return getText(R.string.astrology_aries).toString()
                2 -> return getText(R.string.astrology_taurus).toString()
                3 -> return getText(R.string.astrology_gemini).toString()
                4 -> return getText(R.string.astrology_cancer).toString()
                5 -> return getText(R.string.astrology_leo).toString()
                6 -> return getText(R.string.astrology_virgo).toString()
                7 -> return getText(R.string.astrology_libra).toString()
                8 -> return getText(R.string.astrology_scorpio).toString()
                9 -> return getText(R.string.astrology_sagittarius).toString()
                10 -> return getText(R.string.astrology_capri).toString()
                11 -> return getText(R.string.astrology_aquarius).toString()
                12 -> return getText(R.string.astrology_pisces).toString()
            }
        }
        return "Error"
    }
}