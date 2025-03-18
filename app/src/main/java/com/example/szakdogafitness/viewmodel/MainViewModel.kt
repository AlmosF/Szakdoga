package com.example.szakdogafitness.viewmodel

import android.R.attr.data
import android.util.Log
import android.util.Log.e
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.model.DailyDatas
import com.example.szakdogafitness.domain.model.Nutrition
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.usecases.main.AllMainUseCases
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val allMainUseCases: AllMainUseCases
) : ViewModel(){

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()


    init {
        //initLifestyleTitle()
        //buttonsAreAvailable() //fixme("Workingone")
        //if (_state.value.dailyDataList.isNotEmpty()){
            buttonsAreAvailable()
       // }
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
                    //val nextDailyData = state.value.dailyDatas.dailyData[state.value.dailyDatas.dailyData.indexOf(state.value.currentDailyData) + 1] //fixme("Workingone")
                    val nextDailyData = state.value.dailyDataList[state.value.dailyDataList.indexOf(state.value.currentDailyData) + 1]
                    _state.value = _state.value.copy(lifeStyleTitle = nextDailyData.date.toDate().toString().substring(4, 10))
                    _state.value = _state.value.copy(currentDailyData = nextDailyData)

                }
            }
            MainEvent.PreviousDayDailyData -> {
                if (state.value.isPreviousDayAvailable){
                    //val previousDailyData = state.value.dailyDatas.dailyData[state.value.dailyDatas.dailyData.indexOf(state.value.currentDailyData) - 1] //fixme("Workingone")
                    val previousDailyData = state.value.dailyDataList[state.value.dailyDataList.indexOf(state.value.currentDailyData) - 1]
                    _state.value = _state.value.copy(lifeStyleTitle = previousDailyData.date.toDate().toString().substring(4, 10))
                    _state.value = _state.value.copy(currentDailyData = previousDailyData)

                }
            }

            MainEvent.FetchUser -> {
                fetchUser()
            }

            MainEvent.UpdateLifestyleTitle -> {
                //initLifestyleTitle()
            }

            MainEvent.GenerateNewDailyData -> {
                generateNewDailyData()
            }

            MainEvent.ClearState -> {
                Log.d("MainViewModel", "ClearState: ${state.value}")
                //_state.value = MainState(dailyDatas = DailyDatas()) //fixme("Workingone")
                _state.value = MainState(
                    dailyDataList = listOf(),
                    currentDailyData = DailyData()
                )
                Log.d("MainViewModel", "ClearStateEND: ${state.value}")
            }

            MainEvent.FetchTrainers -> {
                fetchTrainers()
            }

            MainEvent.FetchMealTypes -> {
                fetchMealTypes()
            }
            MainEvent.FetchTrainees -> {
                fetchTrainees()
            }

            is MainEvent.AddNutrient -> {
                addNutrient(event.nutritionID)
            }
        }
        //initLifestyleTitle()
        //buttonsAreAvailable() //fixme("Workingone")

        //if (_state.value.dailyDataList.isNotEmpty()) {
        buttonsAreAvailable()
        //}
    }

    private fun addNutrient(nutritionID: Int) {
        TODO("Not yet implemented")
    }

    private fun fetchTrainees(){
        TODO()
    }

    private fun fetchTrainers(){
        viewModelScope.launch {
            //_state.update { it.copy(isLoading = true) }
            try {
                allMainUseCases.getTrainersUseCase().collect {
                    val trainers = it
                    _state.update { it.copy(trainers = trainers,/* isLoading = false*/) }
                }
            } catch (e: Exception){
                Log.e(TAG, "Error getting trainers: ${e.message}", e)
                //_state.update { it.copy(isLoading = false) }
            } finally {

            }
        }
    }

    private fun fetchMealTypes(){

    }

    private fun generateNewDailyData(){
        Log.d("MainViewModel", "generateNewDailyData")
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                allMainUseCases.generateNewDailyDataUseCase.invoke()
                _state.update { it.copy(isLoading = false) }
            } catch (e: Exception){
                _state.update { it.copy(isLoading = false) }
                Log.e(TAG, "Error generating new daily data: ${e.message}", e)
            } finally {

            }
        }
    }

    /*private fun buttonsAreAvailable(){ //fixme("Workingone")
        Log.d(TAG, "${state.value.dailyDatas.dailyData.size}")
        val dailyDatas = state.value.dailyDatas
        val currentDailyData = state.value.currentDailyData
        val isNextDayAvailable = dailyDatas.dailyData.indexOf(currentDailyData) < dailyDatas.dailyData.size - 1
        val isPreviousDayAvailable = dailyDatas.dailyData.indexOf(currentDailyData) > 0
        _state.value = _state.value.copy(isNextDayAvailable = isNextDayAvailable, isPreviousDayAvailable = isPreviousDayAvailable)
    }*/

    private fun buttonsAreAvailable(){
        val dailyDatas = state.value.dailyDataList
        val currentDailyData = state.value.currentDailyData
        val isNextDayAvailable = dailyDatas.indexOf(currentDailyData) < dailyDatas.size - 1
        val isPreviousDayAvailable = dailyDatas.indexOf(currentDailyData) > 0
        _state.value = _state.value.copy(isNextDayAvailable = isNextDayAvailable, isPreviousDayAvailable = isPreviousDayAvailable)
    }

    private fun fetchUser(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                allMainUseCases.fetchUserUseCase().collect{
                    val user = it
                    _state.update { it.copy(user = user, isLoading = false) }
                }
            } catch (e: Exception){
                Log.e(TAG, "Error getting user: ${e.message}", e)
                _state.update { it.copy(isLoading = false) }
            } finally {

            }
        }
    }
    /*private fun fetchUser(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val user = allMainUseCases.fetchUserUseCase().first()
                Log.d("RootNavigation:::", "User fetched: $user")
                _state.update { it.copy(user = user, isLoading = false) }
            } catch (e: Exception){
                _state.update { it.copy(isLoading = false) }
            } finally {
                Log.d("RootNavigation:::", "FetchUser Setting isLoading to false")
                _state.update { it.copy(isLoading = false) }
            }
        }
    }*/

    /*private fun fetchDailyData(){ //fixme("Workingone")
        viewModelScope.launch {
            //_state.update { it.copy(isLoading = true) }
            _state.update { it.copy(isLoading = true, dailyDatas = DailyDatas(), currentDailyData = DailyData()) }
            try {
                allMainUseCases.fetchUsersDailyDatasUseCase().collect { dailyDatas ->
                    if (dailyDatas != null && dailyDatas.dailyData.isNotEmpty()){
                        val latestDailyDate = dailyDatas.dailyData.last()
                        val latestDate = latestDailyDate.date.toDate().toString().substring(4, 10)
                        //buttonsAreAvailable()
                        _state.update { it.copy(
                            dailyDatas = dailyDatas,
                            currentDailyData = latestDailyDate,
                            lifeStyleTitle = latestDate,
                            isLoading = false
                        ) }
                        buttonsAreAvailable()
                        generateNewDailyData()
                    } else {
                        _state.update { it.copy(isLoading = false) }
                        Log.d("RootNavigation:::", "Daily data fetched: ${state.value.isLoading}")
                    }
                }
            } catch (e: Exception){
                Log.e(TAG, "Error getting daily data: ${e.message}", e)
                _state.update { it.copy(isLoading = false) }
            } finally {

            }
        }
    }*/

    private fun fetchDailyData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, currentDailyData = DailyData()) }
            try {
                allMainUseCases.fetchUsersDailyDatasUseCase().collect { dailyDataList ->
                    if (dailyDataList.isNotEmpty()){
                        val latestDailyDate = dailyDataList.last()
                        val latestDate = latestDailyDate.date.toDate().toString().substring(4, 10)
                        _state.update { it.copy(
                            dailyDataList = dailyDataList,
                            currentDailyData = latestDailyDate,
                            lifeStyleTitle = latestDate,
                            isLoading = false
                        ) }
                        buttonsAreAvailable()
                        generateNewDailyData()
                    }

                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting daily data: ${e.message}", e)
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    /*private fun fetchDailyData(){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val dailyDatas = allMainUseCases.fetchUsersDailyDatasUseCase().first() // ⬅️ Use first() instead of collect
                if (dailyDatas != null && dailyDatas.dailyData.isNotEmpty()) {
                    val latestDailyDate = dailyDatas.dailyData.last()
                    val latestDate = latestDailyDate.date.toDate().toString().substring(4, 10)
                    buttonsAreAvailable()
                    _state.update { it.copy(
                        dailyDatas = dailyDatas,
                        currentDailyData = latestDailyDate,
                        lifeStyleTitle = latestDate
                    ) }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting daily data: ${e.message}", e)
            } finally {
                Log.d("RootNavigation:::", "FetchDailyData Setting isLoading to false")
                _state.update { it.copy(isLoading = false) }
            }
        }
    }*/

    fun checkIfUserIsNew() : Boolean{
        return state.value.user == null
    }


}
fun getYesterdayFirebaseTimestamp(): Timestamp {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -1) // Subtract 1 day
    return Timestamp(calendar.time)
}
fun getDecember11FirebaseTimestamp(): Timestamp {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.MONTH, Calendar.DECEMBER)
    calendar.set(Calendar.DAY_OF_MONTH, 11)
    return Timestamp(calendar.time)
}
fun getTomorrowFirebaseTimestamp(): Timestamp {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 1) // Add 1 day
    return Timestamp(calendar.time)
}

