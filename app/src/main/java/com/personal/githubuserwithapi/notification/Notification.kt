package com.personal.githubuserwithapi.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.personal.githubuserwithapi.MainActivity
import com.personal.githubuserwithapi.R
import java.util.*

class Notification : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        showAlarmNotification(context, message)

        Log.d(TAG, "Alarm is on")
    }

    fun setAlarmNotification(context: Context, type: String, message: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, Notification::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, type)

        val timeArray = TIME.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val calendar = Calendar.getInstance()
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
            set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
            set(Calendar.SECOND, 0)
        }

        val pendingIntent = PendingIntent.getBroadcast(context, REPEATING_ID, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

        Toast.makeText(context, context.resources.getString(R.string.alarm_on), Toast.LENGTH_SHORT).show()
        Log.d(TAG, "Set Alarm Notification Success")
    }

    fun cancelAlarmNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, Notification::class.java)
        val requestCode = NOTIFICATION_ID
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)

        Toast.makeText(context, context.resources.getString(R.string.alarm_off), Toast.LENGTH_SHORT).show()
        Log.d(TAG, "Alarm is Off")
    }

    private fun showAlarmNotification(context: Context, message: String?) {
        val channelId = "channel_id"
        val channelName = "channel_name"

        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(context, channelId)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notifications_white)
                .setContentTitle(context.resources.getString(R.string.github_reminder))
                .setContentText(message)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val EXTRA_TYPE = "extra_type"
        const val EXTRA_MESSAGE = "extra_message"

        private const val NOTIFICATION_ID = 100
        private const val REPEATING_ID = 101
        private const val TIME = "09:00"

        private val TAG = Notification::class.java.simpleName
    }
}