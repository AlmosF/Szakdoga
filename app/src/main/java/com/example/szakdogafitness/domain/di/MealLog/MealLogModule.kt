package com.example.szakdogafitness.domain.di.MealLog

import com.example.szakdogafitness.data.mealLog.IMealLog
import com.example.szakdogafitness.data.mealLog.MealLog
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MealLogModule{


    @Binds
    @Singleton
    abstract fun bindMealLog(
        mealLog: MealLog
    ): IMealLog


}