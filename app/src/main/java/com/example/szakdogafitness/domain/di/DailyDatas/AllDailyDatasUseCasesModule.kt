package com.example.szakdogafitness.domain.di.DailyDatas

import com.example.szakdogafitness.data.dailydatas.IDailyDatasService
import com.example.szakdogafitness.domain.usecases.main.FetchUsersDailyDatasUseCase
import com.example.szakdogafitness.domain.usecases.main.GenerateNewDailyDataUseCase
import com.example.szakdogafitness.domain.usecases.main.TargetWaterModifyUseCase
import com.example.szakdogafitness.domain.usecases.main.WaterIntakeModifyUseCase
import com.example.szakdogafitness.domain.usecases.mealLog.TargetKcalModifyUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AllDailyDatasUseCasesModule {


    @Provides
    @Singleton
    fun provideFetchUsersDailyDatasUseCase(repository: IDailyDatasService): FetchUsersDailyDatasUseCase =
        FetchUsersDailyDatasUseCase(repository)

    @Provides
    @Singleton
    fun provideTargetKcalModifyUseCase(repository: IDailyDatasService): TargetKcalModifyUseCase =
        TargetKcalModifyUseCase(repository)

    @Provides
    @Singleton
    fun provideTargetWaterModifyUseCase(repository: IDailyDatasService): TargetWaterModifyUseCase =
        TargetWaterModifyUseCase(repository)


    @Provides
    @Singleton
    fun provideUpdateWaterIntakeUseCase(repository: IDailyDatasService): WaterIntakeModifyUseCase =
        WaterIntakeModifyUseCase(repository)

    @Provides
    @Singleton
    fun provideGenerateNewDailyDataUseCase(repository: IDailyDatasService): GenerateNewDailyDataUseCase =
        GenerateNewDailyDataUseCase(repository)


}