/*val testMainStateDailyDatas = DailyDatas(
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
)*/



data class MainState(
    val isLoading: Boolean = false,
    //val loadingCount: Int = 0,
    val titleComposable: @Composable () -> Unit = {},
    //val user: User? = User(),
    val user: User? = null,
    //val dailyDatas: DailyDatas = testMainStateDailyDatas,
    val trainers: List<User?> = listOf(),
    val trainees: List<User?> = listOf(),
    val mealTypes: Map<String, List<Nutrition>> = mapOf(),

    //val dailyDatas: DailyDatas = DailyDatas(), //fixme("Workingone")
    //val currentDailyData: DailyData = dailyDatas.dailyData.lastOrNull() ?: DailyData(), //fixme("Workingone")

    val dailyDataList: List<DailyData> = listOf(),
    val currentDailyData: DailyData = DailyData(),

    val isNextDayAvailable: Boolean = false,
    val isPreviousDayAvailable: Boolean = false,
    val lifeStyleTitle: String = "",
)

sealed class MainEvent{
    data class ScreenNavigation(val route: String): MainEvent()
    data class TitleComposableChanged(val titleComposable: @Composable () -> Unit): MainEvent()
    data class AddNutrient(val nutritionID: Int) : MainEvent()
    object FetchDailyData: MainEvent()
    object FetchUser: MainEvent()
    object UpdateLifestyleTitle: MainEvent()
    object PreviousDayDailyData: MainEvent()
    object FetchTrainers: MainEvent()
    object FetchTrainees: MainEvent()
    object FetchMealTypes: MainEvent()
    object NextDayDailyData: MainEvent()
    object GenerateNewDailyData: MainEvent()
    object ClearState: MainEvent()
}

fun Timestamp.toYYYYMMDD(): Triple<Int, Int, Int> {
    val date = this.toDate()
    val calendar = Calendar.getInstance()
    calendar.time = date
    return Triple(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1,
        calendar.get(Calendar.DAY_OF_MONTH)
    )
}

