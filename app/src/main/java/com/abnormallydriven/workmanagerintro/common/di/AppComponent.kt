package com.abnormallydriven.workmanagerintro.common.di

import com.abnormallydriven.workmanagerintro.WorkmanagerIntroApp
import com.abnormallydriven.workmanagerintro.periodic.OfflineSyncWorker
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class,
    AppModule::class])
interface AppComponent : AndroidInjector<WorkmanagerIntroApp> {

    fun inject(worker: OfflineSyncWorker)

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<WorkmanagerIntroApp>()
}