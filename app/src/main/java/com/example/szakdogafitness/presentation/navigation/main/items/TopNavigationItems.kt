package com.example.szakdogafitness.presentation.navigation.main.items

import com.example.szakdogafitness.presentation.navigation.main.MainScreenRoute

data class TopNavigationItem(
    val title: String,
    val route: String,
    val image: String = ""
) {
    fun withImage(image: String) = copy(image = image)

    companion object {
        val ProfileItem = TopNavigationItem(
            title = "Profile",
            route = MainScreenRoute.Profile.route
        )

        val items = listOf(ProfileItem)
    }
}
