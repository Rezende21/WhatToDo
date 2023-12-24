package com.oquefazer.notification

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.oquefazer.MainActivity
import com.oquefazer.R
import java.util.Calendar
import java.util.Date

class SetNotification(
    private val context: Context
) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun scheduleNotification(title : String, time : Long) {
        titleExtra = title
        channelId = title
        val intent = Intent(context, Notification::class.java)
        /*val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        ) */
       val pendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }

    fun createNotificationChannel(id : Int, title: String) {
        notificationId = id
        channelId = title
        val name = "Notification2"
        val desc = "desc Notificacation"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channer = NotificationChannel(id.toString(),title, importance)
        channer.description = desc
        notificationManager.createNotificationChannel(channer)

    }

    companion object {
        //const val COUNTER_CHANNEL_ID = "my_group"
        var notificationId = 0
        var channelId = "channel1"
        var titleExtra = "titleExtra"

    }
}