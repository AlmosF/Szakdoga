package com.example.szakdogafitness.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.privacysandbox.ads.adservices.adid.AdId
import com.example.szakdogafitness.domain.model.Rating
import com.example.szakdogafitness.domain.model.Requests
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.usecases.main.ApplyToTrainerAppointmentUseCase
import com.example.szakdogafitness.domain.usecases.main.FetchRequestedAppoint
import com.example.szakdogafitness.domain.usecases.main.FetchRequestedAppointmentsUseCase
import com.example.szakdogafitness.domain.usecases.main.FetchTrainersRequestsUseCase
import com.example.szakdogafitness.domain.usecases.main.GetAcceptedTrainersUseCase
import com.example.szakdogafitness.domain.usecases.main.GetTrainersUseCase
import com.example.szakdogafitness.domain.usecases.main.LoadAllTrainersAllRateUseCase
import com.example.szakdogafitness.domain.usecases.main.LoadTrainersRateUseCase
import com.example.szakdogafitness.domain.usecases.main.RateTrainerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the the Trainee users,
 * where they can see the trainers and apply for appointments
 */

@HiltViewModel
class TrainersViewModel @Inject constructor(
    private val getTrainersUseCase: GetTrainersUseCase,
    private val applyToTrainerAppointmentUseCase: ApplyToTrainerAppointmentUseCase,
    private val fetchTrainersRequestsUseCase: FetchTrainersRequestsUseCase,
    private val fetchRequestedAppointmentsUseCase: FetchRequestedAppointmentsUseCase,
    private val getAcceptedTrainersUseCase: GetAcceptedTrainersUseCase,
    private val fetchRequestedAppoint: FetchRequestedAppoint,
    private val rateTrainerUseCase: RateTrainerUseCase,
    private val loadTrainersRateUseCase: LoadTrainersRateUseCase,
    private val loadAllTrainersAllRatingUseCase: LoadAllTrainersAllRateUseCase
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

            TrainersEvent.LoadAcceptedTrainers -> {
                loadAcceptedTrainers()
            }

            is TrainersEvent.FetchRequestedAppoint -> {
                fetchRequestedAppoint(event.trainerId)
            }

            is TrainersEvent.LoadTrainersRate -> {
                loadTrainersRate(event.trainerId)
            }
            is TrainersEvent.RateTrainer -> {
                rateTrainer(event.trainerId, event.rate)
            }

            TrainersEvent.LoadAllTrainersAllRating -> {
                loadAllTrainersAllRating()
            }
        }
    }

    private fun loadAllTrainersAllRating() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                loadAllTrainersAllRatingUseCase.invoke().collect { allTrainersAllRating ->
                    _state.update { it.copy(
                        allTrainersAllRating = allTrainersAllRating,
                        isLoading = false
                    ) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }


    private fun rateTrainer(
        trainerId: String,
        rate: Int
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                rateTrainerUseCase.invoke(trainerId, rate)
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun loadTrainersRate(
        trainerId: String
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                loadTrainersRateUseCase.invoke(trainerId).collect { rate ->
                    _state.update { it.copy(
                        trainersRate = rate,
                        isLoading = false
                    ) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * Able to fetch the request from a specific trainer with a sepcific trainee
     * Able to make difference between pending and accepted requests, or not requested at all
     * Useful to make different UI for the users that are accepted already exmaple: Rating UI
     */
    private fun fetchRequestedAppoint(trainerId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                fetchRequestedAppoint.invoke(trainerId).collect { requestedAppoint ->
                    Log.d("TrainersViewModel", "fetchRequestedAppoint: $requestedAppoint")
                    _state.update { it.copy(
                        requestedAppoint = requestedAppoint,
                        isLoading = false
                    ) }
                }
            } catch (e: Exception){
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun loadAcceptedTrainers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                getAcceptedTrainersUseCase.invoke().collect { accepted->
                    _state.update {
                        it.copy(
                            acceptedTrainers = accepted,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    //FIXME: DEPRECATED - NOT USED
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
 * @param requestedAppoint: Pair of requests and user for trainee, so can decide if pending or accepted - new var
 */

data class TrainersState(
    val isLoading: Boolean = false,
    val trainers: List<User?> = listOf(),
    val selectedTrainer: User? = null,
    val appointment: Map<String, List<Int>> = mapOf(),
    val trainersRequests: Map<String, List<Int>> = mapOf(),
    val requestedAppointments: Map<String, List<Int>> = mapOf(),
    val requestedAppoint: Pair<Requests, User> = Pair(Requests(), User()),
    val acceptedTrainers: List<Pair<Requests, User>> = listOf(),
    val trainersRate: Rating = Rating(),
    val allTrainersAllRating: List<Pair<String, List<Rating>>?> = listOf()
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
    object LoadAcceptedTrainers : TrainersEvent()
    object LoadAllTrainersAllRating : TrainersEvent()
    data class LoadTrainersRate(val trainerId: String) : TrainersEvent()
    data class RateTrainer(val trainerId: String, val rate: Int): TrainersEvent()
    data class FetchTrainersRequests(val trainerId: String): TrainersEvent()
    data class FetchRequestedAppointments(val trainerId: String): TrainersEvent()
    data class FetchRequestedAppoint(val trainerId: String): TrainersEvent()
    data class TrainerSelected(val trainer: User?) : TrainersEvent()
    data class ApplyForAppointment(
        val trainer: String?,
        val appointment: Map<String, List<Int>>
    ) : TrainersEvent()
}