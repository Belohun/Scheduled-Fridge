package com.example.scheduledfridge.ui.settings
import android.os.Bundle
import android.util.Log
import android.view.FocusFinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
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
        switchMaterial_nightMode_settings.isChecked = isDark

        switchMaterial_nightMode_settings.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                preferences.setMode(true)

            }else{
               AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                preferences.setMode(false)
            }

        }

        val isNotificationEnabled = preferences.getNotificationSetting()
        switchMaterial_notification_settings.isChecked = isNotificationEnabled
        subOptions_layout_settings.isVisible = isNotificationEnabled


        switchMaterial_notification_settings.setOnCheckedChangeListener{_,isChecked ->
            if (isChecked){
                preferences.setNotifications(true)
                NotificationManagerCompat.from(requireContext()).cancelAll()

            }else{
                preferences.setNotifications(false)

            }
        }

        super.onViewCreated(view, savedInstanceState)
    }
}