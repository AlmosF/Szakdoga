package com.example.szakdogafitness.feature.nutrition

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szakdogafitness.domain.model.Nutrition
import com.example.szakdogafitness.domain.usecases.mealTypes.AllMealTypesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealTypesViewModel @Inject constructor(
    private val allMealTypesUseCases: AllMealTypesUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(MealTypesState())
    val state = _state.asStateFlow()

    private val _sortState = MutableStateFlow(MealTypesSortState())
    val sortState = _sortState.asStateFlow()

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
                _sortState.update {
                    MealTypesSortState()
                }
                Log.d("SelectedMealTitle", "${_state.value.selectedMealTitle}")
            }

            is MealTypesEvent.NutritionSelected -> {
                _state.update { it.copy(selectedNutrition = event.nutrition) }
                Log.e("MealTypesViewModel", "NutritionSelected - EVENT: ${event.nutrition}")
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

            MealTypesEvent.SortByCaloriesDesc -> {
                _state.update {
                    it.copy(
                        selectedMealType = it.selectedMealType.sortedByDescending { it.calories }
                    )
                }
                _sortState.update {
                    MealTypesSortState().copy(
                        sortedByCalDesc = true,
                        sortedByDefault = false
                    )
                }
            }

            MealTypesEvent.SortByCaloriesAsc -> {
                _state.update {
                    it.copy(
                        selectedMealType = it.selectedMealType.sortedBy { it.calories }
                    )
                }
                _sortState.update {
                    MealTypesSortState().copy(
                        sortedByCalAsc = true,
                        sortedByDefault = false
                    )
                }
            }

            MealTypesEvent.SortByCarbsAsc -> {
                _state.update {
                    it.copy(
                        selectedMealType = it.selectedMealType.sortedBy { it.carbs }
                    )
                }
                _sortState.update {
                    MealTypesSortState().copy(
                        sortedByCarbsAsc = true,
                        sortedByDefault = false
                    )
                }
            }
            MealTypesEvent.SortByCarbsDesc -> {
                _state.update {
                    it.copy(
                        selectedMealType = it.selectedMealType.sortedByDescending { it.carbs }
                    )
                }
                _sortState.update {
                    MealTypesSortState().copy(
                        sortedByCarbsDesc = true,
                        sortedByDefault = false
                    )
                }
            }
            MealTypesEvent.SortByFatAsc -> {
                _state.update {
                    it.copy(
                        selectedMealType = it.selectedMealType.sortedBy { it.fat }
                    )
                }
                _sortState.update {
                    MealTypesSortState().copy(
                        sortedByFatAsc = true,
                        sortedByDefault = false
                    )
                }
            }
            MealTypesEvent.SortByFatDesc -> {
                _state.update {
                    it.copy(
                        selectedMealType = it.selectedMealType.sortedByDescending { it.fat }
                    )
                }
                _sortState.update {
                    MealTypesSortState().copy(
                        sortedByFatDesc = true,
                        sortedByDefault = false
                    )
                }
            }
            MealTypesEvent.SortByProteinAsc -> {
                _state.update {
                    it.copy(
                        selectedMealType = it.selectedMealType.sortedBy { it.protein }
                    )
                }
                _sortState.update {
                    MealTypesSortState().copy(
                        sortedByProteinAsc = true,
                        sortedByDefault = false
                    )
                }
            }
            MealTypesEvent.SortByProteinDesc -> {
                _state.update {
                    it.copy(
                        selectedMealType = it.selectedMealType.sortedByDescending { it.protein }
                    )
                }
                _sortState.update {
                    MealTypesSortState().copy(
                        sortedByProteinDesc = true,
                        sortedByDefault = false
                    )
                }
            }
            MealTypesEvent.SortByDefault -> {
                _state.update {
                    it.copy(
                        selectedMealType = it.nutritions.filter { nutrition ->
                            nutrition.mealType.contains(it.selectedMealTitle, ignoreCase = true) &&
                                    nutrition.foodName.contains(it.queryString, ignoreCase = true)
                        }
                    )
                }
                _sortState.update {
                    MealTypesSortState()
                }
            }

            MealTypesEvent.FetchNutritions -> {
                fetchNutritions()
            }

            is MealTypesEvent.FetchNutritionDetail -> {
                fetchNutritionDetail(event.id)
            }

            is MealTypesEvent.EditSelectedNutrition -> {
                _state.update { it.copy(selectedNutrition = event.nutrition) }
            }
            MealTypesEvent.UpdateNutrition -> {
                updateNutrition()
            }

            is MealTypesEvent.DeleteNutrition -> {
                TODO()
            }

            MealTypesEvent.AddNutrition -> {
                addNutrition()
            }

        }
    }

    private fun addNutrition() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val nutrition = _state.value.selectedNutrition ?: return@launch
                allMealTypesUseCases.addNutritionUseCase(nutrition)
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                Log.d("MealTypesViewModel", "addNutrition: $e")
                e.printStackTrace()
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun updateNutrition() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val nutrition = _state.value.selectedNutrition ?: return@launch
                allMealTypesUseCases.updateNutritionUseCase(nutrition)
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                Log.d("MealTypesViewModel", "updateNutrition: $e")
                e.printStackTrace()
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun fetchNutritionDetail(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                allMealTypesUseCases.fetchNutritionByIdUseCase(id = id).collect { nutrition ->
                    _state.update { it.copy(isLoading = false, selectedNutrition = nutrition) }
                }
            } catch (e: Exception) {
                Log.d("MealTypesViewModel", "fetchNutritionDetail: $e")
                e.printStackTrace()
                _state.update { it.copy(isLoading = false) }
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
                            },
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

data class MealTypesSortState(
    val sortedByCalAsc : Boolean = false,
    val sortedByCalDesc : Boolean = false,
    val sortedByProteinAsc : Boolean = false,
    val sortedByProteinDesc : Boolean = false,
    val sortedByCarbsAsc : Boolean = false,
    val sortedByCarbsDesc : Boolean = false,
    val sortedByFatAsc : Boolean = false,
    val sortedByFatDesc : Boolean = false,
    val sortedByFiberAsc : Boolean = false,
    val sortedByFiberDesc : Boolean = false,
    val sortedByDefault: Boolean = true,
)

sealed class MealTypesEvent{
    object LoadMealTypes: MealTypesEvent()
    object ChangeDialogState: MealTypesEvent()
    //SORTING
    object SortByCaloriesAsc: MealTypesEvent()
    object SortByCaloriesDesc: MealTypesEvent()
    object SortByProteinAsc: MealTypesEvent()
    object SortByProteinDesc: MealTypesEvent()
    object SortByCarbsAsc: MealTypesEvent()
    object SortByCarbsDesc: MealTypesEvent()
    object SortByFatAsc: MealTypesEvent()
    object SortByFatDesc: MealTypesEvent()
    object SortByDefault: MealTypesEvent()
    //SORTING
    object FetchNutritions: MealTypesEvent()
    object UpdateNutrition: MealTypesEvent()
    object AddNutrition: MealTypesEvent()
    data class DeleteNutrition(val id : String): MealTypesEvent()
    data class EditSelectedNutrition(val nutrition: Nutrition): MealTypesEvent()
    data class FetchNutritionDetail(val id: String): MealTypesEvent()
    data class SearchQuery(val query: String): MealTypesEvent()
    data class MealTypeSelected(val mealType: String): MealTypesEvent()
    data class NutritionSelected(val nutrition: Nutrition): MealTypesEvent()
}