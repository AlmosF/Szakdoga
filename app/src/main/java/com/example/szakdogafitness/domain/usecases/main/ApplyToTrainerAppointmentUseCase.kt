package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService

class ApplyToTrainerAppointmentUseCase(private val repository: IMainService){
    suspend operator fun invoke(
        trainer: String?,
        appointment: Map<String, List<Int>>
    ) = repository.applyForAppointment(trainer, appointment)
}