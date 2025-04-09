package com.example.szakdogafitness.domain.di.StepCount

import com.example.szakdogafitness.data.stepcount.IStepCountRepository
import com.example.szakdogafitness.data.stepcount.StepCountRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StepCountModule {

    @Binds
    @Singleton
    abstract fun bindStepCountRepository(
        impl: StepCountRepository
    ): IStepCountRepository
}