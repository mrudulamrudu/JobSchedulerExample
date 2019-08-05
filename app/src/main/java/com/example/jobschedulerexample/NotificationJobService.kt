package com.example.jobschedulerexample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat

class NotificationJobService : JobService() {

    private val TAG = "JobScheduler"
    private var isJobStopped = false
    private val NOTIFICATION_ID = 1111

    override fun onStartJob(p0: JobParameters?): Boolean {
        createNotificationChannel()
        return false
    }

    private fun createNotificationChannel() {
        Log.d(TAG, "onStartJob")
        val CHANNEL_ID = "${packageName}-${getString(R.string.app_name)}"
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "notify_channel"
            val desc = "Channel Description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(CHANNEL_ID, channelName, importance)
            notificationChannel.description = desc
            notificationChannel.setShowBadge(true)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val contentPendingIntent = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_launcher_background)
            setContentTitle("Job Service")
            setContentText("I am a notification from job service..")
            setDefaults(NotificationCompat.DEFAULT_ALL)
            priority = NotificationCompat.PRIORITY_HIGH
            setContentIntent(contentPendingIntent)
            setAutoCancel(true)
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        Log.d(TAG, "onStopJob called before completion")
        isJobStopped = true
        return true
    }
}