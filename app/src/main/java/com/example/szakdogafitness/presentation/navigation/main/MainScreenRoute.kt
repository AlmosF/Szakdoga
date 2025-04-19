package com.example.szakdogafitness.presentation.navigation.main

sealed class MainScreenRoute(val route: String){
    object LifeStyle : MainScreenRoute("lifestyle")
    object Nutrition : MainScreenRoute("nutrition")
    object Profile : MainScreenRoute("profile")
    //Trainee
    object Search: MainScreenRoute("search")
    object Trainer: MainScreenRoute("trainer")
    //Trainer
    object Trainees: MainScreenRoute("trainees")
    object Requests: MainScreenRoute("requests")
    object Menu: MainScreenRoute("menu")
}