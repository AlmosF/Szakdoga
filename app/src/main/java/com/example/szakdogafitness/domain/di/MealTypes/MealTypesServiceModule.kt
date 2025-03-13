package com.example.szakdogafitness.domain.di.MealTypes

import com.example.szakdogafitness.data.mealTypes.IMealTypesService
import com.example.szakdogafitness.data.mealTypes.MealTypesService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MealTypesServiceModule {

    @Binds
    @Singleton
    abstract fun bindMealTypesService(
        mealTypesService: MealTypesService
    ) : IMealTypesService

}