package com.example.szakdogafitness.presentation.navigation.setup

sealed class SetupScreenRoute (val route: String){
    object RoleSelection: SetupScreenRoute("role_selection")
    object TraineeOne: SetupScreenRoute("trainee_one")
    object TraineeTwo: SetupScreenRoute("trainee_two")
    object TrainerOne: SetupScreenRoute("trainer_one")
    object TrainerTwo: SetupScreenRoute("trainer_two")
}