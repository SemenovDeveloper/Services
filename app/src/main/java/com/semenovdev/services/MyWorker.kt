package com.semenovdev.services

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class MyWorker(
    context: Context,
    private val workerParameters: WorkerParameters
): Worker(context, workerParameters) {
    override fun doWork(): Result {
        log("doWork")
        val page = workerParameters.inputData.getInt(PAGE_EXTRA, 0)
        for (i in 0..3) {
            Thread.sleep(1000)
            log("Timer: $page $i")
        }

        return Result.success()
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "My work service: $message")
    }

    companion object {
        private const val PAGE_EXTRA = "page"
        const val WORKER_NAME = "worker_name"

        fun makeRequest(page: Int): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<MyWorker>().apply {
                setInputData(workDataOf(PAGE_EXTRA to page))
                setConstraints(makeConstraints())
            }.build()
        }

        private fun makeConstraints(): Constraints {
            return Constraints.Builder()
                .setRequiresCharging(true)
                .build()
        }
    }
}