package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMainService

class AllMainUseCases(
    val repository: IMainService,
    val fetchUserUseCase: FetchUserUseCase,
    val fetchUsersDailyDatasUseCase: FetchUsersDailyDatasUseCase,
    val generateNewDailyDataUseCase: GenerateNewDailyDataUseCase,
    val getTrainersUseCase: GetTrainersUseCase
)