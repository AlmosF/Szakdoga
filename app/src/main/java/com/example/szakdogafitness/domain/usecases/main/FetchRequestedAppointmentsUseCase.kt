package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService

class FetchRequestedAppointmentsUseCase(private val repository: IMainService){
    suspend fun invoke(trainerId: String) =
        repository.fetchRequestedAppointments(trainerId)
}