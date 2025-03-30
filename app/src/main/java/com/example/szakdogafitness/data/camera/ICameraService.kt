package com.example.szakdogafitness.data.camera

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.view.LifecycleCameraController

interface ICameraService {
    suspend fun capturePhoto(
        context: Context,
        cameraController: LifecycleCameraController
    ): Bitmap
}