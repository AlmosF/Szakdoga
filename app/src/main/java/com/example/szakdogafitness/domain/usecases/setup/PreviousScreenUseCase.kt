package com.example.szakdogafitness.domain.usecases.setup

import androidx.navigation.NavHostController
import com.example.szakdogafitness.data.setup.ISetupService

class PreviousScreenUseCase(private val repository: ISetupService){
    operator fun invoke(currentScreen: Int, role: Int, navHostController: NavHostController) =
        repository.previousScreen(currentScreen, role, navHostController)
}