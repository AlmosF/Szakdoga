package com.example.szakdogafitness.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.model.DailyDatas
import com.example.szakdogafitness.domain.usecases.main.AllMainUseCases
import com.example.szakdogafitness.domain.usecases.main.FetchUsersDailyDatasUseCase
import com.example.szakdogafitness.domain.usecases.main.GenerateNewDailyDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyDatasViewModel @Inject constructor(
    private val fetchUsersDailyDatas: FetchUsersDailyDatasUseCase,
    private val generateNewDailyData: GenerateNewDailyDataUseCase
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
            DailyDatasEvent.PreviousDay -> {
                val previousDailyData = state.value.dailyDataList[state.value.dailyDataList.indexOf(state.value.currentDailyData) - 1]
                _state.update { it.copy(
                    lifeStyleTitle = previousDailyData.date.toDate().toString().substring(4, 10),
                    currentDailyData = previousDailyData,
                ) }
                buttonsAvailability()
            }
        }
    }

    private fun loadDailyDatas(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, currentDailyData = DailyData()) }
            try {
                fetchUsersDailyDatas.invoke().collect { dailyDataList ->
                    if (dailyDataList.isNotEmpty()){
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
                        buttonsAvailability()
                        generateNewDailyData() //??
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
                _state.update { it.copy(isLoading = false) }
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
    object LoadDailyDatas: DailyDatasEvent()
    object NextDay: DailyDatasEvent()
    object PreviousDay: DailyDatasEvent()
    object GenerateNewDailyData: DailyDatasEvent()
}


data class DailyDatasState(
    val isLoading: Boolean = false,
    val dailyDataList: List<DailyData> = listOf(),
    val currentDailyData: DailyData = DailyData(),
    val isNextDayAvailable: Boolean = false,
    val isPreviousDayAvailable: Boolean = false,
    val lifeStyleTitle: String = "",
)
