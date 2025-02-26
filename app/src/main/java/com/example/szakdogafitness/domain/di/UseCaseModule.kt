package com.example.szakdogafitness.domain.di

import com.example.szakdogafitness.domain.usecases.IsEmailValidUseCase
import com.example.szakdogafitness.domain.usecases.PasswordsMatchUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideIsEmailValidUseCase(): IsEmailValidUseCase = IsEmailValidUseCase()

    @Provides
    @Singleton
    fun providePasswordsMatchUseCase(): PasswordsMatchUseCase = PasswordsMatchUseCase()


}