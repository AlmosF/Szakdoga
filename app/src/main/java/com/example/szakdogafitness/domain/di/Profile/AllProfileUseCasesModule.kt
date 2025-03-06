package com.example.szakdogafitness.domain.di.Profile

import com.example.szakdogafitness.data.profile.IProfileService
import com.example.szakdogafitness.domain.usecases.profile.AllProfileUseCases
import com.example.szakdogafitness.domain.usecases.profile.GetProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AllProfileUseCasesModule{


    @Provides
    @Singleton
    fun provideGetProfileUseCase(repository: IProfileService): GetProfileUseCase =
        GetProfileUseCase(repository)

    @Provides
    @Singleton
    fun provideAllProfileUseCases(
        repository: IProfileService,
        getProfileUseCase: GetProfileUseCase
    ) : AllProfileUseCases = AllProfileUseCases(
        repository,
        getProfileUseCase
    )

}