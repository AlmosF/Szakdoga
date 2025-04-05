package com.example.szakdogafitness.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.usecases.location.RequestLocationUpdatesUseCase
import com.example.szakdogafitness.domain.usecases.main.FetchUsersDailyDatasUseCase
import com.example.szakdogafitness.domain.usecases.main.GenerateNewDailyDataUseCase
import com.example.szakdogafitness.domain.usecases.main.TargetWaterModifyUseCase
import com.example.szakdogafitness.domain.usecases.main.WaterIntakeModifyUseCase
import com.example.szakdogafitness.domain.usecases.mealLog.MealConsumedUseCase
import com.example.szakdogafitness.domain.usecases.mealLog.MealReplenishedUseCase
import com.example.szakdogafitness.domain.usecases.mealLog.TargetKcalModifyUseCase
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyDatasViewModel @Inject constructor(
    private val fetchUsersDailyDatas: FetchUsersDailyDatasUseCase,
    private val generateNewDailyData: GenerateNewDailyDataUseCase,
    private val mealConsumedUseCase: MealConsumedUseCase,
    private val mealReplenishedUseCase: MealReplenishedUseCase,
    private val targetKcalModifyUseCase: TargetKcalModifyUseCase,
    private val targetWaterModifyUseCase: TargetWaterModifyUseCase,
    private val waterIntakeModifyUseCase: WaterIntakeModifyUseCase,
    private val location: RequestLocationUpdatesUseCase
) : ViewModel(){

    private val _state = MutableStateFlow(DailyDatasState())
    val state = _state.asStateFlow()

    fun onEvent(event: DailyDatasEvent){
        when(event){
            DailyDatasEvent.GenerateNewDailyData -> {
                generateNewDailyData()
            }
            DailyDatasEvent.LoadDailyDatas -> {
                loadDailyDatas()
            }
            DailyDatasEvent.NextDay -> {
                if (state.value.isNextDayAvailable){
                    val nextDailyData = state.value.dailyDataList[state.value.dailyDataList.indexOf(state.value.currentDailyData) + 1]
                    _state.update { it.copy(
                        lifeStyleTitle = nextDailyData.date.toDate().toString().substring(4, 10),
                        currentDailyData = nextDailyData,
                    ) }
                    buttonsAvailability()
                }
            }
            DailyDatasEvent.PreviousDay -> { // FIXME: No check here..??
                val previousDailyData = state.value.dailyDataList[state.value.dailyDataList.indexOf(state.value.currentDailyData) - 1]
                _state.update { it.copy(
                    lifeStyleTitle = previousDailyData.date.toDate().toString().substring(4, 10),
                    currentDailyData = previousDailyData,
                ) }
                buttonsAvailability()
            }

            is DailyDatasEvent.AddNutrition -> {
                addNutrition(event.nutritionID, event.mealType)
            }
            is DailyDatasEvent.ReplenishNutrition -> {
                replenishNutrition(event.nutritionID, event.mealType, event.date)
            }

            DailyDatasEvent.SwitchCurrentDailyDataToLatest -> {
                _state.update { it.copy(
                    currentDailyData = state.value.dailyDataList.last(),
                ) }
            }

            is DailyDatasEvent.UpdateTargetKcal -> {
                updateTargetKcal(event.targetKcal, event.date)
            }

            is DailyDatasEvent.UpdateTargetWater -> {
                updateTargetWater(event.targetWater, event.date)
            }

            is DailyDatasEvent.UpdateWaterIntake -> {
                updateWaterIntake(event.waterIntake, event.date)
            }

            DailyDatasEvent.StartLocationMonitoring -> {
                startLocationMonitoring()
            }
        }
    }

    fun startLocationMonitoring(){
        viewModelScope.launch {
            location().collectLatest { pos ->
                Log.d("Location", "Location: $pos")
            }
        }
    }

    private fun updateWaterIntake(waterIntake: Double, timestamp: Timestamp) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                waterIntakeModifyUseCase.invoke(waterIntake, timestamp)
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun updateTargetWater(targetWater: Double, timestamp: Timestamp) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                targetWaterModifyUseCase.invoke(targetWater, timestamp)
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun updateTargetKcal(targetKcal: Int, timestamp: Timestamp) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                targetKcalModifyUseCase.invoke(targetKcal, timestamp)
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun addNutrition(nutritionID: Int, mealType: String){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                mealConsumedUseCase.invoke(nutritionID, mealType)
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun replenishNutrition(nutritionID: Int, mealType: String, date: Timestamp){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                mealReplenishedUseCase.invoke(nutritionID, mealType, date)
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception){
                e.printStackTrace()
                _state.update { it.copy(isLoading = false) }
            }
        }
    }


    private fun loadDailyDatas(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, currentDailyData = DailyData()) }
            try {
                fetchUsersDailyDatas.invoke().collect { dailyDataList ->
                    if (dailyDataList.isNotEmpty()){

                        if (state.value.dailyDataList.isNotEmpty()){
                            Log.d("DailyDatas", "DailyDataList is not empty - Just updating")
                            val currentDailyData = dailyDataList.find { it.date.toDate() == state.value.currentDailyData.date.toDate() } ?: dailyDataList.last()

                            val latestDate = currentDailyData.date.toDate().toString().substring(4, 10)
                            Log.d("DailyDatas", "CurrentDailyData: $currentDailyData")
                            Log.d("DailyDatas", "LatestDate: $latestDate")
                            _state.update {
                                it.copy(
                                    dailyDataList = dailyDataList,
                                    currentDailyData = currentDailyData,
                                    lifeStyleTitle = latestDate,
                                    isLoading = false
                                )
                            }
                        } else {
                            Log.d("DailyDatas", "DailyDataList is empty - First Start")
                            val latestDailyData = dailyDataList.last()
                            val latestDate = latestDailyData.date.toDate().toString().substring(4, 10)
                            _state.update {
                                it.copy(
                                    dailyDataList = dailyDataList,
                                    currentDailyData = latestDailyData,
                                    lifeStyleTitle = latestDate,
                                    isLoading = false
                                )
                            }
                            generateNewDailyData()
                        }
                        buttonsAvailability()
                        //generateNewDailyData() //?? - itt volt 04.02
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun generateNewDailyData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                generateNewDailyData.invoke()
                val latestDailyData = _state.value.dailyDataList.last()
                val latestDate = latestDailyData.date.toDate().toString().substring(4, 10)
                Log.d("DailyDatas", "GENERATING: $latestDailyData")
                _state.update { it.copy(
                    isLoading = false,
                    currentDailyData = latestDailyData,
                    lifeStyleTitle = latestDate,
                ) }
                buttonsAvailability()
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun buttonsAvailability(){
        val dailyDatas = state.value.dailyDataList
        val currentDailyData = state.value.currentDailyData
        val isNextDayAvailable = dailyDatas.indexOf(currentDailyData) < dailyDatas.size - 1
        val isPreviousDayAvailable = dailyDatas.indexOf(currentDailyData) > 0
        _state.value = state.value.copy(
            isNextDayAvailable = isNextDayAvailable,
            isPreviousDayAvailable = isPreviousDayAvailable
        )
    }


}

sealed class DailyDatasEvent{
    data class AddNutrition(val nutritionID: Int, val mealType: String) : DailyDatasEvent()
    data class ReplenishNutrition(val nutritionID: Int, val mealType: String, val date: Timestamp) : DailyDatasEvent()
    data class UpdateTargetKcal(val targetKcal: Int, val date: Timestamp) : DailyDatasEvent()
    data class UpdateTargetWater(val targetWater: Double, val date: Timestamp) : DailyDatasEvent()
    data class UpdateWaterIntake(val waterIntake: Double, val date: Timestamp) : DailyDatasEvent()
    object LoadDailyDatas: DailyDatasEvent()
    object StartLocationMonitoring: DailyDatasEvent()
    object NextDay: DailyDatasEvent()
    object PreviousDay: DailyDatasEvent()
    object GenerateNewDailyData: DailyDatasEvent()
    object SwitchCurrentDailyDataToLatest: DailyDatasEvent()
}


data class DailyDatasState(
    val isLoading: Boolean = false,
    val dailyDataList: List<DailyData> = listOf(),
    val currentDailyData: DailyData = DailyData(),
    val isNextDayAvailable: Boolean = false,
    val isPreviousDayAvailable: Boolean = false,
    val lifeStyleTitle: String = "",
)
