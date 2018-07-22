package com.abnormallydriven.workmanagerintro.immediate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeUploadManager @Inject constructor() {

    private val mutableFileCount = MutableLiveData<Int>()

    val totalFileCount = 10

    val liveFilesUploadedCount = mutableFileCount as LiveData<Int>

    var hasStartedUpload = false

    init {
        mutableFileCount.value = 0
    }

    fun uploadFile() {
        mutableFileCount.postValue((mutableFileCount.value?.plus(1)))
    }

    fun startUpload(){
        hasStartedUpload = true
    }
}