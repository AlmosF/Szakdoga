package com.example.szakdogafitness.domain.usecases.setup

import com.example.szakdogafitness.data.setup.ISetupService
import com.example.szakdogafitness.feature.setup.SetupProfileState

class NextButtonEnabledUseCase(private val repository: ISetupService){

    operator fun invoke(state: SetupProfileState): Boolean{
        return repository.nextButtonEnabled(state)
    }

}