package com.example.szakdogafitness.domain.usecases.setup

import com.example.szakdogafitness.data.setup.ISetupService
import com.example.szakdogafitness.feature.setup.SetupProfileState

class FinishButtonUseCase(private val repository: ISetupService){

    suspend operator fun invoke(state: SetupProfileState) {
        repository.finishButtonClicked(state)
    }

}