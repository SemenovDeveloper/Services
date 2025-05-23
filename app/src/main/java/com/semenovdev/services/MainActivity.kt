package com.semenovdev.services

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobWorkItem
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val component = ComponentName(this, MyJobService::class.java)
                val jobInfo = JobInfo.Builder(MyJobService.JOB_ID, component)
                    .setRequiresCharging(true)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                    .build()

                val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
                val intent = MyJobService.newIntent(page++)
                jobScheduler.enqueue(jobInfo, JobWorkItem(intent))
            } else {
                startService(EarlyVersionsIntentService.newIntent(this, page++))
            }
        }
        binding.jobIntentService.setOnClickListener {
            MyJobIntentService.enqueue(this, page++)
        }
        binding.workManager.setOnClickListener {
            val workManager = WorkManager.getInstance(applicationContext)
            workManager.enqueueUniqueWork(
                MyWorker.WORKER_NAME,
                ExistingWorkPolicy.APPEND,
                MyWorker.makeRequest(page++)
            )
        }
    }
}