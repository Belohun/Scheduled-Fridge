package com.example.scheduledfridge.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color

import androidx.core.app.NotificationCompat
import com.example.scheduledfridge.MainActivity
import com.example.scheduledfridge.R



fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context, NOTIFICATION_ID: Int,title: String) {


    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )


    val bigImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.notification_drink
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(bigImage)
        .bigLargeIcon(bigImage)

    val builder = NotificationCompat.Builder(
        applicationContext,applicationContext.getString(R.string.notification_chanel_id)
    )
        .setSmallIcon(R.drawable.ic_fridge)
        .setContentTitle(title)
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setColor(Color.CYAN)
        .setLargeIcon(bigImage)

        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()
    notify(NOTIFICATION_ID, builder)
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}
