package com.example.szakdogafitness.domain.di.Main

import com.example.szakdogafitness.data.main.IMainService
import com.example.szakdogafitness.domain.usecases.main.AllMainUseCases
import com.example.szakdogafitness.domain.usecases.main.FetchUserUseCase
import com.example.szakdogafitness.domain.usecases.main.FetchUsersDailyDatasUseCase
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
    fun provideMainUseCases(
        repository: IMainService,
        fetchUserUseCase: FetchUserUseCase,
        fetchUsersDailyDatasUseCase: FetchUsersDailyDatasUseCase
    ): AllMainUseCases = AllMainUseCases(
        repository,
        fetchUserUseCase,
        fetchUsersDailyDatasUseCase
    )


}