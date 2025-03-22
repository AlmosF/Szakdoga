package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService

class FetchPendingRequestsUseCase(private val repository: IMainService){
    suspend operator fun invoke() =
        repository.fetchPendingRequests()
}