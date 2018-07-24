package com.abnormallydriven.workmanagerintro

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.abnormallydriven.workmanagerintro.chained.ComplicatedCalculationWorker
import com.abnormallydriven.workmanagerintro.chained.ComplicatedCalculationWorker.Companion.RESULT_KEY
import com.abnormallydriven.workmanagerintro.chained.PreCalculationWorker
import com.abnormallydriven.workmanagerintro.immediate.FakeUploadManager
import com.abnormallydriven.workmanagerintro.immediate.SimpleUploadWorker
import javax.inject.Inject


class MainActivityViewModel @Inject constructor(private val fakeUploadManager: FakeUploadManager,
                                                private val workManager: WorkManager) : ViewModel() {

    val liveFileCount = fakeUploadManager.liveFilesUploadedCount
    val totalFileCount = fakeUploadManager.totalFileCount

    fun isUploading() = fakeUploadManager.hasStartedUpload

    var analysisInProgress: MutableLiveData<Int> = MutableLiveData()

    init {
        analysisInProgress.value = 0
    }

    fun startSimpleUpload() {
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val uploadWork = OneTimeWorkRequest.Builder(SimpleUploadWorker::class.java)
                .setConstraints(constraints)
                .build()

        workManager.enqueue(uploadWork)
    }

    fun startComplicatedAnalysis() {
        val stageOneWorker = OneTimeWorkRequest.Builder(PreCalculationWorker::class.java)
                .build()

        val stageTwoWorker = OneTimeWorkRequest.Builder(ComplicatedCalculationWorker::class.java)
                .build()

        //We use the "Keep" existing work policy because we dont want these complicated piece of work to run
        //if its already in progress.
        workManager.beginUniqueWork("Complicated_Analysis_Chain_Work", ExistingWorkPolicy.KEEP, stageOneWorker)
                .then(stageTwoWorker).enqueue()

        val stageTwoLiveStatus = workManager.getStatusById(stageTwoWorker.id)

        val statusesForUniqueWork = workManager.getStatusesForUniqueWork("Complicated_Analysis_Chain_Work")
        statusesForUniqueWork.observeForever(object : Observer<MutableList<WorkStatus>> {
            override fun onChanged(t: MutableList<WorkStatus>) {
                var stagesCompleted = 0
                var stagesRunning = 0
                t.forEach { workStatus ->
                    if (workStatus.state == State.SUCCEEDED) {
                        stagesCompleted++
                    }

                    if (workStatus.state == State.RUNNING) {
                        stagesRunning++
                    }
                    Log.d("ChainStatusObserver", "Status: ${workStatus.state}")
                }

                Log.d("ChainStatusObserver", "*******************************")


                if (stagesCompleted == 2) {
                    analysisInProgress.postValue(100)
                    statusesForUniqueWork.removeObserver(this)
                } else if (stagesRunning != 0) {
                    if (stagesCompleted == 1) {
                        analysisInProgress.postValue(75)
                    } else {
                        analysisInProgress.postValue(25)
                    }
                }
            }
        })


        stageTwoLiveStatus.observeForever(object : Observer<WorkStatus> {
            override fun onChanged(workStatus: WorkStatus) {
                if (workStatus.state == State.SUCCEEDED) {
                    val resultString = workStatus.outputData.getString(RESULT_KEY, null)

                    //do something with our result
                    if (resultString != null) {
                        Log.d("mainActivityViewModel", "Result is: $resultString")
                    }

                    workManager.getStatusById(stageTwoWorker.id).removeObserver(this)
                }
            }
        })


    }
}