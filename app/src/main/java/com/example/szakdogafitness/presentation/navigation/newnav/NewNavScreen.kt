package com.example.szakdogafitness.presentation.navigation.newnav

import android.R.attr.label
import android.R.attr.onClick
import android.net.http.SslCertificate.restoreState
import android.net.http.SslCertificate.saveState
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.szakdogafitness.viewmodel.AuthenticationViewModel
import com.example.szakdogafitness.viewmodel.ProfileViewModel
import com.example.szakdogafitness.viewmodel.RootNavViewModel
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.navigation
import com.example.szakdogafitness.feature.auth.AuthenticateScreen
import com.example.szakdogafitness.feature.lifestyle.LifeStyleScreen
import com.example.szakdogafitness.feature.nutrition.NutritionScreen
import com.example.szakdogafitness.feature.nutrition.SearchNutritionScreen
import com.example.szakdogafitness.feature.setup.SetupScreen
import com.example.szakdogafitness.feature.setup.SetupScreenBox
import com.example.szakdogafitness.feature.setup.TraineeOne
import com.example.szakdogafitness.feature.setup.trainee.TraineeTwo
import com.example.szakdogafitness.feature.setup.trainer.TrainerOne
import com.example.szakdogafitness.feature.setup.trainer.TrainerTwo
import com.example.szakdogafitness.feature.trainer.SearchTrainerScreen
import com.example.szakdogafitness.presentation.navigation.main.MainScreenRoute
import com.example.szakdogafitness.presentation.navigation.main.items.BottomNavigationItems
import com.example.szakdogafitness.presentation.navigation.root.RootScreenRoute
import com.example.szakdogafitness.presentation.navigation.setup.SetupScreenRoute
import com.example.szakdogafitness.util.UiEvent
import com.example.szakdogafitness.viewmodel.AuthenticationEvent
import com.example.szakdogafitness.viewmodel.MainEvent
import com.example.szakdogafitness.viewmodel.MainViewModel
import com.example.szakdogafitness.viewmodel.ProfileEvent
import com.example.szakdogafitness.viewmodel.RootNavEvent
import com.example.szakdogafitness.viewmodel.RootNavViewModel_Factory
import com.example.szakdogafitness.viewmodel.SetupViewModel
import kotlinx.coroutines.launch

