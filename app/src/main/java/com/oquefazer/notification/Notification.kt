package com.oquefazer.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.oquefazer.R

class Notification : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notification = NotificationCompat.Builder(context, SetNotification.channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(SetNotification.channelId)
            .build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(SetNotification.notificationId,notification)
    }
}