package com.example.szakdogafitness.domain.usecases.trainees

import com.example.szakdogafitness.data.trainees.ITraineesService

class FetchRequestsUseCase(private val repository: ITraineesService){
    suspend operator fun invoke(status: String) =
        repository.fetchRequests(status)
}