package com.example.szakdogafitness.domain.di.Profile

import com.example.szakdogafitness.data.profile.IProfileService
import com.example.szakdogafitness.domain.usecases.profile.AllProfileUseCases
import com.example.szakdogafitness.domain.usecases.profile.FetchUserUseCase
import com.example.szakdogafitness.domain.usecases.profile.GetProfileUseCase
import com.example.szakdogafitness.domain.usecases.profile.UpdateUserUseCase
import com.example.szakdogafitness.domain.usecases.profile.UploadProfilePictureUseCase
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
    fun provideFetchUserUseCase(repository: IProfileService): FetchUserUseCase =
        FetchUserUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateUserUseCase(repository: IProfileService): UpdateUserUseCase =
        UpdateUserUseCase(repository)

    @Provides
    @Singleton
    fun provideUploadProfilePictureUseCase(repository: IProfileService): UploadProfilePictureUseCase =
        UploadProfilePictureUseCase(repository)

    @Provides
    @Singleton
    fun provideGetProfileUseCase(repository: IProfileService): GetProfileUseCase =
        GetProfileUseCase(repository)

    @Provides
    @Singleton
    fun provideAllProfileUseCases(
        repository: IProfileService,
        getProfileUseCase: GetProfileUseCase,
        fetchUser: FetchUserUseCase,
        updateUser: UpdateUserUseCase,
        uploadProfilePicture: UploadProfilePictureUseCase,
    ) : AllProfileUseCases = AllProfileUseCases(
        repository,
        getProfileUseCase,
        fetchUser,
        updateUser,
        uploadProfilePicture
    )

}