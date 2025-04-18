package com.example.szakdogafitness.domain.di.Trainees

import com.example.szakdogafitness.data.trainees.ITraineesService
import com.example.szakdogafitness.domain.usecases.main.FetchUsersDailyDatasByIdUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AllTraineesUseCasesModule {


    @Provides
    @Singleton
    fun provideFetchUsersDailyDatasByIdUseCase(repository: ITraineesService): FetchUsersDailyDatasByIdUseCase =
        FetchUsersDailyDatasByIdUseCase(repository)

}