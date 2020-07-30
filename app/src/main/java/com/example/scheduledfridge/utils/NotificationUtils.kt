package com.example.scheduledfridge.utils
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.example.scheduledfridge.MainActivity
import com.example.scheduledfridge.R



fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context, ID: Int, title: String, image: Bitmap) {


    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
    val builder = NotificationCompat.Builder(
        applicationContext,applicationContext.getString(R.string.notification_chanel_id)
    )
        .setSmallIcon(R.drawable.ic_fridge)
        .setContentTitle(title)
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setGroup(applicationContext.getString(R.string.notification_group_id))
        .setAutoCancel(true)
        .setColor(Color.CYAN)
        .setLargeIcon(image)

        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()
    val group = NotificationCompat.Builder(
        applicationContext,applicationContext.getString(R.string.notification_chanel_id)
    )
        .setSmallIcon(R.drawable.ic_fridge)
        .setContentIntent(contentPendingIntent)
        .setGroup(applicationContext.getString(R.string.notification_group_id))
        .setAutoCancel(true)
        .setGroupSummary(true)
        .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
        .setColor(Color.CYAN)
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .build()
    notify(ID, builder)
    notify(0,group)
}

