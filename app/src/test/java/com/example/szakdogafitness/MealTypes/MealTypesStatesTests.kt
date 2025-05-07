package com.example.szakdogafitness.MealTypes

import androidx.lifecycle.viewModelScope
import com.example.szakdogafitness.data.mealTypes.IMealTypesService
import com.example.szakdogafitness.domain.model.Nutrition
import com.example.szakdogafitness.domain.usecases.mealTypes.AddNutritionUseCase
import com.example.szakdogafitness.domain.usecases.mealTypes.AllMealTypesUseCases
import com.example.szakdogafitness.domain.usecases.mealTypes.FetchMealTypesUseCase
import com.example.szakdogafitness.domain.usecases.mealTypes.FetchNutritionByIdUseCase
import com.example.szakdogafitness.domain.usecases.mealTypes.FetchNutritionsUseCase
import com.example.szakdogafitness.domain.usecases.mealTypes.UpdateNutritionUseCase
import com.example.szakdogafitness.feature.nutrition.MealTypesEvent
import com.example.szakdogafitness.feature.nutrition.MealTypesViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.AfterTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MealTypesStatesTests {

    private lateinit var viewModel: MealTypesViewModel

    private val repository: IMealTypesService = mockk()
    private val fetchMealTypes = mockk<FetchMealTypesUseCase>(relaxed = true)
    private val fetchNutritions = mockk<FetchNutritionsUseCase>(relaxed = true)
    private val fetchNutritionById = mockk<FetchNutritionByIdUseCase>(relaxed = true)
    private val addNutrition = mockk<AddNutritionUseCase>(relaxed = true)
    private val updateNutrition = mockk<UpdateNutritionUseCase>(relaxed = true)

    val dispatcher = StandardTestDispatcher()

    @Before
    fun setup(){
        Dispatchers.setMain(dispatcher)

        val allMealTypesUseCases = AllMealTypesUseCases(
            repository,
            fetchMealTypes,
            fetchNutritions,
            fetchNutritionById,
            addNutrition,
            updateNutrition
        )

        viewModel = MealTypesViewModel(
            allMealTypesUseCases,
        )
    }

    @AfterTest
    fun tearDown(){
        Dispatchers.resetMain()
        viewModel.viewModelScope.cancel()
    }

    @Test
    fun mealFetchedAndTypesSelectedAndNutritionSelectedTests() = runTest {
        val testNutritions = listOf(
            Nutrition(foodName = "Apple", mealType = "bf"),
            Nutrition(foodName = "Banana", mealType = "l"),
        )
        coEvery { fetchNutritions.invoke() } returns flowOf(testNutritions)

        viewModel.onEvent(MealTypesEvent.FetchNutritions)
        advanceUntilIdle()
        assertFalse(viewModel.state.value.isLoading)
        assertEquals(testNutritions, viewModel.state.value.nutritions)
        viewModel.onEvent(MealTypesEvent.MealTypeSelected("bf"))
        advanceUntilIdle()
        assertEquals("bf", viewModel.state.value.selectedMealTitle)
        assertEquals(testNutritions[0], viewModel.state.value.selectedMealType[0])
        viewModel.onEvent(MealTypesEvent.NutritionSelected(testNutritions[0]))
        advanceUntilIdle()
        assertEquals(testNutritions[0], viewModel.state.value.selectedNutrition)
    }

    @Test
    fun dialogStateChangedTests() = runTest {
        viewModel.onEvent(MealTypesEvent.ChangeDialogState)
        advanceUntilIdle()
        assertTrue(viewModel.state.value.dialogOpen)
        viewModel.onEvent(MealTypesEvent.ChangeDialogState)
        advanceUntilIdle()
        assertFalse(viewModel.state.value.dialogOpen)
    }

    @Test
    fun searchQueryTests() = runTest {
        val testNutritions = listOf(
            Nutrition(foodName = "Apple", mealType = "bf", calories = 1, carbs = 1, protein = 1, fat = 1),
            Nutrition(foodName = "Banana", mealType = "l", calories = 2, carbs = 2, protein = 2, fat = 2),
            Nutrition(foodName = "Orange", mealType = "ms", calories = 3, carbs = 3, protein = 3, fat = 3),
        )
        coEvery { fetchNutritions.invoke() } returns flowOf(testNutritions)
        viewModel.onEvent(MealTypesEvent.FetchNutritions)
        advanceUntilIdle()
        assertEquals(testNutritions, viewModel.state.value.nutritions)
        viewModel.onEvent(MealTypesEvent.SearchQuery("an"))
        advanceUntilIdle()
        assertEquals(2, viewModel.state.value.nutritions.filter { it.foodName.contains("an") }.size)
    }

    @Test
    fun sortByNutrientsTests() = runTest {
        val testNutritions = listOf(
            Nutrition(foodName = "Apple", mealType = "bf", calories = 1, carbs = 1, protein = 1, fat = 1),
            Nutrition(foodName = "Orange", mealType = "bf", calories = 3, carbs = 3, protein = 3, fat = 3),
            Nutrition(foodName = "Banana", mealType = "bf", calories = 2, carbs = 2, protein = 2, fat = 2),
        )
        coEvery { fetchNutritions.invoke() } returns flowOf(testNutritions)
        viewModel.onEvent(MealTypesEvent.FetchNutritions)
        advanceUntilIdle()
        viewModel.onEvent(MealTypesEvent.MealTypeSelected("bf"))
        advanceUntilIdle()
        assertEquals(testNutritions, viewModel.state.value.nutritions)
        viewModel.onEvent(MealTypesEvent.SortByCaloriesAsc)
        advanceUntilIdle()
        assertEquals(
            testNutritions.sortedBy { it.calories },
            viewModel.state.value.selectedMealType
        )
        viewModel.onEvent(MealTypesEvent.SortByCaloriesDesc)
        advanceUntilIdle()
        assertEquals(
            testNutritions.sortedByDescending { it.calories },
            viewModel.state.value.selectedMealType
        )
        viewModel.onEvent(MealTypesEvent.SortByCarbsAsc)
        advanceUntilIdle()
        assertEquals(
            testNutritions.sortedBy { it.carbs },
            viewModel.state.value.selectedMealType
        )
        viewModel.onEvent(MealTypesEvent.SortByCarbsDesc)
        advanceUntilIdle()
        assertEquals(
            testNutritions.sortedByDescending { it.carbs },
            viewModel.state.value.selectedMealType
        )
        viewModel.onEvent(MealTypesEvent.SortByProteinAsc)
        advanceUntilIdle()
        assertEquals(
            testNutritions.sortedBy { it.protein },
            viewModel.state.value.selectedMealType
        )
        viewModel.onEvent(MealTypesEvent.SortByProteinDesc)
        advanceUntilIdle()
        assertEquals(
            testNutritions.sortedByDescending { it.protein },
            viewModel.state.value.selectedMealType
        )
        viewModel.onEvent(MealTypesEvent.SortByFatAsc)
        advanceUntilIdle()
        assertEquals(
            testNutritions.sortedBy { it.fat },
            viewModel.state.value.selectedMealType
        )
        viewModel.onEvent(MealTypesEvent.SortByFatDesc)
        advanceUntilIdle()
        assertEquals(
            testNutritions.sortedByDescending { it.fat },
            viewModel.state.value.selectedMealType
        )
        viewModel.onEvent(MealTypesEvent.SortByDefault)
        advanceUntilIdle()
        assertEquals(
            testNutritions,
            viewModel.state.value.selectedMealType
        )
    }
}