package com.example.szakdogafitness.domain.usecases.mealLog

import com.example.szakdogafitness.data.mealLog.IMealLog
import com.google.firebase.Timestamp

class MealConsumedUseCase(private val repository: IMealLog){

    /*operator fun invoke(mealType: String, meal: Nutrition, dataMap: Map<String, List<Nutrition>>) : Map<String, List<Nutrition>> {
        return repository.consumeMeal(mealType, meal, dataMap)
    }*/
    suspend operator fun invoke(mealID: Int, mealType: String, date: Timestamp = Timestamp.now()){
        return repository.consumeMeal(mealID, mealType, date)
    }

}