package com.example.szakdogafitness.domain.di

import com.example.szakdogafitness.data.setup.ISetupService
import com.example.szakdogafitness.data.setup.SetupService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SetupServiceModule {

    @Binds
    @Singleton
    abstract fun bindSetupService(
        setupService: SetupService
    ): ISetupService
}