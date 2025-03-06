package com.example.szakdogafitness.presentation.navigation.main

import android.R.attr.text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.szakdogafitness.presentation.navigation.main.items.BottomNavigationItems
import com.example.szakdogafitness.presentation.navigation.main.items.BottomNavigationItems.LifeStyleItem
import com.example.szakdogafitness.presentation.navigation.main.items.BottomNavigationItems.SearchItem
import com.example.szakdogafitness.viewmodel.AuthenticationEvent
import com.example.szakdogafitness.viewmodel.AuthenticationViewModel
import com.example.szakdogafitness.viewmodel.MainViewModel
import com.example.szakdogafitness.viewmodel.ProfileEvent
import com.example.szakdogafitness.viewmodel.ProfileViewModel

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    rootNavController: NavHostController,
    snackbarHostState: SnackbarHostState,
    authViewModel: AuthenticationViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel() //Not fix yet
) {

    val navController = rememberNavController()
    val authState by authViewModel.state.collectAsStateWithLifecycle()
    val profileState by profileViewModel.state.collectAsStateWithLifecycle()

    /*LaunchedEffect(Unit) {
        profileViewModel.onEvent(ProfileEvent.GetUser)
    }*/

    Scaffold (
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val items = when (profileState.user?.role) {
                    1 -> BottomNavigationItems.trainerItems
                    2 -> BottomNavigationItems.trianeeItems
                    else -> emptyList()
                }
                items.forEach { navItem ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = navItem.icon,
                                contentDescription = navItem.title
                            )
                        },
                        label = {},
                        selected = currentDestination?.route == navItem.route,
                        onClick = {
                            navController.navigate(navItem.route){
                                popUpTo(navController.graph.findStartDestination().id){
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }


            }
        }
    ) { innerPadding ->

        NavHost(
            modifier = Modifier
                .padding(innerPadding),
            startDestination = MainScreenRoute.LifeStyle.route,
            navController = navController
        ) {

            composable(MainScreenRoute.LifeStyle.route){
                Text(
                    text = "LifeStyle",
                    modifier = Modifier
                        .clickable{
                            authViewModel.onEvent(AuthenticationEvent.LogoutButtonClicked)
                        }
                )
            }
            composable(MainScreenRoute.Nutrition.route){
                Text(text = "Nutrition")
            }
            composable(MainScreenRoute.Profile.route){
                Text(text = "Profile")
            }
            composable(MainScreenRoute.Search.route){
                Text(text = "Search")
            }
            composable(MainScreenRoute.Trainer.route){
                Text(text = "Trainer")
            }
            composable(MainScreenRoute.Trainees.route){
                Text(text = "Trainees")
            }
            composable(MainScreenRoute.Requests.route){
                Text(text = "Requests")
            }
            composable(MainScreenRoute.Menu.route){
                Text(text = "Menu")
            }

        }

    }


}