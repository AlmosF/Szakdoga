package com.example.szakdogafitness.domain.usecases.setup

import androidx.navigation.NavHostController
import com.example.szakdogafitness.data.setup.ISetupService

/*class SetupButtonsUseCase {
    fun navigateNext(currentScreen: Int, role: Int, navHostController: NavHostController) {
        val nextScreen = when (currentScreen) {
            0 -> if (role == 1) SetupScreenRoute.TrainerOne.route else SetupScreenRoute.TraineeOne.route
            1 -> if (role == 1) SetupScreenRoute.TrainerTwo.route else SetupScreenRoute.TraineeTwo.route
            else -> null
        }
        nextScreen?.let {
            navHostController.navigate(it)
        }
    }

    fun navigateBack(currentScreen: Int, navHostController: NavHostController) {
        if (currentScreen > 0) {
            navHostController.popBackStack()
        }
    }
}*/

class NextScreenUseCase(private val repository: ISetupService){
    operator fun invoke(currentScreen: Int, role: Int, navHostController: NavHostController) =
        repository.nextScreen(currentScreen, role, navHostController)
}