package com.example.szakdogafitness.data.generative

import android.graphics.Bitmap
import com.example.szakdogafitness.domain.model.FoodAnalysisResult

interface IGenerativeService {

    suspend fun sendPrompt(
        image: Bitmap,
        prompt: String
    ): FoodAnalysisResult

}