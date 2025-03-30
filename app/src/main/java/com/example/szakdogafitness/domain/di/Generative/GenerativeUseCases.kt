package com.example.szakdogafitness.domain.di.Generative

import com.example.szakdogafitness.data.generative.IGenerativeService
import com.example.szakdogafitness.domain.usecases.generative.SendPromptUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GenerativeUseCases {

    @Provides
    @Singleton
    fun provideSendPromptUseCase(repository: IGenerativeService): SendPromptUseCase =
        SendPromptUseCase(repository)

}