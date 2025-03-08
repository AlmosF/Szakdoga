package com.example.szakdogafitness.viewmodel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.model.DailyDatas
import com.example.szakdogafitness.domain.model.Trainee
import com.example.szakdogafitness.domain.model.Trainer
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.usecases.main.AllMainUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val allMainUseCases: AllMainUseCases
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

            is MainEvent.AddNewDailyData -> {
                addNewDailyData(event.newDay)
            }

            MainEvent.FetchTrainees -> {
                fetchTrainees()
            }
            MainEvent.FetchTrainers -> {
                fetchTrainers()
            }
        }
    }


    private fun fetchDailyData(){
        Log.d("MainViewModel", "fetchDailyData")
        viewModelScope.launch(Dispatchers.IO){
            allMainUseCases.fetchDailyData().collect{
                val dailyDatas = it
                Log.d("MainViewModel", "fetchDailyData collect")
                Log.d("MainViewModel", "${dailyDatas}")
                _state.value = _state.value.copy(dailyData = dailyDatas)
            }

        }
        Log.d("MainViewModel", "fetchDailyData end")
        Log.d("MainViewModel", "${_state.value.dailyData}")
    }

    private fun addNewDailyData(newDay: DailyData){
        viewModelScope.launch(Dispatchers.IO){
            val current = _state.value.dailyData
            allMainUseCases.addNewDailyData(current, newDay)
        }
    }

    private fun fetchTrainers(){
        Log.d("MainNavigation", "FetchTrainers")
        viewModelScope.launch(Dispatchers.IO) {
            allMainUseCases.fetchTrainersUseCase().collect { trainers ->
                val trainers = trainers
                Log.d("MainNavigation..", "Trainers: ${trainers}")
                //_state.update { it.copy(trainers = trainers) }
                _state.value = _state.value.copy(trainers = trainers)
            }
        }
    }

    private fun fetchTrainees(){
        viewModelScope.launch(Dispatchers.IO) {
            allMainUseCases.fetchTraineesUseCase().collect { trainees ->
                val trainees = trainees
                _state.update { it.copy(trainees = trainees) }
            }
        }
    }




}



data class MainState(
    val titleComposable: @Composable () -> Unit = {},
    val dailyData: DailyDatas = DailyDatas(),
    val trainers: List<User> = emptyList(),
    val trainees: List<User> = emptyList()
)

sealed class MainEvent{
    data class ScreenNavigation(val route: String): MainEvent()
    data class TitleComposableChanged(val titleComposable: @Composable () -> Unit): MainEvent()
    object FetchDailyData: MainEvent()
    object FetchTrainers: MainEvent()
    object FetchTrainees: MainEvent()
    data class AddNewDailyData(val newDay: DailyData): MainEvent()
}

