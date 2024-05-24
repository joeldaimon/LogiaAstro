package com.joguco.logiaastro.menu

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.joguco.logiaastro.R
import com.joguco.logiaastro.databinding.ActivityAccountBinding
import com.joguco.logiaastro.ui.MainActivity
import com.joguco.logiaastro.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class AccountActivity : AppCompatActivity() {
    //Binding
    private lateinit var binding: ActivityAccountBinding

    //User
    private lateinit var username: String

    //ViewModel
    private lateinit var userVM: ViewModel

    //Firebase base de datos
    private val db = Firebase.firestore

    //Subiendo imagen de perfil
    val pickMedia = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { uri ->
        if (uri != null) {
            val imageUri: Uri? = uri.data?.data
            val takeFlags = (intent.flags
                    and (Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION))

            if (imageUri != null) {
                contentResolver.takePersistableUriPermission(imageUri, takeFlags)
            }


            this@AccountActivity.grantUriPermission(
                "com.joguco.logiaastro.menu",
                imageUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION and
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION and
                        Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
            )
            binding.ivProfile.setImageURI(imageUri)

            db.collection("users").document(username).update(
                hashMapOf(
                    "image" to imageUri
                ) as Map<String, Uri>
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Inflando layout
        setContentView(ActivityAccountBinding.inflate(layoutInflater).also { binding = it }.root)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //Iniciamos VM - Lo usaremos para validaciones
        userVM = ViewModelProvider(this)[ViewModel::class.java]

        //Cogemos EXTRA
        username = intent?.getStringExtra(MainActivity.USER_KEY)!!

        //Metodos de inicio
        initListeners()
        initUserData()
    }

    /*
    * Método que inicia Listeners
    */
    private fun initListeners(){
        binding.ivProfile.setOnClickListener{
            val galleryIntent = Intent(Intent.ACTION_PICK)
            // here item is type of image
            galleryIntent.type = "image/*"
            pickMedia.launch(galleryIntent)
        }

        binding.btnPwd.setOnClickListener{
            binding.llCorreo.visibility = View.INVISIBLE
            binding.llFecha.visibility = View.INVISIBLE
            binding.llLugar.visibility = View.INVISIBLE
            binding.llHora.visibility = View.INVISIBLE
            binding.llPwdChange.visibility = View.VISIBLE
        }

        binding.btnSave.setOnClickListener{
            var cont = true
            if(binding.llPwdChange.isVisible){ //Updating pwd
                if(getPassword(binding.etPwdOld.text.toString())){
                    if(!binding.etPwdNew.text.isNotEmpty()){
                        binding.etPwdNew.error = getText(R.string.account_pwd)
                        cont = false
                    }
                } else {
                    binding.etPwdOld.error = getText(R.string.account_pwd_error)
                    cont = false
                }
                if(cont){
                    db.collection("users").document(username).update(
                        hashMapOf(
                            "password" to binding.etPwdNew.text.toString()
                        ) as Map<String, Any>
                    )
                }
                binding.llCorreo.visibility = View.VISIBLE
                binding.llFecha.visibility = View.VISIBLE
                binding.llLugar.visibility = View.VISIBLE
                binding.llHora.visibility = View.VISIBLE
                binding.llPwdChange.visibility = View.INVISIBLE
            } else { //Updating user with new data
                if(binding.etFecha.text.toString() != null){
                    if(!userVM.isValidDate(binding.etFecha.text.toString())){
                        binding.etFecha.error = getText(R.string.account_date_error)
                        cont = false
                    }
                } else{
                    binding.etFecha.error = getText(R.string.account_date)
                    cont = false
                }

                if(binding.etCorreo.text.toString() != null){
                    if(!userVM.isValidEmail(binding.etCorreo.text.toString())){
                        binding.etCorreo.error = getText(R.string.validation_mail)
                        cont = false
                    }
                } else {
                    binding.etCorreo.error = getText(R.string.obligado_mail)
                    cont = false
                }

                if(binding.etLugar.text.toString() == null){
                    binding.etLugar.error = getText(R.string.obligado_place)
                    cont = false
                }

                if(binding.etHora.text.toString() != null){
                    if(!userVM.isValidHour(binding.etHora.text.toString())) {
                        binding.etHora.error = getText(R.string.validation_hour)
                        cont = false
                    }
                } else{
                    binding.etHora.error = getText(R.string.obligado_hour)
                    cont = false
                }

                if(cont){
                    var arr: List<String> = binding.etFecha.text.toString().split("/")
                    var day = arr[0].toInt()
                    var month = arr[1].toInt()
                    var year = arr[2].toInt()

                    db.collection("users").document(username).update(
                        hashMapOf(
                            "mail" to binding.etCorreo.text.toString(),
                            "day" to day,
                            "month" to month,
                            "year" to year,
                            "hour" to binding.etHora.text.toString(),
                            "place" to binding.etLugar.text.toString()
                        ) as Map<String, Any>
                    )
                }
            }
        }
        binding.btnPost.setOnClickListener{
            onCreateDialog()
        }
    }

    /*
    * Método que crea un diálogo
     */
    private fun onCreateDialog() {
        var view = this.layoutInflater.inflate(R.layout.dialog_post, null)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setTitle(getText(R.string.account_post))
            .setView(view)
            .setPositiveButton(getText(R.string.util_aceptar)) { dialog, which ->
                var etPost: EditText = view.findViewById(R.id.etPost)
                postText(etPost.text.toString())
            }
            .setNegativeButton(getText(R.string.util_cancelar)) { dialog, which ->
                Toast.makeText(this, R.string.util_cancel_action, Toast.LENGTH_LONG).show()
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    /*
    * Método que carga un texto en Usuario de la base de datos de Firebase
    * @param    text
     */
    private fun postText(text: String) {
        db.collection("users").document(username).update(
            hashMapOf(
                "post" to text
            ) as Map<String, String>
        )

    }

    /*
    * Método que comprueba contraseña
    * @param    pwd
    * @return   Boolean
     */
    private fun getPassword(pwd: String): Boolean {
        var cont = false
        CoroutineScope(Dispatchers.Main).launch{
            db.collection("users").document(username).get().addOnSuccessListener {
                if(pwd == it.get("password").toString()){
                    cont = true
                }
            }.await()
        }

        return cont
    }

    /*
    * Método que inicia datos del usuario
     */
    private fun initUserData() {
        CoroutineScope(Dispatchers.Main).launch{
            db.collection("users").document(username).get().addOnSuccessListener {
                var followers = it.get("followers") as ArrayList<Int>
                var following = it.get("following") as ArrayList<Int>
                binding.tvFollowers.text = followers.size.toString()
                binding.tvFollowing.text = following.size.toString()
                binding.tvTitleProfile.text = it.get("username").toString().uppercase()
                binding.etCorreo.setText(it.get("mail").toString(), TextView.BufferType.EDITABLE)
                binding.etFecha.setText("${it.get("day")}/${it.get("month")}/${it.get("year")}", TextView.BufferType.EDITABLE)
                binding.etHora.setText(it.get("hour").toString(), TextView.BufferType.EDITABLE)
                binding.etLugar.setText(it.get("place").toString(), TextView.BufferType.EDITABLE)
                updateHeight(binding.vAstro, it.get("astroLevel").toString().toInt())
                updateHeight(binding.vMagia, it.get("magicLevel").toString().toInt())
                updateHeight(binding.vNumer, it.get("numerologyLevel").toString().toInt())
                updateHeight(binding.vTarot, it.get("tarotLevel").toString().toInt())

                try {
                    val imagen = it.get("image") as String
                    initProfileImage(imagen)
                }catch(e: Exception){
                    Log.i("LOGIA-ASTRO","No hay imagen en Usuario: "+e)
                    initProfileImage("libra")
                }
            }.await()
        }
    }

    /*
    * Método actualiza altura de un View
    * @param    view, stat
     */
    private fun updateHeight(view: View, stat:Int){
        val params = view.layoutParams
        params.height = stat * 10
        view.layoutParams = params
    }

    /*
    * Método que carga imagen de perfil o pone una aleatoria
     */
    private fun initProfileImage(image: String){
        val context: Context = this
        val id = context.resources.getIdentifier(
            image.lowercase(),
            "raw",
            context.packageName
        )
        binding.ivProfile.setImageResource(id)
    }
}