package com.example.szakdogafitness.domain.di.DailyDatas

import com.example.szakdogafitness.data.dailydatas.DailyDatasService
import com.example.szakdogafitness.data.dailydatas.IDailyDatasService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DailyDatasServiceModule{

    @Binds
    @Singleton
    abstract fun bindDailyDatasService(
        dailyDatasService: DailyDatasService
    ): IDailyDatasService

}