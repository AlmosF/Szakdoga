package com.example.szakdogafitness.domain.di.Appbar

import com.example.szakdogafitness.data.appbar.IAppBarService
import com.example.szakdogafitness.domain.usecases.appbar.AllAppBarUseCases
import com.example.szakdogafitness.domain.usecases.appbar.AppBarVisibilityUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AllAppBarUseCasesModule {


    @Provides
    @Singleton
    fun provideAppBarVisibilityUseCase(repository: IAppBarService): AppBarVisibilityUseCase =
        AppBarVisibilityUseCase(repository)


    @Provides
    @Singleton
    fun provideAllAppBarUseCases(
        repository: IAppBarService,
        appBarVisibilityUseCase: AppBarVisibilityUseCase
    ): AllAppBarUseCases = AllAppBarUseCases(
        repository,
        appBarVisibilityUseCase
    )


}