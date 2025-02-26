package com.example.szakdogafitness.domain.usecases.setup

import com.example.szakdogafitness.data.setup.ISetupService

class NextButtonLabelUseCase(private val repository: ISetupService){

    operator fun invoke(currentScreen: Int) : String {
        return repository.nextButtonLabel(currentScreen)
    }

}