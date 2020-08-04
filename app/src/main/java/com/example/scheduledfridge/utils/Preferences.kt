package com.example.scheduledfridge.utils
import android.content.Context
import android.content.SharedPreferences
import com.example.scheduledfridge.R

class Preferences(context: Context) {
    private val nightMode = context.getString(R.string.nightMode)
    private val notificationEnabled = context.getString(R.string.notificationEnabled)
    private val appPreferences: String = context.getString(R.string.appPreferences)
    private val sortingSelected = context.getString(R.string.sortingSelected)
    private val appSettingPrefs: SharedPreferences = context.getSharedPreferences(appPreferences, 0)
    private val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()


    fun getMode():Boolean{
        return appSettingPrefs.getBoolean(nightMode,false)
    }
    fun setMode(value:Boolean){
        sharedPrefsEdit.putBoolean(nightMode,value)
        sharedPrefsEdit.apply()
    }
    fun setNotifications(value: Boolean){
        sharedPrefsEdit.putBoolean(notificationEnabled,value)
        sharedPrefsEdit.apply()
    }
    fun getNotificationSetting(): Boolean {
        return appSettingPrefs.getBoolean(notificationEnabled,true)
    }
    fun setSorting(value: String ){
        sharedPrefsEdit.putString(sortingSelected,value)
        sharedPrefsEdit.apply()
    }
    fun getSorting(): String {
        return appSettingPrefs.getString(sortingSelected,"Last added last")!!
    }
}