package com.example.szakdogafitness.domain.di.Main

import com.example.szakdogafitness.data.main.IMain
import com.example.szakdogafitness.domain.usecases.main.AddNewDailyDataUseCase
import com.example.szakdogafitness.domain.usecases.main.AllMainUseCases
import com.example.szakdogafitness.domain.usecases.main.FetchDailyDataUseCase
import com.example.szakdogafitness.domain.usecases.main.FetchTraineesUseCase
import com.example.szakdogafitness.domain.usecases.main.FetchTrainersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AllMainUseCasesModule{

    @Provides
    @Singleton
    fun provideAddNewDailyDataUseCase(repository: IMain): AddNewDailyDataUseCase =
        AddNewDailyDataUseCase(repository)

    @Provides
    @Singleton
    fun provideFetchDailyDataUseCase(repository: IMain): FetchDailyDataUseCase =
        FetchDailyDataUseCase(repository)


    @Provides
    @Singleton
    fun provideFetchTrainersUseCase(repository: IMain): FetchTrainersUseCase =
        FetchTrainersUseCase(repository)

    @Provides
    @Singleton
    fun provideFetchTraineesUseCase(repository: IMain): FetchTraineesUseCase =
        FetchTraineesUseCase(repository)



    @Provides
    @Singleton
    fun provideAllMainUseCases(
        repository: IMain,
        fetchDailyDataUseCase: FetchDailyDataUseCase,
        addNewDailyDataUseCase: AddNewDailyDataUseCase,
        fetchTrainersUseCase: FetchTrainersUseCase,
        fetchTraineesUseCase: FetchTraineesUseCase
    ): AllMainUseCases = AllMainUseCases(
        repository,
        fetchDailyDataUseCase,
        addNewDailyDataUseCase,
        fetchTrainersUseCase,
        fetchTraineesUseCase
    )

}