package com.semenovdev.services

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

class NotificationIntentService: IntentService(NAME) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        log("onCreate")
        super.onCreate()
        createNotificationChanel()
        val notification = createNotification()
        showNotification(notification)
    }

    override fun onDestroy() {
        log("onDestroy")
        super.onDestroy()
        coroutineScope.cancel()
    }

    override fun onHandleIntent(intent: Intent?) {
        log("onHandleIntent")
        for (i in 0..3) {
            Thread.sleep(1000)
            log("Timer: $i")
        }
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "My intent service: $message")
    }

    private fun showNotification(notification: Notification) {
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChanel (){
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chanel =
                NotificationChannel(
                    CHANEL_ID,
                    CHANEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            notificationManager.createNotificationChannel(chanel)
        }
    }

    private fun createNotification (): Notification  = NotificationCompat.Builder(this, CHANEL_ID)
        .setContentTitle("Title")
        .setContentText("Text")
        .setSmallIcon(R.drawable.ic_launcher_background)
        .build()

    companion object {
        private const val CHANEL_ID = "chanel_id"
        private const val CHANEL_NAME = "Timer"
        private const val NOTIFICATION_ID = 1
        private const val NAME = "INTENT_SERVICE"

        fun newIntent(context: Context): Intent {
            return Intent(context, NotificationIntentService::class.java)
        }
    }
}