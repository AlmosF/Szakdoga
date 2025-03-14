package com.example.szakdogafitness.domain.usecases.mealTypes

import com.example.szakdogafitness.data.mealTypes.IMealTypesService

class AllMealTypesUseCases(
    val repository: IMealTypesService,
    val fetchMealTypesUseCase: FetchMealTypesUseCase,
    val fetchNutritionsUseCase: FetchNutritionsUseCase
)