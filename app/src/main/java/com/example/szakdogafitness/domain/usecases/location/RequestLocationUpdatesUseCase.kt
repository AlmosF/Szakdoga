package com.example.szakdogafitness.domain.usecases.location

import com.example.szakdogafitness.data.location.ILocationService

class RequestLocationUpdatesUseCase(private val repository: ILocationService){
    suspend operator fun invoke() = repository.requestLocationUpdates()
}