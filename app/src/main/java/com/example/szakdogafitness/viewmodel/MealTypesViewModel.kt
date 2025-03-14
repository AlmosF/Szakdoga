package com.example.szakdogafitness.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szakdogafitness.domain.model.Nutrition
import com.example.szakdogafitness.domain.usecases.mealTypes.AllMealTypesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealTypesViewModel @Inject constructor(
    private val allMealTypesUseCases: AllMealTypesUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(MealTypesState())
    val state = _state.asStateFlow()

    fun onEvent(event: MealTypesEvent) {
        when(event){
            MealTypesEvent.LoadMealTypes -> {
                Log.d("MealTypesViewModel", "LoadMealTypes")
                loadMealTypes()
            }
            is MealTypesEvent.MealTypeSelected -> {
                /*_state.update {
                    it.copy(
                        selectedMealTitle = event.mealType,
                        /*selectedMealType = it.mealTypes[event.mealType]?.filter { nutrition ->
                            nutrition.foodName.contains(it.queryString, ignoreCase = true)
                        } ?: listOf()*/
                        selectedMealType = it.mealTypes[event.mealType]?.filter { nutrition ->
                            nutrition.foodName.contains(it.queryString, ignoreCase = true)
                        } ?: listOf()
                    )
                }*/
                _state.update {
                    it.copy(
                        selectedMealTitle = event.mealType,
                        /*selectedMealType = it.mealTypes[event.mealType]?.filter { nutrition ->
                            nutrition.foodName.contains(it.queryString, ignoreCase = true)
                        } ?: listOf()*/
                        selectedMealType = it.nutritions.filter { nutrition ->
                            //it.mealType.contains(event.mealType, ignoreCase = true)
                            nutrition.mealType.contains(event.mealType, ignoreCase = true) &&
                                    nutrition.foodName.contains(it.queryString, ignoreCase = true)
                        }
                    )
                }
            }

            is MealTypesEvent.NutritionSelected -> {
                _state.update { it.copy(selectedNutrition = event.nutrition) }
            }

            MealTypesEvent.ChangeDialogState -> {
                _state.update { it.copy(dialogOpen = !it.dialogOpen) }
            }

            is MealTypesEvent.SearchQuery -> {
                /*_state.update {
                    it.copy(
                        queryString = event.query,
                        selectedMealType = it.mealTypes[it.selectedMealTitle]?.filter { nutrition ->
                            nutrition.foodName.contains(event.query, ignoreCase = true)
                        } ?: listOf()
                    )
                }*/
                _state.update {
                    it.copy(
                        queryString = event.query,
                        selectedMealType = it.nutritions.filter { nutrition ->
                            nutrition.mealType.contains(it.selectedMealTitle, ignoreCase = true) &&
                                    nutrition.foodName.contains(event.query, ignoreCase = true)
                        }
                    )
                }
            }

            MealTypesEvent.SortByCalories -> {
                _state.update {
                    it.copy(
                        selectedMealType = it.selectedMealType.sortedBy { it.calories }
                    )
                }
            }

            MealTypesEvent.FetchNutritions -> {
                fetchNutritions()
            }
        }
    }

    private fun fetchNutritions(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                allMealTypesUseCases.fetchNutritionsUseCase().collect { nutritions ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            nutritions = nutritions,
                            selectedMealType = nutritions.filter { nutrition ->
                                nutrition.mealType.contains(it.selectedMealTitle, ignoreCase = true) &&
                                        nutrition.foodName.contains(it.queryString, ignoreCase = true)
                            }
                        )
                    }
                }
            } catch (e: Exception) {
                Log.d("MealTypesViewModel", "fetchNutritions: $e")
                e.printStackTrace()
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun loadMealTypes(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                allMealTypesUseCases.fetchMealTypesUseCase().collect{ mealTypes ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            mealTypes = mealTypes,
                            selectedMealType = mealTypes[it.selectedMealTitle] ?: listOf()
                        )
                    }
                }
            } catch (e: Exception) {
                Log.d("MealTypesViewModel", "loadMealTypes: $e")
                e.printStackTrace()

                _state.update { it.copy(isLoading = false) }
            }
        }
    }

}

data class MealTypesState(
    val isLoading: Boolean = false,
    val dialogOpen: Boolean = false,
    val queryString: String = "",
    val mealTypes: Map<String, List<Nutrition>> = mapOf(
        "breakfast" to listOf(),
        "morning_snack" to listOf(),
        "lunch" to listOf(),
        "afternoon_snack" to listOf(),
        "dinner" to listOf(),
        "evening_snack" to listOf()
    ),
    val nutritions: List<Nutrition> = listOf(),
    val selectedMealTitle: String = "bf",
    //val selectedMealType: List<Nutrition> = mealTypes["breakfast"] ?: listOf(),
    val selectedMealType: List<Nutrition> = nutritions.filter { it.mealType.contains("bf", ignoreCase = true) },
    val selectedNutrition: Nutrition? = null
)

sealed class MealTypesEvent{
    object LoadMealTypes: MealTypesEvent()
    object ChangeDialogState: MealTypesEvent()
    object SortByCalories: MealTypesEvent()
    object FetchNutritions: MealTypesEvent()
    data class SearchQuery(val query: String): MealTypesEvent()
    data class MealTypeSelected(val mealType: String): MealTypesEvent()
    data class NutritionSelected(val nutrition: Nutrition): MealTypesEvent()
}