package com.example.szakdogafitness.domain.usecases.setup

import com.example.szakdogafitness.data.setup.ISetupService

class PreviousButtonVisibleUseCase(private val repository: ISetupService){

    operator fun invoke(currentScreen: Int) : Boolean {
        return repository.previousButtonVisible(currentScreen)
    }

}