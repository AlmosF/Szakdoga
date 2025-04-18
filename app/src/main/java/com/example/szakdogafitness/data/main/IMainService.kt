package com.example.szakdogafitness.data.main

import android.net.Uri
import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.model.Rating
import com.example.szakdogafitness.domain.model.Requests
import com.example.szakdogafitness.domain.model.User
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek

@Deprecated("This interface is deprecated and will be removed soon")
interface IMainService{
    ////###PROFILE###
    //suspend fun getUser() : Flow<User?>
    //suspend fun updateUser(user: User)
    ////PFP
    //suspend fun uploadProfilePicture(imageUri: Uri): String
    ////###PROFILE###

    ////###TRAINERS###
    //suspend fun getTrainers(): Flow<List<User?>>
    //suspend fun applyForAppointment(trainer: String?, appointment: Map<String, List<Int>>)
    //suspend fun fetchTrainersRequests(trainerId: String): Flow<Map<String, List<Int>>>
    //suspend fun fetchRequestedAppoint(trainerId: String): Flow<Pair<Requests, User>>
    //suspend fun getAcceptedTrainers(): Flow<List<Pair<Requests, User>>>
    //suspend fun rateTrainer(trainerId: String, rate: Int)
    //suspend fun loadTrainersRate(trainerId: String): Flow<Rating>
    //suspend fun loadAllTrainersRates(): Flow<List<Pair<String, List<Rating>>>>
    ////###TRAINERS###

    ////###TRAINEES###
    //suspend fun getUsersDailyDatasById(userId: String): Flow<List<DailyData>>
    //suspend fun deleteRequest(traineeId: String)
    //suspend fun acceptRequest(traineeId: String)
    //suspend fun removeTrainee(traineeId: String)
    //fun addEventToCalendar(event: Map<String, List<Int>>, title: String, description: String)
    //fun removeEventFromCalendar(event: Map<String, List<Int>>, title: String, description: String)
    //suspend fun fetchRequests(status: String): Flow<List<Pair<Requests, User>>>
    ////###TRAINEES###

    ////###DAILYDATA###
    //suspend fun getUsersDailyDatas(): Flow<List<DailyData>> //fixme("Workingone20250326")
    //suspend fun modifyTargetKcal(targetKcal: Int, date: Timestamp = Timestamp.now())
    //suspend fun modifyTargetWater(targetWater: Double, date: Timestamp = Timestamp.now())
    //suspend fun updateWaterIntake(waterIntake: Double, date: Timestamp = Timestamp.now())
    //suspend fun generateNewDailyData()
    ////###DAILYDATA###



    //suspend fun getUsersDailyDatas(): Flow<DailyDatas?> //fixme("Workingone")
    suspend fun fetchRequestedAppointments(trainerId: String): Flow<Map<String, List<Int>>>
    //suspend fun fetchPendingRequests(status: String): Flow<List<Requests>>
    //suspend fun generateNewDailyData(dailyDatas: DailyDatas) //fixme("Workingone")

}