package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMain

class AllMainUseCases(
    val repository: IMain,
    val fetchDailyData: FetchDailyDataUseCase,
    val addNewDailyData: AddNewDailyDataUseCase,
    val fetchTrainersUseCase: FetchTrainersUseCase,
    val fetchTraineesUseCase: FetchTraineesUseCase,
)