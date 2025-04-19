package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService
import com.example.szakdogafitness.domain.usecases.dailyDatas.FetchUsersDailyDatasUseCase
import com.example.szakdogafitness.domain.usecases.dailyDatas.GenerateNewDailyDataUseCase
import com.example.szakdogafitness.domain.usecases.profile.FetchUserUseCase
import com.example.szakdogafitness.domain.usecases.profile.UpdateUserUseCase
import com.example.szakdogafitness.domain.usecases.trainers.ApplyToTrainerAppointmentUseCase
import com.example.szakdogafitness.domain.usecases.trainers.FetchRequestedAppointmentsUseCase
import com.example.szakdogafitness.domain.usecases.trainers.FetchTrainersRequestsUseCase
import com.example.szakdogafitness.domain.usecases.trainers.GetTrainersUseCase

class AllMainUseCases(
    val repository: IMainService,
    val fetchUserUseCase: FetchUserUseCase,
    val fetchUsersDailyDatasUseCase: FetchUsersDailyDatasUseCase,
    val generateNewDailyDataUseCase: GenerateNewDailyDataUseCase,
    val getTrainersUseCase: GetTrainersUseCase,
    val updateUserUseCase: UpdateUserUseCase,
    val applyToTrainerAppointmentUseCase: ApplyToTrainerAppointmentUseCase,
    val fetchTrainersRequests: FetchTrainersRequestsUseCase,
    val fetchRequestedAppointments: FetchRequestedAppointmentsUseCase,
)