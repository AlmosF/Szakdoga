package com.example.szakdogafitness.viewmodel

import android.R.attr.prompt
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szakdogafitness.domain.model.FoodAnalysisResult
import com.example.szakdogafitness.domain.usecases.generative.SendPromptUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenerativeViewModel @Inject constructor(
    private val sendPromptUseCase: SendPromptUseCase
) : ViewModel(){

    private val _state = MutableStateFlow(GenerativeState())
    val state = _state.asStateFlow()

    private val defaultPrompt = "Analyze the provided image of food. Based on the image and the following instructions, provide the information in JSON format. \n" +
            "\n" +
            "Instructions: Identify the food in the image and estimate its nutritional content. Return the results in JSON format with the following keys: 'foodName', 'kcal', 'fat', 'carb', and 'protein'.\n" +
            "\n" +
            "Example JSON format:\n" +
            "{\n" +
            "  \\\"foodName\\\": \\\"[Food Name]\\\",\n" +
            "  \\\"kcal\\\": [Estimated kcal value],\n" +
            "  \\\"fat\\\": [Estimated fat value in grams],\n" +
            "  \\\"carb\\\": [Estimated carbohydrate value in grams],\n" +
            "  \\\"protein\\\": [Estimated protein value in grams]\n" +
            "}\n" +
            "\n" +
            "Ensure the output is strictly in the specified JSON format. Do not include any additional text or explanations outside of the JSON structure. If you are unable to determine the values, return null for that specific key.\n"

    fun onEvent(event: GenerativeEvent){
        when(event){
            GenerativeEvent.ResetState -> {
                _state.value = GenerativeState()
            }
            is GenerativeEvent.SendPrompt -> {
                sendPrompt(
                    event.bitmap
                )
            }
        }
    }

    private fun sendPrompt(bitmap: Bitmap) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val response = sendPromptUseCase(bitmap, defaultPrompt)
                _state.update { it.copy(
                    response = response,
                    isLoading = false
                ) }
            } catch (e: Exception) {
                _state.update { it.copy(
                    error = e.message ?: "An unexpected error occurred",
                    isLoading = false
                ) }
            }
        }
    }


}


data class GenerativeState(
    val isLoading: Boolean = false,
    val response: FoodAnalysisResult = FoodAnalysisResult(),
    val error: String = ""
)

sealed class GenerativeEvent{
    data class SendPrompt(val bitmap: Bitmap) : GenerativeEvent()
    object ResetState : GenerativeEvent()
}
