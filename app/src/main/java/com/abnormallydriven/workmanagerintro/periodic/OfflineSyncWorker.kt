package com.abnormallydriven.workmanagerintro.periodic

import android.util.Log
import androidx.work.Worker
import com.abnormallydriven.workmanagerintro.WorkmanagerIntroApp
import java.util.Random
import javax.inject.Inject

class OfflineSyncWorker : Worker() {

    @Inject
    lateinit var random: Random

    override fun doWork(): Result {
        Log.d("OfflineSyncWorker", "doing work...")

        // The injection story for Worker objects with dagger is a little awkward at the moment
        // But work is being done:
        // https://github.com/google/dagger/issues/1183
        val app = applicationContext as? WorkmanagerIntroApp ?: return Result.FAILURE
        val injector = app.injector() ?: return Result.FAILURE
        injector.inject(this)

        // Pretend to upload some files that we saved when we were doing work
        // while offline
        val randomJobTime = (random.nextInt(15) + 5) * 1000

        Thread.sleep(randomJobTime.toLong())

        return Result.SUCCESS
    }

    companion object {
        val TAG = "offlineSyncWorker"
    }
}