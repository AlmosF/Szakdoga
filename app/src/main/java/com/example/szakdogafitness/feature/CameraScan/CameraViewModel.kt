package com.example.szakdogafitness.feature.CameraScan

import android.content.Context
import androidx.camera.view.LifecycleCameraController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.Bitmap
import com.example.szakdogafitness.domain.usecases.camera.CapturePhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val capturePhoto: CapturePhotoUseCase
) : ViewModel(){

    private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()

    fun onEvent(event: CameraEvent){
        when(event){
            is CameraEvent.CapturePhoto -> {
                capturePhotoFun(
                    event.context,
                    event.cameraController
                )
            }
            CameraEvent.ResetState -> {
                _state.value = CameraState()
            }
        }
    }

    private fun capturePhotoFun(
        context: Context,
        cameraController: LifecycleCameraController
    ){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val response = capturePhoto(
                    context,
                    cameraController
                )
                _state.update {
                    it.copy(
                        image = response,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.localizedMessage ?: "",
                        isLoading = false
                    )
                }
            }
        }
    }




}

data class CameraState(
    val isLoading: Boolean = false,
    val image: Bitmap? = null,
    val error: String = ""
)

sealed class CameraEvent{
    object ResetState: CameraEvent()
    data class CapturePhoto(
        val context: Context,
        val cameraController: LifecycleCameraController
    ): CameraEvent()
}