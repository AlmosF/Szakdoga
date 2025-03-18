package com.example.szakdogafitness.viewmodel

import androidx.lifecycle.ViewModel
import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.model.DailyDatas
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DailyDatasViewModel @Inject constructor(

) : ViewModel(){

    private val _state = MutableStateFlow(DailyDatasState())
    val state = _state.asStateFlow()



}

data class DailyDatasState(
    val isLoading: Boolean = false,
    val dailyDataList: List<DailyData> = listOf(),
    val currentDailyData: DailyData = DailyData(),
    val isNextDayAvailable: Boolean = false,
    val isPreviousDayAvailable: Boolean = false,
    val lifeStyleTitle: String = "",
)
