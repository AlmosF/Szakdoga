package com.example.szakdogafitness.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szakdogafitness.domain.model.Requests
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.usecases.main.AcceptRequestUseCase
import com.example.szakdogafitness.domain.usecases.main.DeleteRequestUseCase
import com.example.szakdogafitness.domain.usecases.main.FetchRequestsUseCase
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * ViewModel for the the Trainer users,
 * where they can see the requests from the trainees
 */
@HiltViewModel
class TraineesViewModel @Inject constructor(
    private val fetchRequests: FetchRequestsUseCase,
    private val declineRequest: DeleteRequestUseCase,
    private val acceptRequest: AcceptRequestUseCase
) : ViewModel() {


    private val _state = MutableStateFlow(TraineesState())
    val state = _state.asStateFlow()

    fun onEvent(event: TraineesEvent){
        when(event){
            TraineesEvent.LoadPendingRequests -> {
                loadPendingRequests()
            }
            TraineesEvent.LoadAcceptedRequests -> {
                loadAcceptedRequests()
            }
            is TraineesEvent.AcceptRequest -> {
                acceptRequest(event.userId)
            }
            is TraineesEvent.DeclineRequest -> {
                declineRequest(event.userId)
            }

            is TraineesEvent.DetailModeChanged -> {
                _state.update { it.copy(detailMode = event.lifestyle) }
            }
        }
    }

    private fun loadPendingRequests(
        status: String = "pending"
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                fetchRequests.invoke(status).collect { pendingRequests ->
                    _state.update { it.copy(
                        pendingRequests = pendingRequests,
                        isLoading = false
                    ) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun acceptRequest(
        traineeId: String
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                acceptRequest.invoke(traineeId)
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                Log.e(
                    "TraineesViewModel",
                    "acceptRequest: ${e.message}"
                )
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun declineRequest(
        traineeId: String
    ) {
        viewModelScope.launch {
            try {
                declineRequest.invoke(traineeId)
            } catch (e: Exception) {
                Log.e(
                    "TraineesViewModel",
                    "declineRequest: ${e.message}"
                )
            }
        }
    }


    private fun loadAcceptedRequests(
        status: String = "accepted"
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                fetchRequests.invoke(status).collect { acceptedRequests ->
                    _state.update { it.copy(
                        acceptedRequests = acceptedRequests,
                        isLoading = false
                    ) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

}

/**
 * State for the TraineesViewModel
 * @param detailMode: Boolean - If in detail mode: False -> Lifestyle; True -> Bio
 */
data class TraineesState(
    val isLoading: Boolean = false,
    //val pendingRequests: List<Requests> = emptyList(),
    val pendingRequests: List<Pair<Requests, User>> = emptyList(),
    //val acceptedRequests: List<Requests> = emptyList()
    val acceptedRequests: List<Pair<Requests, User>> = emptyList(),
    val detailMode: Boolean = false
)

sealed class TraineesEvent {
    object LoadPendingRequests: TraineesEvent()
    object LoadAcceptedRequests: TraineesEvent()
    data class DetailModeChanged(val lifestyle: Boolean): TraineesEvent()
    data class AcceptRequest(val userId: String): TraineesEvent()
    data class DeclineRequest(val userId: String): TraineesEvent()
}

fun calculateAge(birthTimestamp: Timestamp): Int {
    val now = Timestamp.now()
    val birthDate = birthTimestamp

    val birthLocalDateTime = birthDate.toLocalDateTime()
    val nowLocalDateTime = now.toLocalDateTime()

    return nowLocalDateTime.year - birthLocalDateTime.year
}

fun Timestamp.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(seconds, nanoseconds.toLong()), java.time.ZoneId.systemDefault())
}