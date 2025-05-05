package com.semenovdev.services

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.semenovdev.services.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var foregroundService: Intent
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.simpleService.setOnClickListener {
            // Oстановка сервиса снаружи
            stopService(foregroundService)
            startService(NotificationService.newIntent(this))
        }
        binding.foregroundService.setOnClickListener {
            foregroundService = NotificationForegroundService.newIntent(this)
            ContextCompat.startForegroundService(this, foregroundService)
        }
        binding.intentService.setOnClickListener {
            startService(NotificationIntentService.newIntent(this))
        }
    }
}