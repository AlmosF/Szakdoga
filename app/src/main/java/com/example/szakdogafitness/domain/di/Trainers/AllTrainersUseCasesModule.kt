package com.example.szakdogafitness.domain.di.Trainers

import com.example.szakdogafitness.data.trainers.ITrainersService
import com.example.szakdogafitness.domain.usecases.trainers.ApplyToTrainerAppointmentUseCase
import com.example.szakdogafitness.domain.usecases.trainers.FetchRequestedAppoint
import com.example.szakdogafitness.domain.usecases.trainers.FetchTrainersRequestsUseCase
import com.example.szakdogafitness.domain.usecases.trainers.GetAcceptedTrainersUseCase
import com.example.szakdogafitness.domain.usecases.trainers.GetTrainersUseCase
import com.example.szakdogafitness.domain.usecases.trainers.LoadAllTrainersAllRateUseCase
import com.example.szakdogafitness.domain.usecases.trainers.LoadTrainersRateUseCase
import com.example.szakdogafitness.domain.usecases.trainers.RateTrainerUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AllTrainersUseCasesModule{

    @Singleton
    @Provides
    fun provideGetTrainersUseCase(repository: ITrainersService): GetTrainersUseCase =
        GetTrainersUseCase(repository)

    @Provides
    @Singleton
    fun provideApplyToTrainerAppointmentUseCase(repository: ITrainersService): ApplyToTrainerAppointmentUseCase =
        ApplyToTrainerAppointmentUseCase(repository)

    @Provides
    @Singleton
    fun provideFetchTrainersRequestsUseCase(repository: ITrainersService): FetchTrainersRequestsUseCase =
        FetchTrainersRequestsUseCase(repository)

    @Provides
    @Singleton
    fun provideFetchRequestedAppoint(repository: ITrainersService): FetchRequestedAppoint =
        FetchRequestedAppoint(repository)

    @Provides
    @Singleton
    fun provideAcceptedTrainersUseCase(repository: ITrainersService): GetAcceptedTrainersUseCase =
        GetAcceptedTrainersUseCase(repository)

    @Provides
    @Singleton
    fun provideRateTrainerUseCase(repository: ITrainersService): RateTrainerUseCase =
        RateTrainerUseCase(repository)

    @Provides
    @Singleton
    fun provideLoadTrainersRateUseCase(repository: ITrainersService): LoadTrainersRateUseCase =
        LoadTrainersRateUseCase(repository)

    @Provides
    @Singleton
    fun provideLoadAllTrainersAllRateUseCase(repository: ITrainersService): LoadAllTrainersAllRateUseCase =
        LoadAllTrainersAllRateUseCase(repository)

}