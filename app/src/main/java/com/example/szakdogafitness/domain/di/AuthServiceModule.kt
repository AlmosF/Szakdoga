package com.example.szakdogafitness.domain.di

import com.example.szakdogafitness.data.auth.AuthService
import com.example.szakdogafitness.data.auth.FirebaseAuthService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthServiceModule {

    @Binds
    @Singleton
    abstract fun bindAuthService(
        firebaseAuthService: FirebaseAuthService
    ): AuthService

}