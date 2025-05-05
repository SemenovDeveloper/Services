package com.semenovdev.services

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

class MyJobIntentService: JobIntentService() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        log("onCreate")
        super.onCreate()
    }

    override fun onDestroy() {
        log("onDestroy")
        super.onDestroy()
        coroutineScope.cancel()
    }

    override fun onHandleWork(intent: Intent) {
        log("onHandleWork")
        val page = intent.getIntExtra(PAGE_EXTRA, 0)
        for (i in 0..3) {
            Thread.sleep(1000)
            log("Timer: $page $i")
        }
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "My job intent service: $message")
    }


    companion object {
        private const val PAGE_EXTRA = "page"
        private const val JOB_ID = 111

        fun enqueue(context: Context, page: Int) {
            enqueueWork(
                context,
                MyJobIntentService::class.java,
                JOB_ID,
                newIntent(context, page)
            )
        }

        private fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, MyJobIntentService::class.java).apply {
                putExtra(PAGE_EXTRA, page)
            }
        }
    }
}