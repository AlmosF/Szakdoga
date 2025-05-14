package com.example.szakdogafitness.data.trainees

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import android.util.Log
import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.model.FirestoreTrainee
import com.example.szakdogafitness.domain.model.FirestoreTrainer
import com.example.szakdogafitness.domain.model.FirestoreUser
import com.example.szakdogafitness.domain.model.Requests
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.domain.model.toUser
import com.example.szakdogafitness.domain.usecases.auth.CurrentUserIdUseCase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import java.time.DayOfWeek
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject

class TraineesService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userIdUseCase: CurrentUserIdUseCase,
    @ApplicationContext private val context: Context,
) : ITraineesService {
    override suspend fun getUsersDailyDatasById(userId: String): Flow<List<DailyData>> = flow {
        try {
            firestore.collection("dailyDatas")
                .document(userId)
                .collection("dailyData")
                .dataObjects<DailyData>()
                .collect { dailyDataList ->
                    emit(dailyDataList.sortedBy { it.date.toDate() })
                }
        } catch (e: Exception) {
            Log.e("MainService", "Error fetching daily data by ID: ${e.message}", e)
            emit(emptyList())
        }
    }

    override suspend fun deleteRequest(traineeId: String) {
        val userId = userIdUseCase()
        if (userId == null) {
            Log.e("MainService", "Error deleting request: User ID is null")
            return
        }
        try {
            firestore.collection("requests")
                .document(userId)
                .collection("traineeRequests")
                .document(traineeId)
                .delete()
                .await()



        } catch (e: Exception) {
            Log.e("MainService", "Error deleting request: ${e.message}", e)
        }
    }

    override suspend fun acceptRequest(traineeId: String) {
        val userId = userIdUseCase()
        if (userId == null) {
            Log.e("MainService", "Error accepting request: User ID is null")
            return
        }
        try {
            val requestRef = firestore.collection("requests")
                .document(userId)
                .collection("traineeRequests")
                .document(traineeId)

            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(requestRef)

                val request = snapshot.toObject(Requests::class.java)

                if (request != null) {
                    transaction.update(requestRef, "status", "accepted")
                }
            }.await()
            val appointment = requestRef
                .get()
                .await()
                .toObject(Requests::class.java)
                ?.appointment
            // Create a new document for trainee->trainer
            val traineeRef = firestore.collection("requests")
                .document(traineeId)
                .collection("trainers")
                .document(userId)

            val request = mapOf(
                "appointment" to appointment,
                "status" to "accepted",
                "createdAt" to Timestamp.now(),
                "trainerId" to userId
            )

            traineeRef.set(request).await()


        } catch (e: Exception) {
            Log.e("MainService", "Error accepting request: ${e.message}", e)
        }
    }

    override suspend fun removeTrainee(traineeId: String) {
        val userId = userIdUseCase()
        if (userId == null) {
            Log.e("MainService", "Error accepting request: User ID is null")
            return
        }
        try {
            firestore.collection("requests")
                .document(userId)
                .collection("traineeRequests")
                .document(traineeId)
                .delete()
                .await()

            firestore.collection("requests")
                .document(traineeId)
                .collection("trainers")
                .document(userId)
                .delete()
                .await()

        } catch (e: Exception) {
            Log.e("MainService", "Error removing trainee: ${e.message}", e)
        }
    }

    override fun addEventToCalendar(event: Map<String, List<Int>>, title: String, description: String) {
        val contentResolver = context.contentResolver
        val eventUris = mutableListOf<Uri>()

        event.forEach { (day, hours) ->
            val dayOfWeek = stringToDayOfWeek(day) ?: return@forEach

            hours.forEach { hour ->
                val calendar = Calendar.getInstance().apply {
                    add(Calendar.WEEK_OF_YEAR, 1)
                    set(Calendar.DAY_OF_WEEK, dayToCalendar(dayOfWeek))
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }

                val values = ContentValues().apply {
                    put(CalendarContract.Events.CALENDAR_ID, 1)
                    put(CalendarContract.Events.TITLE, title)
                    put(CalendarContract.Events.DESCRIPTION, description)
                    put(CalendarContract.Events.DTSTART, calendar.timeInMillis)
                    put(CalendarContract.Events.DTEND, calendar.timeInMillis + (60 * 60 * 1000))
                    put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
                    put(CalendarContract.Events.RRULE, "FREQ=WEEKLY")
                }

                val uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
                uri?.let { eventUris.add(it) }
            }
        }

        if (eventUris.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = eventUris.first()
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(intent)
        }
    }

    override fun removeEventFromCalendar(
        event: Map<String, List<Int>>,
        title: String,
        description: String
    ) {
        val contentResolver = context.contentResolver

        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.RRULE
        )

        val now = System.currentTimeMillis()

        val selection = ("(${CalendarContract.Events.TITLE} = ?) AND " +
                "(${CalendarContract.Events.DESCRIPTION} = ?) AND " +
                "(${CalendarContract.Events.DTSTART} >= ?)")
        val selectionArgs = arrayOf(title, description, now.toString())

        val cursor = contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val eventId = it.getLong(it.getColumnIndexOrThrow(CalendarContract.Events._ID))
                val eventStart = it.getLong(it.getColumnIndexOrThrow(CalendarContract.Events.DTSTART))
                val rrule = it.getString(it.getColumnIndexOrThrow(CalendarContract.Events.RRULE)) ?: ""

                val calendar = Calendar.getInstance().apply {
                    timeInMillis = eventStart
                }

                val day = calendar.get(Calendar.DAY_OF_WEEK)
                val hour = calendar.get(Calendar.HOUR_OF_DAY)

                val matches = event.any { (dayStr, hours) ->
                    stringToDayOfWeek(dayStr)?.let { dow ->
                        dayToCalendar(dow) == day && hours.contains(hour)
                    } ?: false
                }

                if (matches) {
                    val deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventId)
                    contentResolver.delete(deleteUri, null, null)
                }
            }
        }
    }

    override suspend fun fetchRequests(status: String): Flow<List<Pair<Requests, User>>> =
        firestore.collection("requests")
            .document(userIdUseCase() ?: "")
            .collection("traineeRequests")
            .whereEqualTo("status", status)
            .dataObjects<Requests>()
            .flatMapLatest { requests ->
                if (requests.isEmpty()) return@flatMapLatest flowOf(emptyList())

                val requestFlows = requests.map { request ->
                    getUserById(request.traineeId)
                        .mapNotNull { user ->
                            if (user != null) request to user else null
                        }
                }
                combine(requestFlows) { it.toList() }
            }
            .flowOn(Dispatchers.IO)

    private suspend fun getUserById(userId: String): Flow<User?> =
        firestore.collection("users")
            .document(userId)
            .dataObjects<FirestoreUser>()
            .flatMapLatest { user->
                if(user != null){
                    if (user.role == 1) {
                        firestore.collection("trainers")
                            .document(userId)
                            .dataObjects<FirestoreTrainer>()
                            .map { trainer ->
                                if (trainer != null) {
                                    user.toUser(trainer)
                                } else {
                                    null
                                }
                            }
                    } else {
                        firestore.collection("trainees")
                            .document(userId)
                            .dataObjects<FirestoreTrainee>()
                            .map { trainee ->
                                if (trainee != null){
                                    user.toUser(trainee)
                                } else {
                                    null
                                }
                            }
                    }
                } else {
                    flowOf(null)
                }
            }.flowOn(Dispatchers.IO)

    private fun stringToDayOfWeek(day: String): DayOfWeek? {
        return when (day.lowercase()) {
            "monday" -> DayOfWeek.MONDAY
            "tuesday" -> DayOfWeek.TUESDAY
            "wednesday" -> DayOfWeek.WEDNESDAY
            "thursday" -> DayOfWeek.THURSDAY
            "friday" -> DayOfWeek.FRIDAY
            "saturday" -> DayOfWeek.SATURDAY
            "sunday" -> DayOfWeek.SUNDAY
            else -> null
        }
    }

    private fun dayToCalendar(day: DayOfWeek): Int {
        return when (day) {
            DayOfWeek.MONDAY -> Calendar.MONDAY
            DayOfWeek.TUESDAY -> Calendar.TUESDAY
            DayOfWeek.WEDNESDAY -> Calendar.WEDNESDAY
            DayOfWeek.THURSDAY -> Calendar.THURSDAY
            DayOfWeek.FRIDAY -> Calendar.FRIDAY
            DayOfWeek.SATURDAY -> Calendar.SATURDAY
            DayOfWeek.SUNDAY -> Calendar.SUNDAY
        }
    }

}