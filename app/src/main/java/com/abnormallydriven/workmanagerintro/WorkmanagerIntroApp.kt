package com.abnormallydriven.workmanagerintro

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import com.abnormallydriven.workmanagerintro.common.di.DaggerAppComponent

class WorkmanagerIntroApp : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}