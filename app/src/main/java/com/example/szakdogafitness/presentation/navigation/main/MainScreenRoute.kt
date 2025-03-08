package com.example.szakdogafitness.presentation.navigation.main

import com.example.szakdogafitness.presentation.navigation.root.RootScreenRoute

sealed class MainScreenRoute(val route: String){
    object LifeStyle : MainScreenRoute("lifestyle")
    /*object Nutrition : MainScreenRoute("nutrition") {

        object Search : MainScreenRoute("nutrition/search")
    }*/
    object NutritionGraph : MainScreenRoute("nutrition_graph") {
        object Nutrition: MainScreenRoute("nutrition_graph/nutrition")
        object Search : MainScreenRoute("nutrition_graph/search")
    }

    object Profile : MainScreenRoute("profile")
    //Trainee
    object Search: MainScreenRoute("search")
    object Trainer: MainScreenRoute("trainer")
    //Trainer
    object Trainees: MainScreenRoute("trainees")
    object Requests: MainScreenRoute("requests")
    object Menu: MainScreenRoute("menu")
}