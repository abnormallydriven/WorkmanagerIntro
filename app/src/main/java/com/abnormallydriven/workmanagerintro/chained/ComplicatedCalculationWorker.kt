package com.abnormallydriven.workmanagerintro.chained

import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import com.abnormallydriven.workmanagerintro.WorkmanagerIntroApp
import java.util.*
import javax.inject.Inject

class ComplicatedCalculationWorker : Worker() {

    @Inject
    lateinit var random: Random

    override fun doWork(): Result {
        Log.d("ComplicatedWorker", "doing work...")

        // The injection story for Worker objects with dagger is a little awkward at the moment
        // But work is being done:
        // https://github.com/google/dagger/issues/1183
        val app = applicationContext as? WorkmanagerIntroApp ?: return Result.FAILURE
        val injector = app.injector() ?: return Result.FAILURE
        injector.inject(this)

        //Pretend to do some complicated calculation and send the result to an output object
        val randomJobTime = (random.nextInt(15) + 5) * 1000

        Thread.sleep(randomJobTime.toLong())

        val completedAnalysis : Data = Data.Builder()
                .putString(RESULT_KEY, "result")
                .build()

        outputData = completedAnalysis
        Log.d("ComplicatedWorker", "Job done!")

        return Result.SUCCESS
    }

    companion object {
        val RESULT_KEY = "resultKey"
    }
}