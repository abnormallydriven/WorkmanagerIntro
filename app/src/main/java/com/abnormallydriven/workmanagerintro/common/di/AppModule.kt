package com.abnormallydriven.workmanagerintro.common.di

import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import java.util.Random
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideRandom(): Random {
        return Random(System.currentTimeMillis())
    }

    @Provides
    @Singleton
    fun provideWorkManager(): WorkManager {
        return WorkManager.getInstance()!!
    }
}