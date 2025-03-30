package com.example.szakdogafitness.domain.di.Generative

import com.example.szakdogafitness.data.generative.GenerativeService
import com.example.szakdogafitness.data.generative.IGenerativeService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class GenerativeServiceModule{

    @Binds
    @Singleton
    abstract fun bindGenerativeService(
        generativeService: GenerativeService
    ): IGenerativeService

}