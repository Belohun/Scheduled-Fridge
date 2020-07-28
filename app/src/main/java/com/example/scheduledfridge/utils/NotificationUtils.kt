package com.example.scheduledfridge.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.example.scheduledfridge.MainActivity
import com.example.scheduledfridge.R



private const val NOTIFICATION_ID = 0
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context, id:String) {


    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )


    val eggImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.ic_meat
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(eggImage)
        .bigLargeIcon(null)


    val builder = NotificationCompat.Builder(
        applicationContext,
       id
    )
        .setSmallIcon(R.mipmap.ic_fridge_foreground)
        .setContentTitle(applicationContext
            .getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setStyle(bigPicStyle)
        .setLargeIcon(eggImage)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}
