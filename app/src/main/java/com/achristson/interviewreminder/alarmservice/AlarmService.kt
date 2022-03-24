package com.achristson.interviewreminder.alarmservice

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import androidx.annotation.RequiresApi
import com.achristson.interviewreminder.models.Interview
import com.achristson.interviewreminder.utils.TimeFormatUtil
import java.util.concurrent.TimeUnit

private const val REQUEST_CODE = 0

@SuppressLint("UnspecifiedImmutableFlag")
@RequiresApi(Build.VERSION_CODES.O)
fun setAlarm(context: Context, interview : Interview){
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context.applicationContext, AlarmReceiver::class.java)
        .putExtra("interviewId", interview.interviewId)

    val pendingIntent = PendingIntent.getBroadcast(
        context.applicationContext,
        REQUEST_CODE,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT)

    val numberOfDaysDifference = TimeFormatUtil().getNumberOfDaysDifference(interview).toLong()
    val numberOfDaysInMillis = TimeUnit.DAYS.toMillis(numberOfDaysDifference)
    val triggerAtMillis = SystemClock.elapsedRealtime()+numberOfDaysInMillis

    alarmManager.set(
        AlarmManager.RTC_WAKEUP,
        triggerAtMillis,
        pendingIntent
    )
}

@SuppressLint("UnspecifiedImmutableFlag")
fun cancelAlarm(context: Context){

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context.applicationContext, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context.applicationContext,
        REQUEST_CODE,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT)
    alarmManager.cancel(pendingIntent)
}