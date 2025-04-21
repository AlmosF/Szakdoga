package com.example.szakdogafitness.presentation.navigation.root

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationItems (
    val title: String,
    val route: String,
    val icon: ImageVector
) {
    //First - "lifestyle"
    data object LifeStyleItem : BottomNavigationItems(
        title = "LifeStyle",
        route = RootScreenRoute.LifeStyle.route,
        icon = Icons.Default.HeartBroken
    )
    //Second - "nutrition"
    data object NutritionItem : BottomNavigationItems(
        title = "Nutrition",
        route = RootScreenRoute.Nutrition.route,
        icon = Icons.Default.Fastfood
    )
    //Third
    //"search"
    data object SearchItem : BottomNavigationItems(
        title = "Nutrition",
        route = RootScreenRoute.Search.route,
        icon = Icons.Default.Search
    )

    //"trainees"
    data object TraineesItem : BottomNavigationItems(
        title = "Trainees",
        route = RootScreenRoute.Trainees.route,
        icon = Icons.AutoMirrored.Default.DirectionsRun
    )

    //Fourth
    //"trainer"
    data object TrainerItem : BottomNavigationItems(
        title = "Trainer",
        route = RootScreenRoute.Trainer.route,
        icon = Icons.Default.MonitorWeight
    )
    //"requests"
    data object RequestsItem : BottomNavigationItems(
        title = "Requests",
        //route = MainScreenRoute.Requests.route,
        route = RootScreenRoute.Requests.route,
        icon = Icons.Default.PersonAddAlt1
    )

    //Fifth
    //"menu" - deprecated
    data object MenuItem : BottomNavigationItems(
        title = "Menu",
        //route = MainScreenRoute.Menu.route,
        route = RootScreenRoute.Menu.route,
        icon = Icons.Default.Menu
    )

    companion object {
        val trianeeItems = mutableListOf(
            LifeStyleItem,
            NutritionItem,
            SearchItem,
            TrainerItem,
            //MenuItem
        )
        val trainerItems = mutableListOf(
            LifeStyleItem,
            NutritionItem,
            TraineesItem,
            RequestsItem,
            //MenuItem
        )
    }
}