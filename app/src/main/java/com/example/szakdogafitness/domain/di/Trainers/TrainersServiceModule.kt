package com.example.szakdogafitness.domain.di.Trainers

import com.example.szakdogafitness.data.trainers.ITrainersService
import com.example.szakdogafitness.data.trainers.TrainersService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TrainersServiceModule{

    @Binds
    @Singleton
    abstract fun bindTrainersService(
        trainersService: TrainersService
    ): ITrainersService

}