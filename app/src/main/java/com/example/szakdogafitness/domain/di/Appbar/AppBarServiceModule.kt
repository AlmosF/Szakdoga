package com.example.szakdogafitness.domain.di.Appbar

import com.example.szakdogafitness.data.appbar.AppBarService
import com.example.szakdogafitness.data.appbar.IAppBarService
import com.example.szakdogafitness.domain.usecases.appbar.AllAppBarUseCases
import com.example.szakdogafitness.domain.usecases.appbar.AppBarVisibilityUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBarServiceModule {

    @Binds
    @Singleton
    abstract fun bindAppBarService(
        appBarService: AppBarService
    ): IAppBarService

}