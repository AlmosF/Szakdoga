package com.example.szakdogafitness.data.generative

import android.graphics.Bitmap
import com.example.szakdogafitness.BuildConfig
import com.example.szakdogafitness.domain.model.FoodAnalysisResult
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import org.json.JSONObject
import javax.inject.Inject

class GenerativeService @Inject constructor(

) : IGenerativeService{

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.API_KEY
    )

    override suspend fun sendPrompt(
        image: Bitmap,
        prompt: String
    ): FoodAnalysisResult {
        try {
            val response = generativeModel.generateContent(
                content {
                    image(image)
                    text(prompt)
                }
            )
            val jsonResponse = response.text ?: ""
            val cleanJson = jsonResponse.trim().removeSurrounding("```json", "```")
            return parseJsonToFoodAnalysisResult(cleanJson)
        } catch (e: Exception) {
            throw Exception("Error analyzing food: ${e.message}")
        }
    }
    private fun parseJsonToFoodAnalysisResult(json: String): FoodAnalysisResult {
        val jsonObject = JSONObject(json)
        return FoodAnalysisResult(
            foodName = jsonObject.optString("foodName", "Unknown"),
            servingSize = jsonObject.optInt("servingSize", 0),
            kcal = jsonObject.optInt("kcal", 0),
            fat = jsonObject.optInt("fat", 0),
            carb = jsonObject.optInt("carb", 0),
            protein = jsonObject.optInt("protein", 0)
        )
    }

}