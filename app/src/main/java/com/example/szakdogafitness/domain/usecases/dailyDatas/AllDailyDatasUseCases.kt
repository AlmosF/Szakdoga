package com.example.szakdogafitness.domain.usecases.dailyDatas

import com.example.szakdogafitness.data.dailydatas.IDailyDatasService

class AllDailyDatasUseCases(
    val repository: IDailyDatasService,
    val fetchUsersDailyDatas: FetchUsersDailyDatasUseCase,
    val generateNewDailyData: GenerateNewDailyDataUseCase,
    val targetKcalModify: TargetKcalModifyUseCase,
    val targetWaterModify: TargetWaterModifyUseCase,
    val waterIntakeModify: WaterIntakeModifyUseCase,
)