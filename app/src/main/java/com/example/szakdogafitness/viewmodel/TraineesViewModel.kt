package com.example.szakdogafitness.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szakdogafitness.domain.model.Requests
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.usecases.main.FetchPendingRequestsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the the Trainer users,
 * where they can see the requests from the trainees
 */
@HiltViewModel
class TraineesViewModel @Inject constructor(
    private val fetchPendingRequestsUseCase: FetchPendingRequestsUseCase
) : ViewModel() {


    private val _state = MutableStateFlow(TraineesState())
    val state = _state.asStateFlow()

    fun onEvent(event: TraineesEvent){
        when(event){
            TraineesEvent.LoadPendingRequests -> {
                loadPendingRequests()
            }
            is TraineesEvent.AcceptRequest -> TODO()
            is TraineesEvent.DeclineRequest -> TODO()
            TraineesEvent.LoadAcceptedRequests -> TODO()

        }
    }

    private fun loadPendingRequests() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                fetchPendingRequestsUseCase.invoke().collect { pendingRequests ->
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
}

data class TraineesState(
    val isLoading: Boolean = false,
    //val pendingRequests: List<Requests> = emptyList(),
    val pendingRequests: List<Pair<Requests, User>> = emptyList(),
    val acceptedRequests: List<Requests> = emptyList()
)

sealed class TraineesEvent {
    object LoadPendingRequests: TraineesEvent()
    object LoadAcceptedRequests: TraineesEvent()
    data class AcceptRequest(val userId: String): TraineesEvent()
    data class DeclineRequest(val userId: String): TraineesEvent()
}