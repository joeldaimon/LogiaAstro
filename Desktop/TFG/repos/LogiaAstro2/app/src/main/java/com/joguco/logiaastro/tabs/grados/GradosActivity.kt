package com.joguco.logiaastro.tabs.grados

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.ActivityGradosBinding

class GradosActivity : AppCompatActivity() {
    //Binding
    private lateinit var binding: ActivityGradosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityGradosBinding.inflate(layoutInflater).also { binding = it }.root)

        initListeners()
    }

    /*
    * Iniciando listeners
     */
    private fun initListeners() {
        binding.ivAries.setOnClickListener{
            binding.tvNumberOne.text = "1"
            binding.tvROne.text = getDegreeMeaning(1)
            binding.tvNumberTwo.text = "13"
            binding.tvRTwo.text = getDegreeMeaning(13)
            binding.tvNumberThree.text = "25"
            binding.tvRThree.text = getDegreeMeaning(25)
        }
        binding.ivTauro.setOnClickListener{
            binding.tvNumberOne.text = "2"
            binding.tvROne.text = getDegreeMeaning(2)
            binding.tvNumberTwo.text = "14"
            binding.tvRTwo.text = getDegreeMeaning(14)
            binding.tvNumberThree.text = "26"
            binding.tvRThree.text = getDegreeMeaning(26)
        }
        binding.ivGeminis.setOnClickListener{
            binding.tvNumberOne.text = "3"
            binding.tvROne.text = getDegreeMeaning(3)
            binding.tvNumberTwo.text = "15"
            binding.tvRTwo.text = getDegreeMeaning(15)
            binding.tvNumberThree.text = "27"
            binding.tvRThree.text = getDegreeMeaning(27)
        }
        binding.ivCancer.setOnClickListener{
            binding.tvNumberOne.text = "4"
            binding.tvROne.text = getDegreeMeaning(4)
            binding.tvNumberTwo.text = "16"
            binding.tvRTwo.text = getDegreeMeaning(16)
            binding.tvNumberThree.text = "28"
            binding.tvRThree.text = getDegreeMeaning(28)
        }
        binding.ivLeo.setOnClickListener{
            binding.tvNumberOne.text = "5"
            binding.tvROne.text = getDegreeMeaning(5)
            binding.tvNumberTwo.text = "17"
            binding.tvRTwo.text = getDegreeMeaning(17)
            binding.tvNumberThree.text = "29"
            binding.tvRThree.text = getDegreeMeaning(29)
        }
        binding.ivVirgo.setOnClickListener{
            binding.tvNumberOne.text = "6"
            binding.tvROne.text = getDegreeMeaning(6)
            binding.tvNumberTwo.text = "18"
            binding.tvRTwo.text = getDegreeMeaning(18)
            binding.tvNumberThree.text = ""
            binding.tvRThree.text = ""
        }
        binding.ivLibra.setOnClickListener{
            binding.tvNumberOne.text = "7"
            binding.tvROne.text = getDegreeMeaning(7)
            binding.tvNumberTwo.text = "19"
            binding.tvRTwo.text = getDegreeMeaning(19)
            binding.tvNumberThree.text = ""
            binding.tvRThree.text = ""
        }
        binding.ivEscorpio.setOnClickListener{
            binding.tvNumberOne.text = "8"
            binding.tvROne.text = getDegreeMeaning(8)
            binding.tvNumberTwo.text = "20"
            binding.tvRTwo.text = getDegreeMeaning(20)
            binding.tvNumberThree.text = ""
            binding.tvRThree.text = ""
        }
        binding.ivSagitario.setOnClickListener{
            binding.tvNumberOne.text = "9"
            binding.tvROne.text = getDegreeMeaning(9)
            binding.tvNumberTwo.text = "21"
            binding.tvRTwo.text = getDegreeMeaning(21)
            binding.tvNumberThree.text = ""
            binding.tvRThree.text = ""
        }
        binding.ivCapri.setOnClickListener{
            binding.tvNumberOne.text = "10"
            binding.tvROne.text = getDegreeMeaning(10)
            binding.tvNumberTwo.text = "22"
            binding.tvRTwo.text = getDegreeMeaning(22)
            binding.tvNumberThree.text = ""
            binding.tvRThree.text = ""
        }
        binding.ivAcuario.setOnClickListener{
            binding.tvNumberOne.text = "11"
            binding.tvROne.text = getDegreeMeaning(11)
            binding.tvNumberTwo.text = "23"
            binding.tvRTwo.text = getDegreeMeaning(23)
            binding.tvNumberThree.text = ""
            binding.tvRThree.text = ""
        }
        binding.ivPiscis.setOnClickListener{
            binding.tvNumberOne.text = "12"
            binding.tvROne.text = getDegreeMeaning(12)
            binding.tvNumberTwo.text = "24"
            binding.tvRTwo.text = getDegreeMeaning(24)
            binding.tvNumberThree.text = ""
            binding.tvRThree.text = ""
        }
    }


    /*
    * Obteniendo significado de cada grado
    * @param    degree
    * @return   String
     */
    private fun getDegreeMeaning(degree: Int): String{
        return when(degree){
            1 -> { getText(R.string.grados_one).toString() }
            2 -> { getText(R.string.grados_two).toString() }
            3 -> { getText(R.string.grados_three).toString() }
            4 -> { getText(R.string.grados_four).toString() }
            5 -> { getText(R.string.grados_five).toString() }
            6 -> { getText(R.string.grados_six).toString() }
            7 -> { getText(R.string.grados_seven).toString() }
            8 -> { getText(R.string.grados_eight).toString() }
            9 -> { getText(R.string.grados_nine).toString() }
            10 -> { getText(R.string.grados_ten).toString() }
            11 -> { getText(R.string.grados_eleven).toString() }
            12 -> { getText(R.string.grados_twelve).toString() }
            13 -> { getText(R.string.grados_thirteen).toString() }
            14 -> { getText(R.string.grados_fourteen).toString() }
            15 -> { getText(R.string.grados_fourteen).toString() }
            16 -> { getText(R.string.grados_sixteen).toString() }
            17 -> { getText(R.string.grados_seventeen).toString() }
            18 -> { getText(R.string.grados_eightteen).toString() }
            19 -> { getText(R.string.grados_nineteen).toString() }
            20 -> { getText(R.string.grados_twenty).toString() }
            21 -> { getText(R.string.grados_twentyone).toString() }
            22 -> { getText(R.string.grados_twentytwo).toString() }
            23 -> { getText(R.string.grados_twentythree).toString() }
            24 -> { getText(R.string.grados_twentyfour).toString() }
            25 -> { getText(R.string.grados_twentyfive).toString() }
            26 -> { getText(R.string.grados_twentysix).toString() }
            27 -> { getText(R.string.grados_twentyseven).toString() }
            28 -> { getText(R.string.grados_twentyeigth).toString() }
            29 -> { getText(R.string.grados_twentynine).toString() }
            else -> { "Error." }
        }
    }
}