package com.example.scheduledfridge.utils

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.scheduledfridge.R

class ReminderBroadcast: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val notificationManager = ContextCompat.getSystemService(
            context!!,
            NotificationManager::class.java
        ) as NotificationManager
        val id = intent!!.getIntExtra(context.getString(R.string.id),0)
        val name = intent.getStringExtra(context.getString(R.string.name))
        val message = intent.getStringExtra(context.getString(R.string.message))
        val type = intent.getStringExtra(context.getString(R.string.type))
        val expirationDate = intent.getStringExtra(context.getString(R.string.ExpirationDate))!!
        val image = ViewUtils().returnIconDrawable(type!!,context)

        val preferences =Preferences(context)
        val isNotificationEnabled = preferences.getNotificationSetting()
        val listOfScheduledNotification = preferences.getDaysBeforeExpiration()
        val daysLeft = ViewUtils().getDaysLeft(expirationDate,context)
        if(isNotificationEnabled) {
            var i = 0
            while (i < listOfScheduledNotification.size ) {
                if (listOfScheduledNotification[i].toInt() == daysLeft.toInt() || daysLeft.toInt() == 0)
                {
                    notificationManager.sendNotification(message!!, context, id, name!!, image!!)
                }
                i++
            }
        }



        if(daysLeft > 0) {
            generateNotification(id, expirationDate, name!!, type, context)
        }

    }
}