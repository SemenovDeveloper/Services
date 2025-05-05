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

class EarlyVersionsIntentService: IntentService(NAME) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate() {
        log("onCreate")
        super.onCreate()
        setIntentRedelivery(true)
    }

    override fun onDestroy() {
        log("onDestroy")
        super.onDestroy()
        coroutineScope.cancel()
    }

    override fun onHandleIntent(intent: Intent?) {
        log("onHandleIntent")
        val page = intent?.getIntExtra(PAGE_EXTRA, 0) ?: 0
        for (i in 0..3) {
            Thread.sleep(1000)
            log("Timer: $page $i")
        }
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "My intent service: $message")
    }


    companion object {
        private const val NAME = "INTENT_SERVICE"
        private const val PAGE_EXTRA = "page"

        fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, EarlyVersionsIntentService::class.java).apply {
                putExtra(PAGE_EXTRA, page)
            }
        }
    }
}