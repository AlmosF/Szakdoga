package com.example.szakdogafitness.presentation.navigation.root

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.szakdogafitness.StepCountService
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.feature.CameraScan.CameraScreen
import com.example.szakdogafitness.feature.auth.AuthenticateScreen
import com.example.szakdogafitness.feature.lifestyle.LifeStyleScreen
import com.example.szakdogafitness.feature.loading.CheckUserStatusScreen
import com.example.szakdogafitness.feature.nutrition.ManageNutritionItemScreen
import com.example.szakdogafitness.feature.nutrition.ModifyNutritionScreen
import com.example.szakdogafitness.feature.nutrition.NutritionScreen
import com.example.szakdogafitness.feature.nutrition.SearchNutritionScreen
import com.example.szakdogafitness.feature.profile.ProfileScreen
import com.example.szakdogafitness.feature.request.RequestDetailScreen
import com.example.szakdogafitness.feature.request.RequestScreen
import com.example.szakdogafitness.feature.search.SearchTrainerDetailsScreen
import com.example.szakdogafitness.feature.search.SearchTrainerScreen
import com.example.szakdogafitness.feature.setup.SetupScreenBox
import com.example.szakdogafitness.feature.trainee.TraineeDetailScreen
import com.example.szakdogafitness.feature.trainee.TraineesScreen
import com.example.szakdogafitness.feature.trainer.TrainerDetailsScreen
import com.example.szakdogafitness.feature.trainer.TrainerScreen
import com.example.szakdogafitness.util.UiEvent
import com.example.szakdogafitness.feature.CameraScan.CameraViewModel
import com.example.szakdogafitness.feature.lifestyle.DailyDatasEvent
import com.example.szakdogafitness.feature.lifestyle.DailyDatasViewModel
import com.example.szakdogafitness.feature.nutrition.MealTypesViewModel
import com.example.szakdogafitness.feature.profile.ProfileEvent
import com.example.szakdogafitness.feature.profile.ProfileViewModel
import com.example.szakdogafitness.feature.lifestyle.StepCountViewModel
import com.example.szakdogafitness.feature.trainee.TraineesEvent
import com.example.szakdogafitness.feature.trainee.TraineesViewModel
import com.example.szakdogafitness.feature.trainer.TrainersViewModel
import com.example.szakdogafitness.presentation.navigation.AppBarEvent
import com.example.szakdogafitness.presentation.navigation.AppBarViewModel
import com.example.szakdogafitness.viewmodel.AuthenticationEvent
import com.example.szakdogafitness.viewmodel.AuthenticationViewModel
import kotlinx.coroutines.launch


fun Context.startStepService(){
    val intent = Intent(this, StepCountService::class.java)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startForegroundService(intent)
    } else {
        startService(intent)
    }
}

