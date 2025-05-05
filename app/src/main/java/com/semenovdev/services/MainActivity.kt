package com.semenovdev.services

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.semenovdev.services.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var foregroundService: Intent
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var page = 0

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
        binding.jobScheduler.setOnClickListener {
            Log.d("jobScheduler", "jobScheduler")
            val component = ComponentName(this, MyJobService::class.java)
            val jobInfo = JobInfo.Builder(MyJobService.JOB_ID, component)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setExtras(MyJobService.newBundle(page++))
                .build()

            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.schedule(jobInfo)
        }
    }
}