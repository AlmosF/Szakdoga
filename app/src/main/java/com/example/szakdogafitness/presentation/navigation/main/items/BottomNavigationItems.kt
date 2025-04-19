package com.example.szakdogafitness.presentation.navigation.main.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.szakdogafitness.presentation.navigation.main.MainScreenRoute

sealed class BottomNavigationItems (
    val title: String,
    val route: String,
    val icon: ImageVector
) {
    //First
    data object LifeStyleItem : BottomNavigationItems(
        title = "LifeStyle",
        route = MainScreenRoute.LifeStyle.route,
        icon = Icons.Default.HeartBroken
    )
    //Second
    data object NutritionItem : BottomNavigationItems(
        title = "Nutrition",
        route = MainScreenRoute.Nutrition.route,
        icon = Icons.Default.Fastfood
    )
    //Third
    data object SearchItem : BottomNavigationItems(
        title = "Nutrition",
        route = MainScreenRoute.Search.route,
        icon = Icons.Default.Search
    )

    data object TraineesItem : BottomNavigationItems(
        title = "Trainees",
        route = MainScreenRoute.Trainees.route,
        icon = Icons.AutoMirrored.Default.DirectionsRun
    )

    //Fourth
    data object TrainerItem : BottomNavigationItems(
        title = "Trainer",
        route = MainScreenRoute.Trainer.route,
        icon = Icons.Default.MonitorWeight
    )

    data object RequestsItem : BottomNavigationItems(
        title = "Requests",
        route = MainScreenRoute.Requests.route,
        icon = Icons.Default.PersonAddAlt1
    )

    //Fifth
    data object MenuItem : BottomNavigationItems(
        title = "Menu",
        route = MainScreenRoute.Menu.route,
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