@Composable
fun NewNavScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthenticationViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    rootNavViewModel: RootNavViewModel = hiltViewModel(),
    setupViewModel: SetupViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {

    val authState by authViewModel.state.collectAsStateWithLifecycle()
    val profileState by profileViewModel.state.collectAsStateWithLifecycle()
    val rootNavState by rootNavViewModel.state.collectAsStateWithLifecycle()
    val mainState by mainViewModel.state.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) { //NavHostController initialization
        rootNavViewModel.onEvent(RootNavEvent.NavController(navController))
    }

    LaunchedEffect(Unit) {
        Log.d("MainNavigation", "Fetching")
        mainViewModel.onEvent(MainEvent.FetchTrainees)
        mainViewModel.onEvent(MainEvent.FetchTrainers)
    }

    //LaunchedEffect
    LaunchedEffect(authState.isLoggedIn) {
        if(authState.isLoggedIn){ //Change it to If
            profileViewModel.onEvent(ProfileEvent.GetUser)
        }
    }

    LaunchedEffect(mainState.trainers) {
        mainState.trainers.forEach {
            Log.d("MainNavigationzz", "Trainers: ${it.trainer.rating}")
            Log.d("MainNavigationzz", "Trainers: ${it.trainer}")
        }
    }

    //LaunchedEffect
    LaunchedEffect(authState.isLoggedIn == true, profileState.user?.isSetupDone) {

        Log.d("RootNavigation-LE", "isLoggedIn: ${authState.isLoggedIn}")
        Log.d("RootNavigation-LE", "isSetupDone: ${profileState.user?.isSetupDone}")
        Log.d("RootNavigation-LE", "state: ${profileState.user}")
        rootNavViewModel.onEvent(
            RootNavEvent.Navigate(
                isLoggedIn = authState.isLoggedIn,
                isSetupDone = profileState.user?.isSetupDone == true,
                isProfileNull = profileState.user == null
            )
        )
    }

    //LaunchedEffect
    LaunchedEffect(Unit) {
        authViewModel.checkSignInState()
        authViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message?.asString(context) ?: ""
                        )
                    }
                }

                is UiEvent.Failure -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message.asString(context)
                        )
                    }
                }
            }
        }
    }


    Scaffold (
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier,
        bottomBar = {
            val currentNav = navController.currentBackStackEntryAsState().value?.destination?.route
            if (currentNav != RootScreenRoute.Authentication.route &&
                currentNav != RootScreenRoute.SetupScreen.route
                ) {
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
                                navController.navigate(navItem.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
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

        }
    ) { innerPadding ->

        NavHost(
            modifier = modifier.padding(innerPadding),
            startDestination = "about",
            navController = navController
        ) {
            composable("about") {
                Text(
                    "About",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {
                            navController.navigate(RootScreenRoute.Authentication.route)
                        }
                )
            }
            composable(RootScreenRoute.Authentication.route) {
                AuthenticateScreen(
                    viewModel = authViewModel,
                    navController = navController
                )
            }
            composable(RootScreenRoute.SetupScreen.route){
                SetupScreenBox(){}
            }


            /*navigation(
                route = RootScreenRoute.SetupScreen.route,
                startDestination = ""
            ) {

            }*/
            navigation (
                route = MainScreenRoute.NutritionGraph.route,
                startDestination = MainScreenRoute.NutritionGraph.Nutrition.route
            ) {
                composable (MainScreenRoute.NutritionGraph.Nutrition.route) {
                    NutritionScreen()
                }
                composable (MainScreenRoute.NutritionGraph.Search.route) {
                    SearchNutritionScreen()
                }
            }
            navigation (
                route = RootScreenRoute.MainScreen.route,
                startDestination = MainScreenRoute.LifeStyle.route
            ) {
                composable (MainScreenRoute.LifeStyle.route) {
                    /*Text(
                        "LifeStyle",
                        modifier = Modifier
                            .clickable{
                                authViewModel.onEvent(AuthenticationEvent.LogoutButtonClicked)
                            }
                    )*/
                    LifeStyleScreen()
                }
                /*composable (MainScreenRoute.Nutrition.route) {
                    Text("Nutrition")
                }*/
                composable (MainScreenRoute.Profile.route) {
                    Text("Profile")
                }
                composable (MainScreenRoute.Search.route) {
                    SearchTrainerScreen(
                        trainers = mainState.trainers
                    )
                }
                composable (MainScreenRoute.Trainer.route) {
                    Text(
                        "Trainer",
                        modifier = Modifier
                            .clickable {
                                authViewModel.onEvent(AuthenticationEvent.LogoutButtonClicked)
                            }
                    )
                }
                composable (MainScreenRoute.Trainees.route) {
                    Text(
                        "Trainees",
                        modifier = Modifier
                            .clickable {
                                authViewModel.onEvent(AuthenticationEvent.LogoutButtonClicked)
                            }
                    )
                }
                composable (MainScreenRoute.Requests.route) {
                    Text("Requests")
                }
                composable (MainScreenRoute.Menu.route) {
                    Text("Menu")
                }
            }
        }

    }
}


fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation (
        route = RootScreenRoute.Authentication.route,
        startDestination = "login"
    ) {
        composable("login") { TODO() }
        composable("register") { TODO() }
        composable("forgotPassword") { TODO() }
    }
}

fun NavGraphBuilder.mainGraph(navController: NavHostController) {
    navigation (
        route = RootScreenRoute.MainScreen.route,
        startDestination = MainScreenRoute.LifeStyle.route
    ) {
        composable (MainScreenRoute.LifeStyle.route) {
            LifeStyleScreen()
        }
        composable (MainScreenRoute.NutritionGraph.Nutrition.route) {
            Text("Nutrition")
        }
        composable (MainScreenRoute.Profile.route) {
            Text("Profile")
        }
        composable (MainScreenRoute.Search.route) {
            Text("Search")
        }
        composable (MainScreenRoute.Trainer.route) {
            Text("Trainer")
        }
        composable (MainScreenRoute.Trainees.route) {
            Text(
                "Trainees",
            )
        }
        composable (MainScreenRoute.Requests.route) {
            Text("Requests")
        }
        composable (MainScreenRoute.Menu.route) {
            Text("Menu")
        }
    }
}

fun NavGraphBuilder.setupGraph(navController: NavHostController, setupViewModel: SetupViewModel) {
    navigation (
        route = RootScreenRoute.SetupScreen.route,
        startDestination = SetupScreenRoute.RoleSelection.route
    ) {
        composable(SetupScreenRoute.RoleSelection.route) { SetupScreen(setupViewModel = setupViewModel) }
        composable(SetupScreenRoute.TrainerOne.route) { TrainerOne(setupViewModel = setupViewModel) }
        composable(SetupScreenRoute.TrainerTwo.route) { TrainerTwo(setupViewModel = setupViewModel) }
        composable(SetupScreenRoute.TraineeOne.route) { TraineeOne(setupViewModel = setupViewModel) }
        composable(SetupScreenRoute.TraineeTwo.route) { TraineeTwo(setupViewModel = setupViewModel) }
    }
}