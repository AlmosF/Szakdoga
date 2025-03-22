package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService

class FetchRequestsUseCase(private val repository: IMainService){
    suspend operator fun invoke(status: String) =
        repository.fetchPendingRequests(status)
}