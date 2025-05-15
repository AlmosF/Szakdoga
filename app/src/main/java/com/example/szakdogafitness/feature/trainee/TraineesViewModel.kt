package com.example.szakdogafitness.feature.trainee

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.model.Requests
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.usecases.trainees.AllTraineesUseCases
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

/**
 * ViewModel for the the Trainer users,
 * where they can see the requests from the trainees
 */
@HiltViewModel
class TraineesViewModel @Inject constructor(
    private val allTraineesUseCases: AllTraineesUseCases
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

            is TraineesEvent.SelectTrainee -> {
                selectTrainee(event.userId)
            }

            TraineesEvent.ClearSelectedTrainee -> {
                _state.update { it.copy(
                    selectedTrainee = null
                ) }
            }

            is TraineesEvent.FetchTraineesDailyData -> {
                fetchTraineeDailyData(event.userId)
            }

            is TraineesEvent.AppointmentsToCalendar -> {
                _state.update { it.copy(
                    appointmentsToCalendar = event.status
                ) }
            }

            TraineesEvent.NextDailyDataDay -> {
                if (state.value.isNextDayAvailable){
                    val nextDailyData = state.value.traineesDailyDataList[state.value.traineesDailyDataList.indexOf(state.value.selectedTraineesDailyData) + 1]
                    _state.update {
                        it.copy(
                            selectedTraineesDailyData = nextDailyData,
                            lifeStyleTitle = nextDailyData.date.toDate().toString().substring(4, 10)
                        )
                    }
                    buttonsAvailable()
                }
            }
            TraineesEvent.PreviousDailyDataDay -> {
                if (state.value.isPreviousDayAvailable){
                    val previousDailyData = state.value.traineesDailyDataList[state.value.traineesDailyDataList.indexOf(state.value.selectedTraineesDailyData) - 1]
                    _state.update {
                        it.copy(
                            selectedTraineesDailyData = previousDailyData,
                            lifeStyleTitle = previousDailyData.date.toDate().toString().substring(4, 10)
                        )
                    }
                    buttonsAvailable()
                }
            }

            is TraineesEvent.RemoveTrainee -> {
                removeTrainee(event.userId)
            }
        }
    }

    private fun removeTrainee(traineeId: String){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                allTraineesUseCases.removeTrainee.invoke(traineeId)
                allTraineesUseCases.removeEventFromCalendar.invoke(
                    state.value.selectedTrainee?.first?.appointment ?: emptyMap(),
                    "Appointment",
                    "Appointment with ${state.value.selectedTrainee?.second?.firstName} ${state.value.selectedTrainee?.second?.lastName}"
                )
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                Log.e(
                    "TraineesViewModel",
                    "removeTrainee: ${e.message}"
                )
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun buttonsAvailable(){
        val dailyDatas = state.value.traineesDailyDataList
        val currentDailyData = state.value.selectedTraineesDailyData
        val isNextDayAvailable = dailyDatas.indexOf(currentDailyData) < dailyDatas.size - 1
        val isPreviousDayAvailable = dailyDatas.indexOf(currentDailyData) > 0
        _state.update {
            it.copy(
                isNextDayAvailable = isNextDayAvailable,
                isPreviousDayAvailable = isPreviousDayAvailable
            )
        }
    }

    private fun fetchTraineeDailyData(traineeId: String){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try{
                allTraineesUseCases.fetchUsersDailyDatasById.invoke(traineeId).collect { traineeDailyDataList ->
                    if (traineeDailyDataList.isNotEmpty()){
                        if (state.value.traineesDailyDataList.isNotEmpty()) { //Just Update
                            //Log.d("TraineesViewModel", "Just update")
                            val currentDailyData = traineeDailyDataList.find { it.date.toDate() == state.value.selectedTraineesDailyData?.date?.toDate() } ?: traineeDailyDataList.last()

                            val latestDate = currentDailyData.date.toDate().toString().substring(4, 10)
                            _state.update {
                                it.copy(
                                    selectedTraineesDailyData = currentDailyData,
                                    traineesDailyDataList = traineeDailyDataList,
                                    lifeStyleTitle = latestDate,
                                    isLoading = false
                                )
                            }
                        }else { //First Load
                            //Log.d("TraineesViewModel", "First Load")
                            val currentDailyData = traineeDailyDataList.last()
                            val latestDate = currentDailyData.date.toDate().toString().substring(4, 10)
                            _state.update {
                                it.copy(
                                    selectedTraineesDailyData = currentDailyData,
                                    traineesDailyDataList = traineeDailyDataList,
                                    lifeStyleTitle = latestDate,
                                    isLoading = false
                                )
                            }
                            _state.update {
                                it.copy(
                                    selectedTraineesDailyData = currentDailyData,
                                    traineesDailyDataList = traineeDailyDataList,
                                    isLoading = false
                                )
                            }

                        }
                        buttonsAvailable()
                    }
                }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun selectTrainee(traineeId: String){
        _state.update { it.copy(isLoading = true) }
        try {
            val trainee = state.value.acceptedRequests.find {
                it.second.userId == traineeId
            } ?: state.value.pendingRequests.find { //fixme: Might be not safe
                it.second.userId == traineeId
            } ?: error("Error finding trainee")
            _state.update { it.copy(
                selectedTrainee = trainee,
                isLoading = false
            ) }

        } catch (e: Exception) {
            _state.update { it.copy(isLoading = false) }
            Log.e(
                "TraineesViewModel",
                "selectTrainee: ${e.message}"
            )
        }
    }

    private fun loadPendingRequests(
        status: String = "pending"
    ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                allTraineesUseCases.fetchRequests.invoke(status).collect { pendingRequests ->
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
                allTraineesUseCases.acceptRequest.invoke(traineeId)
                if (state.value.appointmentsToCalendar){
                    allTraineesUseCases.addEventToCalendar.invoke(
                        state.value.selectedTrainee?.first?.appointment ?: emptyMap(),
                        "Appointment",
                        "Appointment with ${state.value.selectedTrainee?.second?.firstName} ${state.value.selectedTrainee?.second?.lastName}"
                    )
                }
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
                allTraineesUseCases.deleteRequest.invoke(traineeId)
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
                allTraineesUseCases.fetchRequests.invoke(status).collect { acceptedRequests ->
                    _state.update {
                        it.copy(
                            acceptedRequests = acceptedRequests,
                            isLoading = false,
                        )
                    }
                    state.value.selectedTrainee?.let {
                        selectTrainee(it.second.userId)
                    }
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
    val pendingRequests: List<Pair<Requests, User>> = emptyList(),
    val acceptedRequests: List<Pair<Requests, User>> = emptyList(),
    val detailMode: Boolean = false,
    val selectedTrainee: Pair<Requests, User>? = null,
    val traineesDailyDataList: List<DailyData> = emptyList(),
    val selectedTraineesDailyData: DailyData? = null,
    val appointmentsToCalendar: Boolean = false,
    val isNextDayAvailable: Boolean = false,
    val isPreviousDayAvailable: Boolean = false,
    val lifeStyleTitle : String = "",
)

sealed class TraineesEvent {
    object LoadPendingRequests: TraineesEvent()
    object ClearSelectedTrainee: TraineesEvent()
    object LoadAcceptedRequests: TraineesEvent()
    object PreviousDailyDataDay: TraineesEvent()
    object NextDailyDataDay: TraineesEvent()
    data class DetailModeChanged(val lifestyle: Boolean): TraineesEvent()
    data class FetchTraineesDailyData(val userId: String): TraineesEvent()
    data class SelectTrainee(val userId: String) : TraineesEvent()
    data class AcceptRequest(val userId: String): TraineesEvent()
    data class DeclineRequest(val userId: String): TraineesEvent()
    data class RemoveTrainee(val userId: String): TraineesEvent()
    data class AppointmentsToCalendar(val status: Boolean): TraineesEvent()
}

fun calculateAge(birthTimestamp: Timestamp): Int {
    val now = Timestamp.now()
    val birthDate = birthTimestamp

    val birthLocalDateTime = birthDate.toLocalDateTime()
    val nowLocalDateTime = now.toLocalDateTime()

    return nowLocalDateTime.year - birthLocalDateTime.year
}

fun Timestamp.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(seconds, nanoseconds.toLong()), ZoneId.systemDefault())
}