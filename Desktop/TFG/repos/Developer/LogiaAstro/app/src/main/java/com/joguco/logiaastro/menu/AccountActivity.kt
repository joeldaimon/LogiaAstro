package com.joguco.logiaastro.menu

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.joguco.logiaastro.R
import com.joguco.logiaastro.database.entities.UserEntity
import com.joguco.logiaastro.databinding.ActivityAccountBinding
import com.joguco.logiaastro.ui.MainActivity
import com.joguco.logiaastro.viewmodel.UserViewModel

class AccountActivity : AppCompatActivity() {
    //Binding
    private lateinit var binding: ActivityAccountBinding

    //User
    private lateinit var user: UserEntity

    //ViewModel
    private lateinit var userVM: UserViewModel


    //Subiendo imagen de perfil
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.ivProfile.setImageURI(uri)
            this@AccountActivity.grantUriPermission(
                "com.joguco.logiaastro.menu",
                uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            user.image = uri.toString() //MOD db
            userVM.update(user)
            Log.i("JOELDAIMON","INSERTING image in ${user.username} -> Uri $uri")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Inflando layout
        setContentView(ActivityAccountBinding.inflate(layoutInflater).also { binding = it }.root)

        //Iniciamos VM
        userVM = ViewModelProvider(this)[UserViewModel::class.java]

        //Cogemos EXTRA
        var username = intent?.getStringExtra(MainActivity.KEY_EXTRA_USER)!!
        user = userVM.getByName(username)!!

        //Metodos de inicio
        initListeners()
        initUserData()
    }

    /*
    * Método que inicia Listeners
     */
    private fun initListeners(){
        binding.ivProfile.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.btnPwd.setOnClickListener{
            binding.llUser.visibility = View.INVISIBLE
            binding.llCorreo.visibility = View.INVISIBLE
            binding.llFecha.visibility = View.INVISIBLE
            binding.llLugar.visibility = View.INVISIBLE
            binding.llHora.visibility = View.INVISIBLE
            binding.llPwdChange.visibility = View.VISIBLE
        }

        binding.btnSave.setOnClickListener{
            if(binding.llPwdChange.isVisible){ //Updating pwd
                if(binding.etPwdOld.text.toString() == user.password){
                    if(binding.etPwdNew.text.isNotEmpty()){
                        user.password = binding.etPwdNew.text.toString()
                        userVM.update(user)
                    } else {
                        binding.etPwdNew.error = getText(R.string.account_pwd)
                    }
                } else {
                    binding.etPwdOld.error = getText(R.string.account_pwd_error)
                }
            } else { //Updating user with new data
                if(binding.etFecha.text.toString() != null){
                    if(userVM.isValidDate(binding.etFecha.text.toString())){
                        var arr: List<String> = binding.etFecha.text.toString().split("/")
                        user.day = arr[0].toInt()
                        user.month = arr[1].toInt()
                        user.year = arr[2].toInt()
                    } else{
                        binding.etFecha.error = getText(R.string.account_date_error)
                    }
                } else{
                    binding.etFecha.error = getText(R.string.account_date)
                }

                if(binding.etCorreo.text.toString() != null){
                    if(userVM.isValidEmail(binding.etCorreo.text.toString()))
                        user.mail = binding.etCorreo.text.toString()
                    else{
                        binding.etCorreo.error = getText(R.string.validation_mail)
                    }
                } else{
                    binding.etCorreo.error = getText(R.string.obligado_mail)
                }

                if(binding.etLugar.text.toString() != null){
                    user.place = binding.etLugar.text.toString()
                } else{
                    binding.etLugar.error = getText(R.string.obligado_place)
                }

                if(binding.etHora.text.toString() != null){
                    if(userVM.isValidHour(binding.etHora.text.toString()))
                        user.hour = binding.etHora.text.toString()
                    else{
                        binding.etHora.error = getText(R.string.validation_hour)
                    }
                } else{
                    binding.etHora.error = getText(R.string.obligado_hour)
                }

                userVM.update(user)
            }

        }
    }

    /*
    * Método que inicia datos del usuario
     */
    private fun initUserData() {
        binding.tvFollowers.text = user.followers.size.toString()
        binding.tvFollowing.text = user.following.size.toString()
        binding.tvFriends.text = user.friends.size.toString()
        binding.tvTitleProfile.text = user.username.uppercase()
        binding.etUsuario.setText(user.username, TextView.BufferType.EDITABLE)
        binding.etCorreo.setText(user.mail, TextView.BufferType.EDITABLE)
        binding.etFecha.setText("${user.day}/${user.month}/${user.year}", TextView.BufferType.EDITABLE)
        binding.etHora.setText(user.hour, TextView.BufferType.EDITABLE)
        binding.etLugar.setText(user.place, TextView.BufferType.EDITABLE)

        if(user.image != ""){
            try {
                var uri = Uri.parse(user.image);
                binding.ivProfile.setImageURI(uri)
            } catch(se: SecurityException){
                Log.i("JOELDAIMON", "SecurityException in ${user.username}'s user image -> $se")
                user.image = ""
                userVM.update(user)
                initProfileImage()
            } finally {}
        } else {
            Log.i("JOELDAIMON", "Image in ${user.username} is empty")
            initProfileImage()
        }
    }

    /*
    * Método que carga imagen de perfil o pone una aleatoria
     */
    private fun initProfileImage(){
        val context: Context = this
        val id = context.resources.getIdentifier(
            "libra",
            "raw",
            context.packageName
        )
        //Setting the image by id
        binding.ivProfile.setImageResource(id)
    }
}