package com.example.szakdogafitness.domain.usecases.profile

import android.net.Uri
import com.example.szakdogafitness.data.profile.IProfileService

class UploadProfilePictureUseCase(
    private val repository: IProfileService
) {
    suspend operator fun invoke(imageUri: Uri): String {
        return repository.uploadProfilePicture(imageUri)
    }
}