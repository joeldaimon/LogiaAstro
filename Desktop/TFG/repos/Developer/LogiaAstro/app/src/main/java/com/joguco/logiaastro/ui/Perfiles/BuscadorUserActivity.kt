package com.joguco.logiaastro.ui.Perfiles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import com.joguco.logiaastro.R
import com.joguco.logiaastro.database.entities.UserEntity
import com.joguco.logiaastro.databinding.ActivityBuscadorUserBinding
import com.joguco.logiaastro.interfaces.OnUserClick
import com.joguco.logiaastro.tabs.horoscopo.HoroscopoDetailFragment
import com.joguco.logiaastro.tabs.horoscopo.HoroscopoListFragment
import com.joguco.logiaastro.viewmodel.UserViewModel

class BuscadorUserActivity : AppCompatActivity(), OnUserClick {
    private lateinit var binding: ActivityBuscadorUserBinding

    //Layouts
    private val layoutList: FrameLayout by lazy { findViewById(R.id.containerListPerfiles) } //List fragment
    private val layoutDetail: FrameLayout? by lazy { findViewById(R.id.containerDetailPerfiles) } //Detail fragment

    private lateinit var userVM: UserViewModel
    private lateinit var userList: MutableList<UserEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityBuscadorUserBinding.inflate(layoutInflater).also { binding = it }.root)

        userVM = ViewModelProvider(this)[UserViewModel::class.java]
        userList = getUserList()!!
        loadRecyclerView()
        //If we are in ladscape
        if (isLandScape()){
            //Load DetailFragment too
            loadDetailFragment(true, 1)
        }
    }

    fun getUserList():MutableList<UserEntity> {
        return userVM.getAllUsers()
    }

    //Function that loads RecyclerView
    private fun loadRecyclerView() {
        supportFragmentManager.beginTransaction()
            .replace(layoutList.id, PerfilesListFragment.newInstance(1))
            .addToBackStack(null)
            .commit()
    }

    //Function that returns a Boolean - if the View is in Landscape mode
    private fun isLandScape():Boolean {
        return layoutDetail != null
    }

    //Function that loads DetailFragment if you Click an Item
    override fun onUserClick(user: UserEntity) {
        loadDetailFragment(isLandScape(), user.id)
    }

    //Function that loads DetailFragment
    private fun loadDetailFragment(twoPane:Boolean, signId:Long){
        //If we are in ListFragment and DetailFragment is null
        val id = layoutDetail?.id?:layoutList.id //we load DetailFragment layout

        supportFragmentManager.beginTransaction()
            .replace(id, HoroscopoDetailFragment.newInstance(signId.toInt()))
            .addToBackStack(null)
            .commit()
    }


}