package com.example.szakdogafitness.domain.usecases.camera

import android.content.Context
import androidx.camera.view.LifecycleCameraController
import com.example.szakdogafitness.data.camera.ICameraService

class CapturePhotoUseCase(private val repository: ICameraService){
    suspend operator fun invoke(
        context: Context,
        cameraController: LifecycleCameraController,
    ) = repository.capturePhoto(
        context,
        cameraController
    )
}