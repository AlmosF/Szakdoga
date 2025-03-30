package com.example.szakdogafitness.domain.di.Camera

import com.example.szakdogafitness.data.camera.CameraService
import com.example.szakdogafitness.data.camera.ICameraService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CameraServiceModule{


    @Binds
    @Singleton
    abstract fun bindCameraService(
        cameraService: CameraService
    ): ICameraService

}