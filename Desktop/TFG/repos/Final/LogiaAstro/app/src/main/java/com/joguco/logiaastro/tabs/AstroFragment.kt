package com.joguco.logiaastro.tabs

import android.content.Intent
import android.icu.text.DecimalFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.joguco.logiaastro.R
import com.joguco.logiaastro.database.entities.UserEntity
import com.joguco.logiaastro.databinding.FragmentAstroBinding
import com.joguco.logiaastro.tabs.astrology.*
import com.joguco.logiaastro.ui.ComprasActivity
import com.joguco.logiaastro.ui.MainActivity
import com.joguco.logiaastro.ui.Perfiles.BuscadorUserActivity
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
    //Atributos
    private lateinit var binding: FragmentAstroBinding
    private lateinit var chart: AstrologyDataResponse
    private lateinit var chartTransits: AstrologyDataResponse
    private lateinit var user: UserEntity
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


        initListeners()

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

        //Carta del usuario
        var hourArr = user.hour.split(":")
        val request = ChartRequest(
            year = user.year,
            month = user.month,
            date = user.day,
            hours = hourArr[0].toInt(),
            minutes = hourArr[1].toInt(),
            seconds = 0,
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

        }
        binding.fabSearch.setOnClickListener{

        }
        binding.fabInfo.setOnClickListener{
            Toast.makeText(requireContext(), "PRONTO HABRÁ MÁS FUNCIONALIDADES", Toast.LENGTH_LONG).show()
        }
    }

    /*
    * Función que procesa la carta natal de hoy
     */
    private fun processTransits() {
        if(chartTransits != null){
            while(true){
                if(getSignName(chartTransits.chartData[0].venus.sign) == "Piscis" ||
                    getSignName(chartTransits.chartData[0].venus.sign) == "Libra"){
                    makeEvent("Hoy buena suerte en el amor")
                    break
                }
                if(getSignName(chartTransits.chartData[0].moon.sign) == "Escorpio" ||
                    getSignName(chartTransits.chartData[0].moon.sign) == "Aries"){
                    makeEvent("Hoy emociones irascibles, ¡cuidado!")
                    break
                }
                if(getSignName(chartTransits.chartData[0].mercury.sign) == "Acuario" ||
                    getSignName(chartTransits.chartData[0].mercury.sign) == "Géminis"){
                    makeEvent("Buen día para investigar, estudiar o avanzar en tu carrera.")
                    break
                }
                if(getSignName(chartTransits.chartData[0].mars.sign) == "Cáncer" ||
                    getSignName(chartTransits.chartData[0].mars.sign) == "Leo"){
                    makeEvent("Evita los enfados tontos, ¡controla!")
                    break
                }
                if(getSignName(chartTransits.chartData[0].jupiter.sign) == "Virgo" ||
                    getSignName(chartTransits.chartData[0].jupiter.sign) == "Tauro"){
                    makeEvent("Céntrate más en lo espiritual, ¡estás en lo material!")
                    break
                }
                if(getSignName(chartTransits.chartData[0].saturn.sign) == "Aries" ||
                    getSignName(chartTransits.chartData[0].saturn.sign) == "Cáncer"){
                    makeEvent("Te sientes víctima del destino, ¡sana!")
                    break
                }
                makeEvent("Puedes crear cartas en los botones inferiores")
                break
            }

        }
    }

    /*
    * Función que genera un evento
    * @param    msg
     */
    private fun makeEvent(msg: String){
        binding.tvEvent.text = msg
    }


    /*
    * Función que inicia Retrofit
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
    * Función que envia postRequest
     */
    private fun getNatalChartInfo(request: ChartRequest, user: Boolean) {
        val call =  getRetrofit().create(AstrologyApi::class.java).getPlanets(request)
        call.enqueue(object : retrofit2.Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    if(user){
                        chart = parseJsonResponse(response.body().toString())
                        createUI(chart,"sun")
                        addChartToList(chart)
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
    * Función que añade una carta a la lista del usuario
    * @param    chart
     */
    private fun addChartToList(chart: AstrologyDataResponse) {
        user.chartList.add(chart.chartData[0])
    }

    /*
    * Función que transforma JSON en AstrologyDataResponse
    * @param    json
    * @return   AstrologyDataResponse
     */
    fun parseJsonResponse(json: String): AstrologyDataResponse {
        return Gson().fromJson(json, AstrologyDataResponse::class.java)
    }

    /*
    * Función que gestiona datos de la carta natal
    * @param    chart, name
     */
    private fun createUI(chart: AstrologyDataResponse, name: String) {
        val df = DecimalFormat("#.##")

        when(name){
            "ascendant" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.aquamarine))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.aquamarine))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.aquamarine), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.ascendant)
                binding.tvNombre.text = getText(R.string.astrology_asc).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.chartData[0].ascendant?.sign)
                binding.tvGrado.text = df.format(chart.chartData[0].ascendant?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.chartData[0].ascendant?.isRetro == "true") "sí" else "no"
            }
            "sun" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.beige))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.beige))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.beige), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.solplaneta)
                binding.tvNombre.text = getText(R.string.astrology_sun).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.chartData[0].sun?.sign)
                binding.tvGrado.text = df.format(chart.chartData[0].sun?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.chartData[0].sun?.isRetro == "true") "sí" else "no"
            }
            "moon" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_blue))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_blue))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_blue), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.lunaplaneta)
                binding.tvNombre.text = getText(R.string.astrology_moon).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.chartData[0].moon?.sign)
                binding.tvGrado.text = df.format(chart.chartData[0].moon?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.chartData[0].moon?.isRetro == "true") "sí" else "no"
            }
            "mars" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_salmon))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_salmon))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_salmon), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.marte)
                binding.tvNombre.text = getText(R.string.astrology_mars).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.chartData[0].mars?.sign)
                binding.tvGrado.text = df.format(chart.chartData[0].mars?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.chartData[0].mars?.isRetro == "true") "sí" else "no"
            }
            "mercury" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_orange))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_orange))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_orange), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.mercurio)
                binding.tvNombre.text = getText(R.string.astrology_mercury).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.chartData[0].mercury?.sign)
                binding.tvGrado.text = df.format(chart.chartData[0].mercury?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.chartData[0].mercury?.isRetro == "true") "sí" else "no"
            }
            "jupiter" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_green))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_green))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_green), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.jupiter)
                binding.tvNombre.text = getText(R.string.astrology_jupiter).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.chartData[0].jupiter?.sign)
                binding.tvGrado.text = df.format(chart.chartData[0].jupiter?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.chartData[0].jupiter?.isRetro == "true") "sí" else "no"
            }
            "venus" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.rosa_palo))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.rosa_palo))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.rosa_palo), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.venus)
                binding.tvNombre.text = getText(R.string.astrology_venus).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.chartData[0].venus?.sign)
                binding.tvGrado.text = df.format(chart.chartData[0].venus?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.chartData[0].venus?.isRetro == "true") "sí" else "no"
            }
            "saturn" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_green))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_green))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_green), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.saturno)
                binding.tvNombre.text = getText(R.string.astrology_saturn).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.chartData[0].saturn?.sign)
                binding.tvGrado.text = df.format(chart.chartData[0].saturn?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.chartData[0].saturn?.isRetro == "true") "sí" else "no"
            }
            "rahu" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_brown))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_brown))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_brown), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.nodonorte)
                binding.tvNombre.text = getText(R.string.astrology_rahu).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.chartData[0].rahu?.sign)
                binding.tvGrado.text = df.format(chart.chartData[0].rahu?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.chartData[0].rahu?.isRetro == "true") "sí" else "no"
            }
            "ketu" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.blue_gray))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.blue_gray))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.blue_gray), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.nodosur)
                binding.tvNombre.text = getText(R.string.astrology_ketu).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.chartData[0].ketu?.sign)
                binding.tvGrado.text = df.format(chart.chartData[0].ketu?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.chartData[0].ketu?.isRetro == "true") "sí" else "no"
            }
            "uranus" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_green))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_green))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.light_green), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.urano)
                binding.tvNombre.text = getText(R.string.astrology_uranus).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.chartData[0].uranus?.sign)
                binding.tvGrado.text = df.format(chart.chartData[0].uranus?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.chartData[0].uranus?.isRetro == "true") "sí" else "no"
            }
            "neptune" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_blue))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_blue))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_blue), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.neptuno)
                binding.tvNombre.text = getText(R.string.astrology_neptune).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.chartData[0].neptune?.sign)
                binding.tvGrado.text = df.format(chart.chartData[0].neptune?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.chartData[0].neptune?.isRetro == "true") "sí" else "no"
            }
            "pluto" -> {
                binding.frAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_violet))
                binding.clAstrology.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_violet))
                binding.ivPlaneta.setColorFilter(ContextCompat.getColor(requireActivity().applicationContext,R.color.pastel_violet), android.graphics.PorterDuff.Mode.MULTIPLY);
                binding.ivPlaneta.setImageResource(R.drawable.pluton)
                binding.tvNombre.text = getText(R.string.astrology_pluto).toString().uppercase()
                binding.tvSigno.text = getSignName(chart.chartData[0].pluto?.sign)
                binding.tvGrado.text = df.format(chart.chartData[0].pluto?.normDegree).toString()
                binding.tvIsRetro.text = if(chart.chartData[0].pluto?.isRetro == "true") "sí" else "no"
            }
        }
    }

    /*
    * Función que gestiona datos de la carta natal
    * @param    sign
    * @return   string
     */
    private fun getSignName(sign: Int?): String {
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