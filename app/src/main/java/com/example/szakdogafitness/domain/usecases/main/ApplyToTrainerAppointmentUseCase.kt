package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService
import com.example.szakdogafitness.data.trainers.ITrainersService

/*
class ApplyToTrainerAppointmentUseCase(private val repository: IMainService){
    suspend operator fun invoke(
        trainer: String?,
        appointment: Map<String, List<Int>>
    ) = repository.applyForAppointment(trainer, appointment)
}*/
class ApplyToTrainerAppointmentUseCase(private val repository: ITrainersService){
    suspend operator fun invoke(
        trainer: String?,
        appointment: Map<String, List<Int>>
    ) = repository.applyForAppointment(trainer, appointment)
}