package com.achristson.interviewreminder.alarmservice

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.achristson.interviewreminder.R

private const val NOTIFICATION_ID = 0

class AlarmReceiver : BroadcastReceiver(){
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context, intent: Intent?) {
        cancelAlarm(context)

        val bundle : Bundle? = intent?.extras

        val contentPendingIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.interviewDetailsFragment)
            .setArguments(bundle)
            .createPendingIntent()

        val builder = NotificationCompat.Builder(
            context.applicationContext,
            context.getString(R.string.interview_notification_channel_id))
            .setSmallIcon(R.drawable.clock)
            .setContentIntent(contentPendingIntent)
            .setContentTitle(context.getString(R.string.interview_notification_title))
            .setContentText(context.getString(R.string.interview_notification_text))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(
            NOTIFICATION_ID,
            builder.build()
        )

    }
}