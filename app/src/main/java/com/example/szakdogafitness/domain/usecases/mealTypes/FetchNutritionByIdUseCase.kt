package com.example.szakdogafitness.domain.usecases.mealTypes

import com.example.szakdogafitness.data.mealTypes.IMealTypesService

class FetchNutritionByIdUseCase(private val repository: IMealTypesService){
    suspend operator fun invoke(id: String) = repository.getNutritionById(id)
}