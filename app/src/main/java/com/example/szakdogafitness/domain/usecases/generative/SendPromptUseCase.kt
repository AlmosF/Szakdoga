package com.example.szakdogafitness.domain.usecases.generative

import android.graphics.Bitmap
import com.example.szakdogafitness.data.generative.IGenerativeService

class SendPromptUseCase(private val repository: IGenerativeService){
    suspend operator fun invoke(
        bitmap: Bitmap,
        prompt: String
    ) = repository.sendPrompt(bitmap, prompt)
}