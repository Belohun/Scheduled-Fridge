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
        val image = ViewUtils().returnIconDrawable(type!!,context)
        notificationManager.sendNotification(message!!,context,id,name!!,image!!)


    }
}