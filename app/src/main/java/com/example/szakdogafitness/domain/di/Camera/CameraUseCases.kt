package com.example.szakdogafitness.domain.di.Camera

import com.example.szakdogafitness.data.camera.ICameraService
import com.example.szakdogafitness.domain.usecases.camera.CapturePhotoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CameraUseCases {

    @Provides
    @Singleton
    fun provideCapturePhotoUseCase(repository: ICameraService): CapturePhotoUseCase =
        CapturePhotoUseCase(repository)

}