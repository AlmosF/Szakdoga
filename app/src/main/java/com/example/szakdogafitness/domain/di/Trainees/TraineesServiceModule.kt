package com.example.szakdogafitness.domain.di.Trainees

import com.example.szakdogafitness.data.trainees.ITraineesService
import com.example.szakdogafitness.data.trainees.TraineesService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TraineesServiceModule {

    @Binds
    @Singleton
    abstract fun bindTraineesService(
        traineesService: TraineesService
    ) : ITraineesService

}