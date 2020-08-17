package com.example.scheduledfridge.utils
import android.annotation.SuppressLint
import android.app.AlarmManager
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
@SuppressLint("NewApi")

fun generateNotification(
    id: Int,
    productExpirationDate: String,
    productName: String,
    productType: String,
    context: Context
) {
    val intent = Intent(context, ReminderBroadcast::class.java)
    val daysLeft = ViewUtils().getDaysLeft(productExpirationDate, context)
            val currentDate = System.currentTimeMillis()
            val millsInDay = 86400000
            val timeToBeAdded: Long = millsInDay * (daysLeft)
            val timeToBeAddedTemp = 1000 * 5
            val message: String =
                if(daysLeft <1){
                    context.getString(R.string.Expired)
                }else {
                    (daysLeft ).toString() + " " + context.getString(R.string.daysLeftToExpire)
            }

            intent.putExtra(context.getString(R.string.name),productName)
            intent.putExtra(context.getString(R.string.id),id)
            intent.putExtra(context.getString(R.string.message), message)
            intent.putExtra(context.getString(R.string.type), productType)
            intent.putExtra(context.getString(R.string.ExpirationDate),productExpirationDate)
            val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0)
            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    currentDate + timeToBeAddedTemp,
                    pendingIntent
                )
        }

fun cancelNotification(context: Context,id:Int){
    val intent = Intent(context, ReminderBroadcast::class.java)
    PendingIntent.getBroadcast(context,id,intent,PendingIntent.FLAG_UPDATE_CURRENT).cancel()
}
