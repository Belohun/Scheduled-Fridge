package com.example.scheduledfridge.ui.settings
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduledfridge.R
import com.example.scheduledfridge.utils.Preferences
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.settings_add_day_layout.*

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
        val daysBeforeExpired = preferences.getDaysBeforeExpired()
        val daysBeforeExpiredAdapter = SettingsDaysBeforeExpiredAdapter(context)
        daysBeforeExpiredAdapter.setDays(daysBeforeExpired)
        settings_daysBefore_RecyclerView.adapter = daysBeforeExpiredAdapter
        settings_daysBefore_RecyclerView.layoutManager = LinearLayoutManager(context,
            RecyclerView.VERTICAL,false)

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
        settings_addDaysBefore_ImageButton.setOnClickListener{
            val dialogView =
                LayoutInflater.from(this.activity).inflate(R.layout.settings_add_day_layout, null)
            val mBuilder = AlertDialog.Builder(this.requireActivity())
                .setView(dialogView)
            val mAlertDialog = mBuilder.show()
            mAlertDialog.settings_add_day_btn_cancel.setOnClickListener{
                mAlertDialog.dismiss()
            }
            mAlertDialog.settings_add_day_btn_add.setOnClickListener{

            }

        }
        super.onViewCreated(view, savedInstanceState)
    }
}