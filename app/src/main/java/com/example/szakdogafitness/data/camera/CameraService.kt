package com.example.szakdogafitness.data.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CameraService @Inject constructor(

) : ICameraService {
    override suspend fun capturePhoto(
        context: Context,
        cameraController: LifecycleCameraController
    ): Bitmap {
        val mainExecutor = ContextCompat.getMainExecutor(context)

        return suspendCancellableCoroutine { continuation ->
            cameraController.takePicture(mainExecutor, object: ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    val bitmap = image.toBitmap()
                    val rotatedBitmap = rotateBitmap(bitmap, image.imageInfo.rotationDegrees)
                    image.close()
                    continuation.resume(rotatedBitmap)
                }

                override fun onError(exception: ImageCaptureException) {
                    continuation.resumeWithException(exception)
                }
            })
        }
    }
    private fun rotateBitmap(bitmap: Bitmap, rotationDegrees: Int): Bitmap {
        if (rotationDegrees == 0) return bitmap

        val matrix = Matrix().apply {
            postRotate(rotationDegrees.toFloat())
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}