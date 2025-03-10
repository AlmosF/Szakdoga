package com.example.szakdogafitness.domain.usecases.main

import android.util.Log
import com.example.szakdogafitness.data.main.IMainService

class FetchUsersDailyDatasUseCase (private val repository: IMainService){

    suspend operator fun invoke() = repository.getUsersDailyDatas()


}