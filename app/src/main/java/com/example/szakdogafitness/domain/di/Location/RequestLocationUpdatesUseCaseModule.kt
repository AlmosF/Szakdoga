package com.example.szakdogafitness.domain.di.Location

import com.example.szakdogafitness.data.location.ILocationService
import com.example.szakdogafitness.domain.usecases.location.RequestLocationUpdatesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RequestLocationUpdatesUseCaseModule{

    @Provides
    @Singleton
    fun provideRequestLocationUpdatesUseCase(repository: ILocationService): RequestLocationUpdatesUseCase =
        RequestLocationUpdatesUseCase(repository)

}