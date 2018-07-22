package com.abnormallydriven.workmanagerintro.common.di

import com.abnormallydriven.workmanagerintro.WorkmanagerIntroApp
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class])
interface AppComponent : AndroidInjector<WorkmanagerIntroApp> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<WorkmanagerIntroApp>()

}