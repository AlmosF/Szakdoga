package com.example.szakdogafitness.domain.usecases.mealTypes

import com.example.szakdogafitness.data.mealTypes.IMealTypesService

class FetchMealTypesUseCase(private val repository: IMealTypesService){
    suspend operator fun invoke() = repository.getMealTypes()
}