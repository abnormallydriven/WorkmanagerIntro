package com.abnormallydriven.workmanagerintro

import androidx.lifecycle.ViewModel
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.abnormallydriven.workmanagerintro.immediate.FakeUploadManager
import com.abnormallydriven.workmanagerintro.immediate.SimpleUploadWorker
import javax.inject.Inject


class MainActivityViewModel @Inject constructor(private val fakeUploadManager: FakeUploadManager,
                                                private val workManager: WorkManager) : ViewModel() {

    val liveFileCount = fakeUploadManager.liveFilesUploadedCount
    val totalFileCount = fakeUploadManager.totalFileCount

    fun isUploading() = fakeUploadManager.hasStartedUpload

    fun startSimpleUpload() {
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val uploadWork = OneTimeWorkRequest.Builder(SimpleUploadWorker::class.java)
                .setConstraints(constraints)
                .build()

        workManager.enqueue(uploadWork)
    }

}