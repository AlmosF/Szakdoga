package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService
import com.example.szakdogafitness.data.trainees.ITraineesService

/*
class FetchRequestsUseCase(private val repository: IMainService){
    suspend operator fun invoke(status: String) =
        repository.fetchRequests(status)
}*/
class FetchRequestsUseCase(private val repository: ITraineesService){
    suspend operator fun invoke(status: String) =
        repository.fetchRequests(status)
}