package com.example.szakdogafitness.presentation.navigation.root

sealed class RootScreenRoute(val route: String){
    object Profile: RootScreenRoute("profile")
    object Authentication: RootScreenRoute("authentication")

    object Loading: RootScreenRoute("loading")

    object EditFoods: RootScreenRoute("editFoods")
    object EditFoodDetail: RootScreenRoute("editFoods/{id}") {
        fun createRoute(id: String) = "editFoods/$id"
    }

    object MainScreen: RootScreenRoute("main")

    //###
    object LifeStyle : RootScreenRoute("lifestyle")
    object Nutrition : RootScreenRoute("nutrition")

    object NutritionAI : RootScreenRoute("nutritionAI")
    object NutritionSearch : RootScreenRoute("nutritionSearch")
    object NutritionBarcode : RootScreenRoute("nutritionBarcode")

    //Trainee
    object Search: RootScreenRoute("search")
    object SearchDetail : RootScreenRoute("search/{id}") {
        fun createRoute(id: String) = "search/$id"
    }

    object Trainer: RootScreenRoute("trainer")
    object TrainerDetail : RootScreenRoute("trainer/{id}") {
        fun createRoute(id: String) = "trainer/$id"
    }
    //Trainer
    object Trainees: RootScreenRoute("trainees")
    object TraineeDetail : RootScreenRoute("trainees/{id}") {
        fun createRoute(id: String) = "trainees/$id"
    }
    object Requests: RootScreenRoute("requests")
    object RequestDetail : RootScreenRoute("requests/{id}") {
        fun createRoute(id: String) = "requests/$id"
    }

    object Menu: RootScreenRoute("menu")
    //###

    object SetupScreen: RootScreenRoute("setup")
}