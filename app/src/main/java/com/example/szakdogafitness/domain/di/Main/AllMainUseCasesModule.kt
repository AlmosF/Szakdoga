package com.example.szakdogafitness.domain.di.Main

import com.example.szakdogafitness.data.main.IMainService
import com.example.szakdogafitness.domain.usecases.main.AllMainUseCases
import com.example.szakdogafitness.domain.usecases.main.FetchUserUseCase
import com.example.szakdogafitness.domain.usecases.main.FetchUsersDailyDatasUseCase
import com.example.szakdogafitness.domain.usecases.main.GenerateNewDailyDataUseCase
import com.example.szakdogafitness.domain.usecases.main.GetTrainersUseCase
import com.example.szakdogafitness.domain.usecases.main.UpdateUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AllMainUseCasesModule {


    @Provides
    @Singleton
    fun provideFetchUserUseCase(repository: IMainService): FetchUserUseCase =
        FetchUserUseCase(repository)

    @Provides
    @Singleton
    fun provideFetchUsersDailyDatasUseCase(repository: IMainService): FetchUsersDailyDatasUseCase =
        FetchUsersDailyDatasUseCase(repository)

    @Provides
    @Singleton
    fun provideGenerateNewDailyDataUseCase(repository: IMainService): GenerateNewDailyDataUseCase =
        GenerateNewDailyDataUseCase(repository)

    @Provides
    @Singleton
    fun provideGetTrainersUseCase(repository: IMainService): GetTrainersUseCase =
        GetTrainersUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateUserUseCase(repository: IMainService): UpdateUserUseCase =
        UpdateUserUseCase(repository)

    @Provides
    @Singleton
    fun provideMainUseCases(
        repository: IMainService,
        fetchUserUseCase: FetchUserUseCase,
        fetchUsersDailyDatasUseCase: FetchUsersDailyDatasUseCase,
        generateNewDailyDataUseCase: GenerateNewDailyDataUseCase,
        getTrainersUseCase: GetTrainersUseCase,
        updateUserUseCase: UpdateUserUseCase
    ): AllMainUseCases = AllMainUseCases(
        repository,
        fetchUserUseCase,
        fetchUsersDailyDatasUseCase,
        generateNewDailyDataUseCase,
        getTrainersUseCase,
        updateUserUseCase
    )


}