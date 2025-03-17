package com.example.szakdogafitness.domain.di.MealTypes

import com.example.szakdogafitness.data.mealTypes.IMealTypesService
import com.example.szakdogafitness.domain.usecases.mealTypes.AddNutritionUseCase
import com.example.szakdogafitness.domain.usecases.mealTypes.AllMealTypesUseCases
import com.example.szakdogafitness.domain.usecases.mealTypes.FetchMealTypesUseCase
import com.example.szakdogafitness.domain.usecases.mealTypes.FetchNutritionByIdUseCase
import com.example.szakdogafitness.domain.usecases.mealTypes.FetchNutritionsUseCase
import com.example.szakdogafitness.domain.usecases.mealTypes.UpdateNutritionUseCase
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
    fun provideFetchNutritionById(repository: IMealTypesService): FetchNutritionByIdUseCase =
        FetchNutritionByIdUseCase(repository)

    @Provides
    @Singleton
    fun provideAddNutrition(repository: IMealTypesService): AddNutritionUseCase =
        AddNutritionUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateNutrition(repository: IMealTypesService): UpdateNutritionUseCase =
        UpdateNutritionUseCase(repository)

    @Provides
    @Singleton
    fun provideMealTypesUseCases(
        repository: IMealTypesService,
        fetchMealTypesUseCase: FetchMealTypesUseCase,
        fetchNutritionsUseCase: FetchNutritionsUseCase,
        fetchNutritionByIdUseCase: FetchNutritionByIdUseCase,
        updateNutritionUseCase: UpdateNutritionUseCase,
        addNutritionUseCase: AddNutritionUseCase
    ): AllMealTypesUseCases = AllMealTypesUseCases(
        repository,
        fetchMealTypesUseCase,
        fetchNutritionsUseCase,
        fetchNutritionByIdUseCase,
        addNutritionUseCase,
        updateNutritionUseCase
    )


}