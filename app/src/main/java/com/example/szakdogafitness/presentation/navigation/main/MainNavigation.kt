package com.example.szakdogafitness.presentation.navigation.main

import android.R.attr.text
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.model.Nutrition
import com.example.szakdogafitness.feature.lifestyle.LifeStyleScreen
import com.example.szakdogafitness.feature.nutrition.NutritionScreen
import com.example.szakdogafitness.feature.trainer.SearchTrainerScreen
import com.example.szakdogafitness.presentation.navigation.main.items.BottomNavigationItems
import com.example.szakdogafitness.presentation.navigation.main.items.BottomNavigationItems.LifeStyleItem
import com.example.szakdogafitness.presentation.navigation.main.items.BottomNavigationItems.SearchItem
import com.example.szakdogafitness.viewmodel.AuthenticationEvent
import com.example.szakdogafitness.viewmodel.AuthenticationViewModel
import com.example.szakdogafitness.viewmodel.MainEvent
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
    val mainState by mainViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        Log.d("MainNavigation", "Fetching")
        mainViewModel.onEvent(MainEvent.FetchTrainees)
        mainViewModel.onEvent(MainEvent.FetchTrainers)
    }

    LaunchedEffect(mainState.trainers, mainState.trainees) {
        Log.d("MainNavigation", "Trainers: ${mainState.trainers}")
        Log.d("MainNavigation", "Trainees: ${mainState.trainees}")
    }


    /*LaunchedEffect(Unit) {
        profileViewModel.onEvent(ProfileEvent.GetUser)
    }*/

    LaunchedEffect(Unit) { //Buggos ha nem fetchelek eloszor es hozzaadok egy ujat
        //mainViewModel.onEvent(MainEvent.FetchDailyData)
    }

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
                Text("Asd")
                //LifeStyleScreen()
                /*Text(
                    text = "LifeStyle",
                    modifier = Modifier
                        .clickable{
                            authViewModel.onEvent(AuthenticationEvent.LogoutButtonClicked)
                        }
                )*/
            }
            composable(MainScreenRoute.NutritionGraph.Nutrition.route){
                /*Column {
                    Text(
                        text = "Nutrition",
                        modifier = Modifier
                            .clickable{
                                mainViewModel.onEvent(MainEvent.AddNewDailyData(
                                    newDay = DailyData(
                                        waterIntake = 1.2,
                                        targetWater = 1.5,
                                        steps = 123,
                                        targetSteps = 4000,
                                        targetKcal = 2000,
                                        foodIntake = mapOf(
                                            "Breakfast" to listOf(
                                                Nutrition(1, "Egg", 100, 10, 10, 10, 10, ""),
                                                Nutrition(1, "AA", 100, 10, 10, 10, 10, ""),
                                            ),
                                            "Lunch" to listOf(
                                                Nutrition(1, "EE", 100, 10, 10, 10, 10, ""),
                                                Nutrition(1, "AAF", 100, 10, 10, 10, 10, ""),
                                            ),
                                            "Dinner" to listOf(
                                                Nutrition(1, "GGG", 100, 10, 10, 10, 10, ""),
                                                Nutrition(1, "ZZZ", 100, 10, 10, 10, 10, ""),
                                            )
                                        ),
                                    )
                                ))
                            }
                    )
                    Text(
                        "Nutrition2",
                        modifier = Modifier
                            .clickable{
                                mainViewModel.onEvent(MainEvent.FetchDailyData)
                            }
                    )

                }*/
                NutritionScreen()
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