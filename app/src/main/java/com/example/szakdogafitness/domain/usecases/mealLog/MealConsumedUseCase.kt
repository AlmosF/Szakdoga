package com.example.szakdogafitness.domain.usecases.mealLog

import com.example.szakdogafitness.data.mealLog.IMealLog
import com.example.szakdogafitness.domain.model.Nutrition

class MealConsumedUseCase(private val repository: IMealLog){

    operator fun invoke(mealType: String, meal: Nutrition, dataMap: Map<String, List<Nutrition>>) : Map<String, List<Nutrition>> {
        return repository.consumeMeal(mealType, meal, dataMap)
    }

}