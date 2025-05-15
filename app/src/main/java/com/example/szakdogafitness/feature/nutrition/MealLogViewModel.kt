package com.example.szakdogafitness.feature.nutrition

import androidx.lifecycle.ViewModel
import com.example.szakdogafitness.domain.model.Nutrition
import com.example.szakdogafitness.domain.usecases.mealLog.AllMealLogUseCases
import com.example.szakdogafitness.ui.common.nutrition.logblock.BlockState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MealLogViewModel @Inject constructor(
    private val mealLog: AllMealLogUseCases
) : ViewModel() {
    private val _state = MutableStateFlow(MealLogState())
    val state = _state.asStateFlow()

    fun onEvent(event: MealLogEvent){
        when(event){
            is MealLogEvent.BlockStateChanged -> {
                _state.update {
                    it.copy(
                        currentBlockState = if (it.currentBlockState == event.blockState)
                            BlockState.Closed
                        else
                            event.blockState
                    )
                }
            }
            is MealLogEvent.MealConsumed -> {
                //consumeMeal(event.mealType, event.meal)
            }
            is MealLogEvent.MealRemoved -> {
                //removeMeal(event.mealType, event.index)
            }
            is MealLogEvent.SetupDemoMeal -> {
                setupDemoMeals(event.mealMap)
            }
        }
    }


    private fun setupDemoMeals(mealMap: Map<String, List<Int>>){
        //_consumedMeals.value = mealMap
        _state.update {
            it.copy(consumedMeals = mealMap)
        }
    }

}

data class MealLogState(
    val currentBlockState: BlockState = BlockState.Closed,
    val consumedMeals: Map<String, List<Int>> = emptyMap()
)

sealed class MealLogEvent{
    data class BlockStateChanged(val blockState: BlockState): MealLogEvent()
    data class MealConsumed(val mealType: String, val meal: Nutrition): MealLogEvent()
    data class MealRemoved(val mealType: String, val index: Int): MealLogEvent()
    data class SetupDemoMeal(val mealMap: Map<String, List<Int>>): MealLogEvent()
}