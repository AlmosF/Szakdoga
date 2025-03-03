package com.example.szakdogafitness.domain.usecases.mealLog

import com.example.szakdogafitness.data.mealLog.IMealLog
import com.example.szakdogafitness.domain.model.Nutrition

class MealRemovedUseCase(private val repository: IMealLog){

    operator fun invoke(mealType: String, mealId: Int, dataMap: Map<String, List<Nutrition>>) : Map<String, List<Nutrition>> {
        return repository.removeMeal(mealType, mealId, dataMap)
    }

}