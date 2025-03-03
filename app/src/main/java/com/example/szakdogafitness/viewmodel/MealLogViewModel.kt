package com.example.szakdogafitness.viewmodel

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
    //private val _currentBlockState = MutableStateFlow(BlockState.Closed)
    //private val _currentBlockState = MutableStateFlow(MealLogState())
    //val currentBlockState = _currentBlockState.asStateFlow()
//
    //private val _consumedMeals = MutableStateFlow(mapOf<String, List<Nutrition>>())
    //val consumedMeals = _consumedMeals.asStateFlow()

    private val _state = MutableStateFlow(MealLogState())
    val state = _state.asStateFlow()

    fun onEvent(event: MealLogEvent){
        when(event){
            is MealLogEvent.BlockStateChanged -> {
                /*_currentBlockState.update {
                    it.copy(currentBlockState = if (it.currentBlockState == event.blockState)
                        BlockState.Closed
                    else
                        event.blockState
                    )
                }*/
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
                consumeMeal(event.mealType, event.meal)
            }
            is MealLogEvent.MealRemoved -> {
                removeMeal(event.mealType, event.index)
            }
            is MealLogEvent.SetupDemoMeal -> {
                setupDemoMeals(event.mealMap)
            }
        }
    }

    private fun consumeMeal(mealType: String, meal: Nutrition){
        /*val newConsumedMeals = mealLog.mealConsumedUseCase(mealType, meal, _consumedMeals.value)
        _consumedMeals.value = newConsumedMeals*/
        val newConsumedMeals = mealLog.mealConsumedUseCase(mealType, meal, _state.value.consumedMeals)
        _state.update {
            it.copy(consumedMeals = newConsumedMeals)
        }
    }

    private fun setupDemoMeals(mealMap: Map<String, List<Nutrition>>){
        //_consumedMeals.value = mealMap
        _state.update {
            it.copy(consumedMeals = mealMap)
        }
    }


    private fun removeMeal(mealType: String, index: Int){
        /*val newConsumedMeals = mealLog.mealRemovedUseCase(mealType, index, _consumedMeals.value)
        _consumedMeals.value = newConsumedMeals*/
        val newConsumedMeals = mealLog.mealRemovedUseCase(mealType, index, _state.value.consumedMeals)
        _state.update {
            it.copy(consumedMeals = newConsumedMeals)
        }
    }




}

data class MealLogState(
    val currentBlockState: BlockState = BlockState.Closed,
    val consumedMeals: Map<String, List<Nutrition>> = emptyMap()
)

sealed class MealLogEvent{
    data class BlockStateChanged(val blockState: BlockState): MealLogEvent()
    data class MealConsumed(val mealType: String, val meal: Nutrition): MealLogEvent()
    data class MealRemoved(val mealType: String, val index: Int): MealLogEvent()
    data class SetupDemoMeal(val mealMap: Map<String, List<Nutrition>>): MealLogEvent()
}