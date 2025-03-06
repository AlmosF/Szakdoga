package com.example.szakdogafitness.presentation.navigation.root

sealed class RootScreenRoute(val route: String){
    object Profile: RootScreenRoute("profile")
    object Authentication: RootScreenRoute("authentication")
    object MainScreen: RootScreenRoute("main")
    object SetupScreen: RootScreenRoute("setup")
}