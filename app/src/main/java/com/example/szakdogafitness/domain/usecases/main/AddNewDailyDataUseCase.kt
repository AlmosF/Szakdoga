package com.example.szakdogafitness.domain.usecases.main

import com.example.szakdogafitness.data.main.IMain
import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.model.DailyDatas


class AddNewDailyDataUseCase(private val repository: IMain){
    suspend operator fun invoke(
        current: DailyDatas,
        newDay: DailyData
    ) = repository.addNewDailyData(
        current = current,
        newDay = newDay
    )
}