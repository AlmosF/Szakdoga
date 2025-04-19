package com.example.szakdogafitness.domain.usecases.trainers

import com.example.szakdogafitness.data.trainees.ITraineesService

class AllTrainersUseCases(
    val repository: ITraineesService,
    val applyToTrainerAppointment: ApplyToTrainerAppointmentUseCase,
    val fetchRequestedAppointments: FetchRequestedAppoint,
    val fetchTrainersRequests: FetchTrainersRequestsUseCase,
    val getAcceptedTrainers: GetAcceptedTrainersUseCase,
    val getTrainers: GetTrainersUseCase,
    val loadTrainersRate: LoadTrainersRateUseCase,
    val loadAllTrainersAllRate: LoadAllTrainersAllRateUseCase,
    val rateTrainer: RateTrainerUseCase,
)