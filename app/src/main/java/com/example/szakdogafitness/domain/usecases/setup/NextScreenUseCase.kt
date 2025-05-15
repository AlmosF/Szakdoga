package com.example.szakdogafitness.domain.usecases.setup

import androidx.navigation.NavHostController
import com.example.szakdogafitness.data.setup.ISetupService

class NextScreenUseCase(private val repository: ISetupService){
    operator fun invoke(currentScreen: Int, role: Int, navHostController: NavHostController) =
        repository.nextScreen(currentScreen, role, navHostController)
}