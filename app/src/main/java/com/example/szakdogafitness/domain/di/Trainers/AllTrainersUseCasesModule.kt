package com.example.szakdogafitness.domain.di.Trainers

import com.example.szakdogafitness.data.trainers.ITrainersService
import com.example.szakdogafitness.domain.usecases.main.GetTrainersUseCase
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


}