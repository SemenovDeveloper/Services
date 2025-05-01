package com.semenovdev.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.semenovdev.services.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var notificationID = 0

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.simpleService.setOnClickListener {
            startService(MyService.newIntent(this))
        }
        binding.foregroundService.setOnClickListener {
            showNotification()
        }
    }

    private fun showNotification () {

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(
                    CHANEL_ID,
                    CHANEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notification = NotificationCompat.Builder(this, CHANEL_ID)
            .setContentTitle("Title")
            .setContentText("Text")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build()

        notificationManager.notify(notificationID++, notification)
    }

    companion object {
        private const val CHANEL_ID = "chanel_id"
        private const val CHANEL_NAME = "foreground_chanel"
    }
}