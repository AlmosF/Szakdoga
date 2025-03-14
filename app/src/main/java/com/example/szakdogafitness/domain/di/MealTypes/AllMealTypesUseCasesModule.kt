package com.example.szakdogafitness.domain.di.MealTypes

import com.example.szakdogafitness.data.mealTypes.IMealTypesService
import com.example.szakdogafitness.domain.usecases.mealTypes.AllMealTypesUseCases
import com.example.szakdogafitness.domain.usecases.mealTypes.FetchMealTypesUseCase
import com.example.szakdogafitness.domain.usecases.mealTypes.FetchNutritionsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AllMealTypesUseCasesModule {

    @Provides
    @Singleton
    fun provideFetchMealTypes(repository: IMealTypesService): FetchMealTypesUseCase =
        FetchMealTypesUseCase(repository)

    @Provides
    @Singleton
    fun provideFetchNutritions(repository: IMealTypesService): FetchNutritionsUseCase =
        FetchNutritionsUseCase(repository)

    @Provides
    @Singleton
    fun provideMealTypesUseCases(
        repository: IMealTypesService,
        fetchMealTypesUseCase: FetchMealTypesUseCase,
        fetchNutritionsUseCase: FetchNutritionsUseCase
    ): AllMealTypesUseCases = AllMealTypesUseCases(
        repository,
        fetchMealTypesUseCase,
        fetchNutritionsUseCase
    )


}