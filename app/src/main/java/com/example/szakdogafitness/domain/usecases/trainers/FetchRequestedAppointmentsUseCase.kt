package com.example.szakdogafitness.domain.usecases.trainers

import com.example.szakdogafitness.data.main.IMainService
import com.example.szakdogafitness.data.trainers.ITrainersService

class FetchRequestedAppointmentsUseCase(private val repository: IMainService){
    suspend fun invoke(trainerId: String) =
        repository.fetchRequestedAppointments(trainerId)
}

/*
class FetchRequestedAppoint(private val repository: IMainService){
    suspend fun invoke(trainerId: String) =
        repository.fetchRequestedAppoint(trainerId)
}*/
class FetchRequestedAppoint(private val repository: ITrainersService){
    suspend fun invoke(trainerId: String) =
        repository.fetchRequestedAppoint(trainerId)
}