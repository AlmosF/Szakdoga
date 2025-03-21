package com.example.szakdogafitness.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.usecases.main.ApplyToTrainerAppointmentUseCase
import com.example.szakdogafitness.domain.usecases.main.FetchRequestedAppointmentsUseCase
import com.example.szakdogafitness.domain.usecases.main.FetchTrainersRequestsUseCase
import com.example.szakdogafitness.domain.usecases.main.GetTrainersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainersViewModel @Inject constructor(
    private val getTrainersUseCase: GetTrainersUseCase,
    private val applyToTrainerAppointmentUseCase: ApplyToTrainerAppointmentUseCase,
    private val fetchTrainersRequestsUseCase: FetchTrainersRequestsUseCase,
    private val fetchRequestedAppointmentsUseCase: FetchRequestedAppointmentsUseCase
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

            is TrainersEvent.ApplyForAppointment -> {
                applyForAppointment(event.trainer, event.appointment)
            }

            is TrainersEvent.FetchTrainersRequests -> {
                fetchTrainersRequests(event.trainerId)
            }

            is TrainersEvent.FetchRequestedAppointments -> {
                fetchRequestedAppointments(event.trainerId)
            }
        }
    }

    private fun fetchRequestedAppointments(trainerId: String) {
        Log.d("TrainersViewModel", "fetchRequestedAppointments: $trainerId")
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                fetchRequestedAppointmentsUseCase.invoke(trainerId).collect { requestedAppointments ->
                    _state.update { it.copy(
                        requestedAppointments = requestedAppointments,
                        isLoading = false
                    ) }
                    Log.d("TrainersViewModel", "fetchRequestedAppointments: $requestedAppointments")
                }
            } catch (e: Exception){
                _state.update { it.copy(isLoading = false) }
                Log.e("TrainersViewModel", "fetchRequestedAppointments: ${e.message}")
            }
        }
    }

    private fun fetchTrainersRequests(trainerId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                fetchTrainersRequestsUseCase.invoke(trainerId).collect { trainersRequests ->
                    _state.update { it.copy(
                        trainersRequests = trainersRequests,
                        isLoading = false
                    ) }
                }
            } catch (e: Exception){
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun applyForAppointment(
        trainer: String?,
        appointment: Map<String, List<Int>>
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                applyToTrainerAppointmentUseCase.invoke(
                    trainer = trainer,
                    appointment = appointment
                )
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
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

/**
 * @param isLoading: Loading state
 * @param trainers: List of trainers
 * @param selectedTrainer: Selected trainer
 * @param appointment: Map of appointments
 * @param trainersRequests: Map of trainers requests
 * @param requestedAppointments: Map of requested appointments
 */

data class TrainersState(
    val isLoading: Boolean = false,
    val trainers: List<User?> = listOf(),
    val selectedTrainer: User? = null,
    val appointment: Map<String, List<Int>> = mapOf(),
    val trainersRequests: Map<String, List<Int>> = mapOf(),
    val requestedAppointments: Map<String, List<Int>> = mapOf()
)

/**
 * @param LoadTrainers: Load trainers from the database
 * @param FetchTrainersRequests: Fetch the trainers requests, that trainees applied for
 * @param FetchRequestedAppointments: Fetch the requested appointments for the trainer (trainee can see it)
 * @param TrainerSelected: Select a trainer
 * @param ApplyForAppointment: Apply for an appointment
 */

sealed class TrainersEvent {
    object LoadTrainers : TrainersEvent()
    data class FetchTrainersRequests(val trainerId: String): TrainersEvent()
    data class FetchRequestedAppointments(val trainerId: String): TrainersEvent()
    data class TrainerSelected(val trainer: User?) : TrainersEvent()
    data class ApplyForAppointment(
        val trainer: String?,
        val appointment: Map<String, List<Int>>
    ) : TrainersEvent()
}