package com.abnormallydriven.workmanagerintro.chained

import android.util.Log
import androidx.work.Worker
import com.abnormallydriven.workmanagerintro.WorkmanagerIntroApp
import java.util.*
import javax.inject.Inject

class PreCalculationWorker : Worker() {

    @Inject
    lateinit var random: Random

    override fun doWork(): Result {
        Log.d("PreCalculationWorker", "doing work...")

        // The injection story for Worker objects with dagger is a little awkward at the moment
        // But work is being done:
        // https://github.com/google/dagger/issues/1183
        val app = applicationContext as? WorkmanagerIntroApp ?: return Result.FAILURE
        val injector = app.injector() ?: return Result.FAILURE
        injector.inject(this)

        //Pretend to do some sensor gathering and bundling that we store some where
        //to be consumed later by the ComplicatedCalculationWorker
        val randomJobTime = (random.nextInt(15) + 5) * 1000

        Thread.sleep(randomJobTime.toLong())

        Log.d("PreCalculationWorker", "Job done!")

        return Result.SUCCESS
    }
}