package com.example.szakdogafitness.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.example.szakdogafitness.domain.model.DailyData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

) : ViewModel(){

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    fun onEvent(event: MainEvent){
        when(event){
            MainEvent.FetchDailyData -> {
                fetchDailyData()
            }
            is MainEvent.ScreenNavigation -> {

            }
            is MainEvent.TitleComposableChanged -> {
                _state.value = _state.value.copy(titleComposable = event.titleComposable)
            }
        }
    }


    private fun fetchDailyData(){

    }




}



data class MainState(
    val titleComposable: @Composable () -> Unit = {},
    val dailyData: DailyData = DailyData()
)

sealed class MainEvent{
    data class ScreenNavigation(val route: String): MainEvent()
    data class TitleComposableChanged(val titleComposable: @Composable () -> Unit): MainEvent()
    object FetchDailyData: MainEvent()
}

