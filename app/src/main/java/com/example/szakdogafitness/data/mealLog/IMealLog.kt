package com.example.szakdogafitness.data.mealLog

import com.example.szakdogafitness.domain.model.Nutrition

interface IMealLog{
    fun consumeMeal(mealType: String, meal: Nutrition, dataMap: Map<String, List<Nutrition>> ) : Map<String, List<Nutrition>>
    fun removeMeal(mealType: String, index: Int, dataMap: Map<String, List<Nutrition>>) : Map<String, List<Nutrition>>
}