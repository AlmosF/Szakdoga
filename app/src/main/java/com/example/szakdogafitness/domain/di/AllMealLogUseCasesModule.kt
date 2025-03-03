package com.example.szakdogafitness.domain.di

import com.example.szakdogafitness.data.mealLog.IMealLog
import com.example.szakdogafitness.data.mealLog.MealLog
import com.example.szakdogafitness.domain.usecases.mealLog.AllMealLogUseCases
import com.example.szakdogafitness.domain.usecases.mealLog.MealConsumedUseCase
import com.example.szakdogafitness.domain.usecases.mealLog.MealRemovedUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AllMealLogUseCasesModule {

    @Provides
    @Singleton
    fun provideMealConsumedUseCase(repository: MealLog): MealConsumedUseCase =
        MealConsumedUseCase(repository)

    @Provides
    @Singleton
    fun provideMealRemovedUseCase(repository: MealLog): MealRemovedUseCase =
        MealRemovedUseCase(repository)


    @Provides
    @Singleton
    fun provideAllMealLogUseCases(
        repository: IMealLog,
        mealConsumedUseCase: MealConsumedUseCase,
        mealRemovedUseCase: MealRemovedUseCase
    ): AllMealLogUseCases = AllMealLogUseCases(
        repository,
        mealConsumedUseCase,
        mealRemovedUseCase
    )

}