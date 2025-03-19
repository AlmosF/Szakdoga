package com.example.szakdogafitness.data.mealLog

import com.example.szakdogafitness.domain.model.Nutrition
import com.google.firebase.Timestamp

interface IMealLog{
    //fun consumeMeal(mealType: String, meal: Nutrition, dataMap: Map<String, List<Nutrition>> ) : Map<String, List<Nutrition>>
    suspend fun consumeMeal(mealID: Int, mealType: String, date: Timestamp)
    fun removeMeal(mealType: String, index: Int, dataMap: Map<String, List<Nutrition>>) : Map<String, List<Nutrition>>
}