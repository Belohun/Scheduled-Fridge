package com.example.scheduledfridge.utils
import android.content.Context
import android.content.SharedPreferences

class Preferences(context: Context) {
    private val nightMode: String = "NightMode"
    private val appPreferences: String = "AppPreferences"
    private val appSettingPrefs: SharedPreferences = context.getSharedPreferences(appPreferences, 0)
    private val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()

    fun getMode():Boolean{
        return appSettingPrefs.getBoolean(nightMode,false)
    }
    fun setMode(value:Boolean){
        sharedPrefsEdit.putBoolean("NightMode",value)
        sharedPrefsEdit.apply()
    }
}