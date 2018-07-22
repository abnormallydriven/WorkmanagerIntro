package com.abnormallydriven.workmanagerintro

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.ExistingPeriodicWorkPolicy
import com.abnormallydriven.workmanagerintro.common.di.AppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import com.abnormallydriven.workmanagerintro.common.di.DaggerAppComponent
import com.abnormallydriven.workmanagerintro.periodic.OfflineSyncWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkmanagerIntroApp : DaggerApplication() {

    @Inject
    lateinit var workManager: WorkManager

    override fun onCreate() {
        super.onCreate()

        schedulePeriodicOfflineWorkSync()
    }

    private fun schedulePeriodicOfflineWorkSync() {
        // We can make sure the device has a connection and healthy battery before running this job
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()

        val syncRequest = PeriodicWorkRequest.Builder(OfflineSyncWorker::class.java, 24L, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()

        // We want this to replace any existing job to sync offline work because we only need to sync once per day
        // and don't want duplicate jobs running.
        workManager.enqueueUniquePeriodicWork(OfflineSyncWorker.TAG, ExistingPeriodicWorkPolicy.REPLACE, syncRequest)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }

    fun injector(): AppComponent {
        return applicationInjector() as AppComponent
    }
}