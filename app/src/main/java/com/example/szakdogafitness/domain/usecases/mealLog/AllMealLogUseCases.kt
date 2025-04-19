package com.example.szakdogafitness.domain.usecases.mealLog

import com.example.szakdogafitness.data.mealLog.IMealLog

class AllMealLogUseCases (
    val repository: IMealLog,
    val mealConsumed: MealConsumedUseCase,
    val mealRemoved: MealRemovedUseCase,
    val mealReplenished: MealReplenishedUseCase
)