package com.semenovdev.services

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Build
import android.os.PersistableBundle
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyJobService: JobService() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        log("onStartJob")
        coroutineScope.launch {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var workItem = params?.dequeueWork()
            while (workItem != null) {
                var page = workItem.intent?.getIntExtra(PAGE_EXTRA, 0) ?: 0

                    for (i in 0..5) {
                        delay(1000)
                        log("Timer: $page $i")
                    }
                    params?.completeWork(workItem)
                    workItem = params?.dequeueWork()
                }
                jobFinished(params, true)
            }
        }

        //возвращаемое значение true, если сервис все еще выполняется и мы сами завершим работу
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        log("onStopJob")
        return true
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "My job service: $message")
    }

    companion object {
        const val JOB_ID = 111
        private const val PAGE_EXTRA = "page"

        fun newIntent (page: Int): Intent {
            return Intent()
                .putExtra(PAGE_EXTRA, page)
        }
    }
}