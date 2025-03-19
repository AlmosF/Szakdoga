package com.example.szakdogafitness.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szakdogafitness.domain.model.Trainer
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.usecases.main.GetTrainersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainersViewModel @Inject constructor(
    private val getTrainersUseCase: GetTrainersUseCase
) : ViewModel() {


    private val _state = MutableStateFlow(TrainersState())
    val state = _state.asStateFlow()

    fun onEvent(event: TrainersEvent){
        when(event){
            TrainersEvent.LoadTrainers -> {
                loadTrainers()
            }
            is TrainersEvent.TrainerSelected -> {
                _state.update { it.copy(selectedTrainer = event.trainer) }
            }
        }
    }

    private fun loadTrainers(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                getTrainersUseCase.invoke().collect { trainers ->
                    _state.update { it.copy(
                        trainers = trainers,
                        isLoading = false
                    ) }
                }
            } catch (e: Exception){
                _state.update { it.copy(isLoading = false) }
            }
        }
    }


}

data class TrainersState(
    val isLoading: Boolean = false,
    val trainers: List<User?> = listOf(),
    val selectedTrainer: User? = null
)

sealed class TrainersEvent {
    object LoadTrainers : TrainersEvent()
    data class TrainerSelected(val trainer: User?) : TrainersEvent()
}