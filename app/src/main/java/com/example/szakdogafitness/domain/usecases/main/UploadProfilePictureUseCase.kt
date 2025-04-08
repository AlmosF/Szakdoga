package com.example.szakdogafitness.domain.usecases.main

import android.net.Uri
import com.example.szakdogafitness.data.main.IMainService

class UploadProfilePictureUseCase(
    private val repository: IMainService
) {
    suspend operator fun invoke(imageUri: Uri): String {
        return repository.uploadProfilePicture(imageUri)
    }
}