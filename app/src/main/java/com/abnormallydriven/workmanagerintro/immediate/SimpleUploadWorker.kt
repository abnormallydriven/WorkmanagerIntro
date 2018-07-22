package com.abnormallydriven.workmanagerintro.immediate

import android.util.Log
import androidx.work.WorkStatus
import androidx.work.Worker
import com.abnormallydriven.workmanagerintro.WorkmanagerIntroApp
import java.util.*
import javax.inject.Inject

class SimpleUploadWorker : Worker() {

    @Inject
    lateinit var fakeUploadManager: FakeUploadManager

    @Inject
    lateinit var random: Random

    override fun doWork(): Result {
        Log.d("SimpleUploadWorker", "doing work...")

        // The injection story for Worker objects with dagger is a little awkward at the moment
        // But work is being done:
        // https://github.com/google/dagger/issues/1183
        val app = applicationContext as? WorkmanagerIntroApp ?: return Result.FAILURE
        val injector = app.injector() ?: return Result.FAILURE
        injector.inject(this)

        // Pretend to upload some files that we saved when we were doing work
        // while offline
        val jobRunTime = (random.nextInt(15) + 5) * 1000
        val jobCompleteTime = System.currentTimeMillis() + jobRunTime


        fakeUploadManager.startUpload()
        while(System.currentTimeMillis() < jobCompleteTime){
            fakeUploadManager.uploadFile()
            Thread.sleep(jobRunTime.toLong() / fakeUploadManager.totalFileCount)
        }

        Log.d("SimpleUploadWorker", "Job Done!")
        return Result.SUCCESS
    }

    companion object {
        val TAG = "SimpleUploadWorker"
    }
}