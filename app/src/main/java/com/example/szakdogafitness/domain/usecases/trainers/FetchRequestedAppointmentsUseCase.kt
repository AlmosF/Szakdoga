package com.example.szakdogafitness.domain.usecases.trainers

import com.example.szakdogafitness.data.trainers.ITrainersService

/*
class FetchRequestedAppoint(private val repository: IMainService){
    suspend fun invoke(trainerId: String) =
        repository.fetchRequestedAppoint(trainerId)
}*/
class FetchRequestedAppoint(private val repository: ITrainersService){
    suspend fun invoke(trainerId: String) =
        repository.fetchRequestedAppoint(trainerId)
}