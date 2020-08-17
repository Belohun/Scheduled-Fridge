package com.example.scheduledfridge.utils
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.scheduledfridge.R
import com.google.gson.Gson

class Preferences(context: Context) {
    private val nightMode = context.getString(R.string.nightMode)
    private val notificationEnabled = context.getString(R.string.notificationEnabled)
    private val appPreferences: String = context.getString(R.string.appPreferences)
    private val sortingSelected = context.getString(R.string.sortingSelected)
    private val daysBeforeExpired = context.getString(R.string.daysBeforeExpired)
    private val appSettingPrefs: SharedPreferences = context.getSharedPreferences(appPreferences, 0)
    private val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()


    fun isNightMode():Boolean{
        return appSettingPrefs.getBoolean(nightMode,false)
    }
    fun setNightMode(value:Boolean){
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
    fun addDayBeforeExpiration(value:String){
        val days = getDaysBeforeExpiration()
        days.add(value)
        days.sort()
        val jsonString = Gson().toJson(days)
        sharedPrefsEdit.putString(daysBeforeExpired,jsonString)
        sharedPrefsEdit.apply()
    }
    fun getDaysBeforeExpiration(): ArrayList<String> {
        val standardValue = ArrayList<String>()
        standardValue.add("1")
        standardValue.add("3")
        val standardValueJson = Gson().toJson(standardValue)
        val jsonString = appSettingPrefs.getString(daysBeforeExpired,standardValueJson)
        return Gson().fromJson<ArrayList<String>>(jsonString, ArrayList::class.java)
    }
    fun deleteDaysBeforeExpiration(value: String){
        val days = getDaysBeforeExpiration()
        days.remove(value)
        val jsonString = Gson().toJson(days)
        sharedPrefsEdit.putString(daysBeforeExpired,jsonString)
        sharedPrefsEdit.apply()

    }
}
