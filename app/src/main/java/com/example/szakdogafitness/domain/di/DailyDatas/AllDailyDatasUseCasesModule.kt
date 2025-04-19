package com.example.szakdogafitness.domain.di.DailyDatas

import com.example.szakdogafitness.data.dailydatas.IDailyDatasService
import com.example.szakdogafitness.domain.usecases.dailyDatas.AllDailyDatasUseCases
import com.example.szakdogafitness.domain.usecases.dailyDatas.FetchUsersDailyDatasUseCase
import com.example.szakdogafitness.domain.usecases.dailyDatas.GenerateNewDailyDataUseCase
import com.example.szakdogafitness.domain.usecases.dailyDatas.TargetWaterModifyUseCase
import com.example.szakdogafitness.domain.usecases.dailyDatas.WaterIntakeModifyUseCase
import com.example.szakdogafitness.domain.usecases.dailyDatas.TargetKcalModifyUseCase
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

    @Provides
    @Singleton
    fun provideAllDailyDatasUseCases(
        repository: IDailyDatasService,
        fetchUsersDailyDatas: FetchUsersDailyDatasUseCase,
        generateNewDailyData: GenerateNewDailyDataUseCase,
        targetKcalModify: TargetKcalModifyUseCase,
        targetWaterModify: TargetWaterModifyUseCase,
        waterIntakeModify: WaterIntakeModifyUseCase
    ) = AllDailyDatasUseCases(
        repository,
        fetchUsersDailyDatas,
        generateNewDailyData,
        targetKcalModify,
        targetWaterModify,
        waterIntakeModify
    )


}