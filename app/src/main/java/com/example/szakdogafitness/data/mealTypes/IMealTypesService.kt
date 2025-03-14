package com.example.szakdogafitness.data.mealTypes

import com.example.szakdogafitness.domain.model.Nutrition
import kotlinx.coroutines.flow.Flow


interface IMealTypesService {
    fun getMealTypes(): Flow<Map<String, List<Nutrition>>>
    fun getNutritions(): Flow<List<Nutrition>>
}