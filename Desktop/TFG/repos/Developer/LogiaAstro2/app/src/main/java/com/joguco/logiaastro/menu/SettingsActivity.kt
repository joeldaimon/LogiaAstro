package com.joguco.logiaastro.menu

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.joguco.logiaastro.databinding.ActivitySettingsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsActivity : AppCompatActivity() {
    //Binding
    private lateinit var binding: ActivitySettingsBinding

    private var firstTime = true

    companion object{
        const val KEY_DARKMODE = "key_darkmode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivitySettingsBinding.inflate(layoutInflater).also { binding = it}.root )

        CoroutineScope(Dispatchers.IO).launch{
            getSettings().filter{ firstTime }.collect{darkMode ->
                if(darkMode != null){
                    runOnUiThread{
                        binding.sDarkmode.isChecked = darkMode
                        firstTime = false
                    }
                }
            }
        }

        initListeners()
        checkDarkMode()
    }

    /*
    * Método que guarda opciones de la App
    * @param    key, value
     */
    private suspend fun saveOptions(key: String, value: Boolean){
        dataStore.edit{preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }

    /*
     * Método que carga Settings
     * @return  Flow<Boolean?>
      */
    private fun getSettings(): Flow<Boolean?> {
        return dataStore.data.map{ preferences->
            preferences[booleanPreferencesKey(KEY_DARKMODE)] ?: false
        }
    }

    /*
     * Método que comprueba si está encendido el modo oscuro
      */
    private fun checkDarkMode() {
        when (this.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.sDarkmode.isChecked = true
            }
        }
    }

    /*
    * Cargando listeners
     */
    private fun initListeners() {
        binding.sDarkmode.setOnCheckedChangeListener{ _, value ->
            if(value)
                enableDarkMode()
            else
                disableDarkMode()

            CoroutineScope(Dispatchers.IO).launch{
                saveOptions(KEY_DARKMODE, value)
            }
        }
    }

    /*
     * Método que activa modo oscuro
      */
    private fun enableDarkMode(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        delegate.applyDayNight()
    }

    /*
     * Método que desactiva el modo oscuro
      */
    private fun disableDarkMode(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        delegate.applyDayNight()
    }
}