fun Context.stopStepService(){
    val intent = Intent(this, StepCountService::class.java)
    stopService(intent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRootNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    networkOn: Boolean,
    authViewModel: AuthenticationViewModel = hiltViewModel(),
    appBarViewModel: AppBarViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(key = authViewModel.getUserId()), //Mimic of the mainVM
    mealTypesViewModel: MealTypesViewModel = hiltViewModel(),
    dailyDatasViewModel: DailyDatasViewModel = hiltViewModel(),
    trainersViewModel: TrainersViewModel = hiltViewModel(),
    traineesViewModel: TraineesViewModel = hiltViewModel(),
    cameraViewModel: CameraViewModel = hiltViewModel(),
    stepCountViewModel: StepCountViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val authState by authViewModel.state.collectAsStateWithLifecycle()
    val appBarState by appBarViewModel.state.collectAsStateWithLifecycle()
    val profileState by profileViewModel.state.collectAsStateWithLifecycle()
    val dailyDatasState by dailyDatasViewModel.state.collectAsStateWithLifecycle()
    val trainersSate by trainersViewModel.state.collectAsStateWithLifecycle()
    val cameraState by cameraViewModel.state.collectAsStateWithLifecycle()




    LaunchedEffect(networkOn) {
        authViewModel.checkOnlineState(context)
        scope.launch {
            snackbarHostState.showSnackbar(
                message = if (networkOn) {
                    "Online"
                } else {
                    "Offline"
                }
            )
        }
    }

    LaunchedEffect(navBackStackEntry?.destination?.route) { //AppBarVisibility - Settings
        appBarViewModel.onEvent(AppBarEvent.AppBarVisibility(navBackStackEntry?.destination?.route ?: ""))
    }
    LaunchedEffect(Unit) {
        authViewModel.checkSignInState()
        authViewModel.checkOnlineState(context)
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
        appBarViewModel.onEvent(AppBarEvent.CurrentNavControllerChanged(navController))
    }


    val startDestination = when { //Changed to the top one at 2025.04.06 - Changed again.
        authState.isLoggedIn && authState.isOnline -> RootScreenRoute.Loading.route
        else -> RootScreenRoute.Authentication.route
    }

    LaunchedEffect(authState.isLoggedIn) {
        Log.d("RootNavigation", "isLoggedIn: ${authState.isLoggedIn}")
        if (!authState.isLoggedIn){
            profileViewModel.onEvent(ProfileEvent.ClearState)
            navController.navigate(RootScreenRoute.Authentication.route) {
                popUpTo(0)
            }
        }
    }
    Scaffold (
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            AnimatedVisibility(
                visible =  appBarState.topBarVisibility) {appBarState.topBarComposable.invoke()}
        },
        bottomBar = {
            AnimatedVisibility(
                visible = appBarState.bottomBarVisibility
            ){
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
                                Log.d("RootNavigationNAVIGATE", "navItem.route: ${navItem.route}")
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
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController=navController,
            startDestination = startDestination
        ){
            //Authentication
            composable(RootScreenRoute.Authentication.route) {
                AuthenticateScreen(
                    viewModel = authViewModel,
                    navController = navController
                )
            }
            //Loading - CheckUser
            composable(RootScreenRoute.Loading.route) {
                CheckUserStatusScreen(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    profileViewModel = profileViewModel
                )
            }
            //Setup
            composable(RootScreenRoute.SetupScreen.route) {
                SetupScreenBox(){
                    navController.navigate(RootScreenRoute.Loading.route)
                }
            }
            //LifeStyle - First
            composable(RootScreenRoute.LifeStyle.route) {
                LifeStyleScreen(
                    modifier = Modifier,
                    profileViewModel = profileViewModel,
                    appBarViewModel = appBarViewModel,
                    navController = navController,
                    dailyDatasViewModel = dailyDatasViewModel,
                    mealTypesViewModel = mealTypesViewModel,
                    stepCountViewModel = stepCountViewModel,
                )

            }
            //Nutrition - Second
            composable(RootScreenRoute.Nutrition.route){
                NutritionScreen(
                    mealTypesViewModel = mealTypesViewModel,
                    dailyDatasViewModel = dailyDatasViewModel,
                    cameraViewModel = cameraViewModel,
                    appBarViewModel = appBarViewModel,
                    onAINavigate = {
                        navController.navigate(RootScreenRoute.NutritionAI.route)
                    },
                    onSearchNavigate = {
                        navController.navigate(RootScreenRoute.NutritionSearch.route)
                    },
                    onBarcodeNavigate = {
                        navController.navigate(RootScreenRoute.NutritionBarcode.route)
                    }
                )
            }
            composable(RootScreenRoute.NutritionAI.route) {
                CameraScreen (
                    cameraViewModel = cameraViewModel,
                ) {
                    navController.popBackStack()
                }
            }
            composable(RootScreenRoute.NutritionSearch.route) {
                SearchNutritionScreen(
                    mealTypesViewModel = mealTypesViewModel,
                    appBarViewModel = appBarViewModel
                ) {
                    navController.popBackStack()
                }
            }
            composable(RootScreenRoute.NutritionBarcode.route) {
                Text(text = "NutritionBarcode")
            }
            //Profile - None
            composable(RootScreenRoute.Profile.route){
                ProfileScreen (
                    appBarViewModel = appBarViewModel,
                    onNavigateBack = {
                        navController.navigate(RootScreenRoute.LifeStyle.route){
                            popUpTo(RootScreenRoute.LifeStyle.route){
                                inclusive = true
                            }
                        }
                    },
                    onLogout = {
                        context.stopStepService()
                        stepCountViewModel.resetOnLogout()
                        profileViewModel.onEvent(ProfileEvent.StopLocationMonitoring)
                        authViewModel.onEvent(AuthenticationEvent.LogoutButtonClicked)
                        dailyDatasViewModel.onEvent(DailyDatasEvent.ResetDailyData)
                    }
                ){
                    navController.navigate(RootScreenRoute.EditFoods.route){

                    }
                }
            }

            composable (RootScreenRoute.EditFoods.route) {
                ModifyNutritionScreen (
                    appBarVIewModel = appBarViewModel,
                    onItemClicked = {
                        navController.navigate(RootScreenRoute.EditFoodDetail.createRoute(it))
                    },
                ) {
                    navController.popBackStack()
                }
            }

            composable(RootScreenRoute.EditFoodDetail.route) { entry ->
                val id = entry.arguments?.getString("id")
                ManageNutritionItemScreen(
                    id = id!!,
                    appBarVIewModel = appBarViewModel,
                    mealTypesViewModel = mealTypesViewModel
                ) {
                    navController.popBackStack()
                }
            }
            //Search - Third
            composable(RootScreenRoute.Search.route){
                //Text(text = "Search")
                SearchTrainerScreen( //fixme: TrainersVM needed
                    navController = navController,
                    trainersViewModel = trainersViewModel,
                    currentUser = profileState.user ?: User(), // FIXME: WONT BE NULL ANYWAYS
                    appBarViewModel = appBarViewModel,
                ) { userId ->
                    navController.navigate(RootScreenRoute.SearchDetail.createRoute(userId))
                }
            }

            composable(RootScreenRoute.SearchDetail.route) { entry ->
                val id = entry.arguments?.getString("id")
                val trainer = trainersSate.trainers.find { it?.userId == id } //With MainVM

                //Text(text = "SearchDetail: ${trainer}")
                SearchTrainerDetailsScreen(
                    data = trainer!!,
                    currentUser = profileState.user!!,
                    trainersViewModel = trainersViewModel,
                    appBarViewModel = appBarViewModel,
                ){
                    navController.popBackStack()
                } // FIXME: !! is not safe
            }
            //Trainer - Fourth
            composable(RootScreenRoute.Trainer.route){
                TrainerScreen(
                    currentUser = profileState.user ?: User(),
                    appBarViewModel = appBarViewModel,
                    trainersViewModel = trainersViewModel
                ) { userId ->
                    navController.navigate(RootScreenRoute.TrainerDetail.createRoute(userId))
                }
            }

            composable (RootScreenRoute.TrainerDetail.route) { entry ->
                val id = entry.arguments?.getString("id")
                TrainerDetailsScreen(
                    data = trainersSate.acceptedTrainers.find {
                        it.second.userId == id
                    }!!,
                    currentUser = profileState.user!!,
                    trainersViewModel = trainersViewModel,
                    appBarViewModel = appBarViewModel,
                ) {
                    navController.popBackStack()
                }
            }

            //Trainees - Third
            composable(RootScreenRoute.Trainees.route){
                TraineesScreen(
                    traineesViewModel = traineesViewModel,
                    appBarViewModel = appBarViewModel
                ) {
                    traineesViewModel.onEvent(TraineesEvent.SelectTrainee(
                        it
                    ))
                    navController.navigate(RootScreenRoute.TraineeDetail.createRoute(
                        it
                    ))
                }
            }

            composable(RootScreenRoute.TraineeDetail.route) { entry ->
                val id = entry.arguments?.getString("id")
                TraineeDetailScreen(
                    traineesViewModel = traineesViewModel,
                    appBarViewModel = appBarViewModel,
                    mealTypesViewModel = mealTypesViewModel,
                    id = id!!,
                    onNavigateBack = {
                        navController.navigate(RootScreenRoute.Trainees.route){
                            popUpTo(RootScreenRoute.Trainees.route){
                                inclusive = true
                            }
                        }
                    }
                )
            }

            //Requests - Fourth
            composable(RootScreenRoute.Requests.route){
                RequestScreen(
                    traineesViewModel = traineesViewModel,
                    appBarViewModel = appBarViewModel,
                ){ id ->
                    navController.navigate(RootScreenRoute.RequestDetail.createRoute(id))
                }
            }

            composable(RootScreenRoute.RequestDetail.route) { entry ->
                val id = entry.arguments?.getString("id")
                RequestDetailScreen(
                    traineesViewModel = traineesViewModel,
                    appBarViewModel = appBarViewModel,
                    traineeId = id!!
                ) {
                    navController.popBackStack()
                }
            }

            //Menu - Deprecated
            composable(RootScreenRoute.Menu.route){
                Text(text = "Menu")
            }
        }
    }
}