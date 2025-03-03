package com.example.szakdogafitness.presentation.navigation.root

sealed class RootScreenRoute(val route: String){
    object Authentication: RootScreenRoute("authentication")
    object MainScreen: RootScreenRoute("main_screen")
}