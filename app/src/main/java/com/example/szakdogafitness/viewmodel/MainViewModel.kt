package com.example.szakdogafitness.viewmodel

import android.R.attr.data
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.model.DailyDatas
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

) : ViewModel(){

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()


    init {
        _state.value = _state.value.copy(lifeStyleTitle = state.value.currentDailyData.date.toDate().toString().substring(4, 10))
        buttonsAreAvailable()
    }

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

            MainEvent.NextDayDailyData -> {
                if (state.value.isNextDayAvailable){
                    val nextDailyData = state.value.dailyDatas.dailyData[state.value.dailyDatas.dailyData.indexOf(state.value.currentDailyData) + 1]
                    _state.value = _state.value.copy(lifeStyleTitle = nextDailyData.date.toDate().toString().substring(4, 10))
                    _state.value = _state.value.copy(currentDailyData = nextDailyData)
                }
            }
            MainEvent.PreviousDayDailyData -> {
                if (state.value.isPreviousDayAvailable){
                    val previousDailyData = state.value.dailyDatas.dailyData[state.value.dailyDatas.dailyData.indexOf(state.value.currentDailyData) - 1]
                    _state.value = _state.value.copy(lifeStyleTitle = previousDailyData.date.toDate().toString().substring(4, 10))
                    _state.value = _state.value.copy(currentDailyData = previousDailyData)
                }
            }
        }
        buttonsAreAvailable()
    }

    private fun buttonsAreAvailable(){
        Log.d("RootNavigationXAXA", "${state.value.dailyDatas.dailyData.size}")
        val dailyDatas = state.value.dailyDatas
        val currentDailyData = state.value.currentDailyData
        val isNextDayAvailable = dailyDatas.dailyData.indexOf(currentDailyData) < dailyDatas.dailyData.size - 1
        val isPreviousDayAvailable = dailyDatas.dailyData.indexOf(currentDailyData) > 0
        _state.value = _state.value.copy(isNextDayAvailable = isNextDayAvailable, isPreviousDayAvailable = isPreviousDayAvailable)
    }


    private fun fetchDailyData(){

    }
}
fun getYesterdayFirebaseTimestamp(): Timestamp {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -1) // Subtract 1 day
    return Timestamp(calendar.time)
}

fun getTomorrowFirebaseTimestamp(): Timestamp {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 1) // Add 1 day
    return Timestamp(calendar.time)
}

val testMainStateDailyDatas = DailyDatas(
    dailyData = listOf(
        DailyData(
            date = getYesterdayFirebaseTimestamp(),
        ),
        DailyData(
            date = Timestamp.now(),
        ),
        DailyData(
            date = getTomorrowFirebaseTimestamp(),
        )
    )
)



data class MainState(
    val isLoading: Boolean = false,
    val titleComposable: @Composable () -> Unit = {},
    val dailyDatas: DailyDatas = testMainStateDailyDatas,
    //val dailyDatas: DailyDatas = DailyDatas(),
    val currentDailyData: DailyData = dailyDatas.dailyData.lastOrNull() ?: DailyData(),
    val isNextDayAvailable: Boolean = false,
    val isPreviousDayAvailable: Boolean = false,
    val lifeStyleTitle: String = "",

)

sealed class MainEvent{
    data class ScreenNavigation(val route: String): MainEvent()
    data class TitleComposableChanged(val titleComposable: @Composable () -> Unit): MainEvent()
    object FetchDailyData: MainEvent()
    object PreviousDayDailyData: MainEvent()
    object NextDayDailyData: MainEvent()
}

