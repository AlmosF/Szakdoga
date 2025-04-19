package com.example.szakdogafitness.domain.di.Appbar

import com.example.szakdogafitness.data.appbar.AppBarService
import com.example.szakdogafitness.data.appbar.IAppBarService
import dagger.Binds
import dagger.Module
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