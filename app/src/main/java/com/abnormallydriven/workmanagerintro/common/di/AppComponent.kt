package com.abnormallydriven.workmanagerintro.common.di

import com.abnormallydriven.workmanagerintro.MainActivityInjectorModule
import com.abnormallydriven.workmanagerintro.WorkmanagerIntroApp
import com.abnormallydriven.workmanagerintro.chained.ComplicatedCalculationWorker
import com.abnormallydriven.workmanagerintro.chained.PreCalculationWorker
import com.abnormallydriven.workmanagerintro.immediate.SimpleUploadWorker
import com.abnormallydriven.workmanagerintro.periodic.OfflineSyncWorker
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class,
    AppModule::class,
    ViewModelModule::class,
    MainActivityInjectorModule::class])
interface AppComponent : AndroidInjector<WorkmanagerIntroApp> {

    fun inject(worker: OfflineSyncWorker)
    fun inject(simpleUploadWorker: SimpleUploadWorker)
    fun inject(preCalculationWorker: PreCalculationWorker)
    fun inject(complicatedCalculationWorker: ComplicatedCalculationWorker)

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<WorkmanagerIntroApp>()
}