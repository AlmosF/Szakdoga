package com.example.szakdogafitness.domain.di.Setup

import com.example.szakdogafitness.data.profile.IProfileService
import com.example.szakdogafitness.data.profile.ProfileService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileServiceModule {

    @Binds
    @Singleton
    abstract fun bindProfileService(
        profileService: ProfileService
    ): IProfileService

}