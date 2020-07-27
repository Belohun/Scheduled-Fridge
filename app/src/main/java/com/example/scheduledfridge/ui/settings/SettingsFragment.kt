package com.example.scheduledfridge.ui.settings
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.scheduledfridge.R
import com.example.scheduledfridge.utils.Preferences
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val preferences = Preferences(requireContext())
        val isDark = preferences.getMode()
        if(isDark){
            switchMaterial_nightMode_settings.isChecked = true
        }else if(!isDark){
            switchMaterial_nightMode_settings.isChecked = false
        }

        switchMaterial_nightMode_settings.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                preferences.setMode(true)

            }else{
               AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                preferences.setMode(false)
            }

        }

        super.onViewCreated(view, savedInstanceState)
    }
}