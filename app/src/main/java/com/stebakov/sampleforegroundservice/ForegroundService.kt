package com.stebakov.sampleforegroundservice

import android.R
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * Created by Vladimir Stebakov on 31.05.2023
 */
class ForegroundService : Service() {

    companion object {
        const val SERVICE_ID = 1002
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(SERVICE_ID, createNotification())
        Log.d("SERVICE_TEST", "On create")
    }

//    override fun onHandleIntent(p0: Intent?) {
//
//
//        coroutineScope.launch {
//            for (i in 0 until 100) {
//                delay(5000)
//                Log.d("SERVICE_TEST", "$i")
//            }
//        }
//    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        coroutineScope.launch {
            for (i in 0 until 100) {
                delay(5000)
                Log.d("SERVICE_TEST", "$i")
                if (i == 2) {
                        val notificationManager =
                            getSystemService(NOTIFICATION_SERVICE) as NotificationManager

                        val contentIntent = Intent(this@ForegroundService, MainActivity::class.java)
                        val contentPendingIntent =
                            PendingIntent.getActivity(
                                this@ForegroundService,
                                0,
                                contentIntent,
                                PendingIntent.FLAG_IMMUTABLE
                            )

                        val fullScreenIntent = Intent(this@ForegroundService, MainActivity2::class.java)
                        val fullScreenPendingIntent =
                            PendingIntent.getActivity(
                                this@ForegroundService,
                                0,
                                fullScreenIntent,
                                PendingIntent.FLAG_IMMUTABLE
                            )

                        val fullscreenIntent =
                            NotificationCompat.Builder(this@ForegroundService, "Channelid")
                                .setSmallIcon(R.drawable.ic_delete)
                                .setColor(ResourcesCompat.getColor(resources, R.color.background_dark, null))
                                .setContentTitle("Heads Up Notification")
                                .setAutoCancel(true)
                                .setContentIntent(contentPendingIntent)
                                .setFullScreenIntent(fullScreenPendingIntent, true)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory(NotificationCompat.CATEGORY_ALARM)
                                .build()
                        notificationManager.notify(1, fullscreenIntent)

                }
            }
        }
        return START_STICKY
    }

    private fun createNotification() = NotificationCompat.Builder(this, "Channelid")
        .setSmallIcon(R.drawable.ic_delete)
        .setContentTitle("My foreground service")
        .setContentText("Doing some work...")
        .build()

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel("Channelid", "ChannelName", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        Log.d("SERVICE_TEST", "On destroy")
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }


}