package com.example.szakdogafitness.domain.usecases.mealTypes

import com.example.szakdogafitness.data.mealTypes.IMealTypesService
import com.example.szakdogafitness.domain.model.Nutrition

class AddNutritionUseCase(private val repository: IMealTypesService){
    suspend operator fun invoke(nutrition: Nutrition) = repository.addNutrition(nutrition)
}