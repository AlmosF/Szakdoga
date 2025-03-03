package com.example.szakdogafitness.data.mealLog

import com.example.szakdogafitness.domain.model.Nutrition
import javax.inject.Inject

class MealLog @Inject constructor(

) : IMealLog {
    override fun consumeMeal(
        mealType: String,
        meal: Nutrition,
        dataMap: Map<String, List<Nutrition>>
    ): Map<String, List<Nutrition>> {
        val newConsumedMeals = dataMap.toMutableMap()
        val mealList = newConsumedMeals[mealType]?.toMutableList() ?: mutableListOf()
        mealList.add(meal)
        newConsumedMeals[mealType] = mealList
        return newConsumedMeals
    }

    override fun removeMeal(
        mealType: String,
        index: Int,
        dataMap: Map<String, List<Nutrition>>
    ): Map<String, List<Nutrition>> {
        val newConsumedMeals = dataMap.toMutableMap()
        newConsumedMeals[mealType]?.let {
            val newMealList = it.toMutableList()
            newMealList.removeAt(index)
            newConsumedMeals[mealType] = newMealList
        }
        return newConsumedMeals
    }

}