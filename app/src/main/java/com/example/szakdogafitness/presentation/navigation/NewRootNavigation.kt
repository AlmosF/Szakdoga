package com.example.szakdogafitness.presentation.navigation

import android.R.attr.text
import android.R.attr.visible
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
import com.example.szakdogafitness.feature.search.SearchTrainerScreen
import com.example.szakdogafitness.feature.setup.SetupScreenBox
import com.example.szakdogafitness.feature.trainee.TraineeDetailScreen
import com.example.szakdogafitness.feature.trainee.TraineesScreen
import com.example.szakdogafitness.feature.trainer.TrainerDetailsScreen
import com.example.szakdogafitness.feature.trainer.TrainerScreen
import com.example.szakdogafitness.presentation.navigation.main.items.BottomNavigationItems
import com.example.szakdogafitness.presentation.navigation.main.items.TopNavigationItem.Companion.items
import com.example.szakdogafitness.presentation.navigation.root.RootScreenRoute
import com.example.szakdogafitness.ui.common.appbars.BackNavWithTitle
import com.example.szakdogafitness.ui.common.appbars.LifeStyleTitle
import com.example.szakdogafitness.ui.common.appbars.TopBar
import com.example.szakdogafitness.util.UiEvent
import com.example.szakdogafitness.viewmodel.AppBarEvent
import com.example.szakdogafitness.viewmodel.AppBarViewModel
import com.example.szakdogafitness.viewmodel.AuthenticationEvent
import com.example.szakdogafitness.viewmodel.AuthenticationViewModel
import com.example.szakdogafitness.viewmodel.CameraViewModel
import com.example.szakdogafitness.viewmodel.DailyDatasEvent
import com.example.szakdogafitness.viewmodel.DailyDatasViewModel
import com.example.szakdogafitness.viewmodel.MealTypesViewModel
import com.example.szakdogafitness.viewmodel.ProfileEvent
import com.example.szakdogafitness.viewmodel.ProfileViewModel
import com.example.szakdogafitness.viewmodel.TraineesEvent
import com.example.szakdogafitness.viewmodel.TraineesViewModel
import com.example.szakdogafitness.viewmodel.TrainersViewModel
import kotlinx.coroutines.launch
import java.lang.System.exit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRootNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthenticationViewModel = hiltViewModel(),
    appBarViewModel: AppBarViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(key = authViewModel.getUserId()), //Mimic of the mainVM
    mealTypesViewModel: MealTypesViewModel = hiltViewModel(),
    dailyDatasViewModel: DailyDatasViewModel = hiltViewModel(),
    trainersViewModel: TrainersViewModel = hiltViewModel(),
    traineesViewModel: TraineesViewModel = hiltViewModel(),
    cameraViewModel: CameraViewModel = hiltViewModel()
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


    /*val startDestination = when {
        authState.isLoggedIn -> RootScreenRoute.Loading.route
        else -> RootScreenRoute.Authentication.route
    }*/
    val startDestination = when {
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
            composable(RootScreenRoute.Authentication.route) {
                AuthenticateScreen(
                    viewModel = authViewModel,
                    navController = navController
                )
            }
            composable(RootScreenRoute.Loading.route) {
                CheckUserStatusScreen(
                    modifier = Modifier.padding(innerPadding),
                    navController = navController,
                    profileViewModel = profileViewModel
                )
            }
            composable(RootScreenRoute.SetupScreen.route) {
                SetupScreenBox(){
                    navController.navigate(RootScreenRoute.Loading.route)
                }
            }
            composable(RootScreenRoute.LifeStyle.route) {
                LifeStyleScreen(
                    modifier = Modifier,
                    profileViewModel = profileViewModel,
                    appBarViewModel = appBarViewModel,
                    navController = navController,
                    dailyDatasViewModel = dailyDatasViewModel,
                    mealTypesViewModel = mealTypesViewModel
                )

            }
            composable(RootScreenRoute.Nutrition.route){
                appBarViewModel.onEvent(
                    AppBarEvent.TopBarComposableChanged(
                        {
                            TopBar(
                                title = {
                                    LifeStyleTitle(
                                        title = dailyDatasState.lifeStyleTitle,
                                        previousButtonEnabled = dailyDatasState.isPreviousDayAvailable,
                                        onPreviousDayClick = {
                                            Log.d("RootNavigation", "onPreviousDayClick")
                                            dailyDatasViewModel.onEvent(DailyDatasEvent.PreviousDay)
                                        },
                                        nextButtonEnabled = dailyDatasState.isNextDayAvailable,
                                        onNextDayClick = {
                                            Log.d("RootNavigation", "onNextDayClick")
                                            dailyDatasViewModel.onEvent(DailyDatasEvent.NextDay)
                                        }
                                    )
                                },
                            )
                        }
                    )
                )
                NutritionScreen(
                    mealTypesViewModel = mealTypesViewModel,
                    dailyDatasViewModel = dailyDatasViewModel,
                    cameraViewModel = cameraViewModel,
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
                appBarViewModel.onEvent(
                    AppBarEvent.TopBarComposableChanged(
                        {
                            BackNavWithTitle(title = "Search Nutrition") {
                                navController.popBackStack()
                            }
                        }
                    )
                )
                SearchNutritionScreen(
                    mealTypesViewModel = mealTypesViewModel,
                )
            }
            composable(RootScreenRoute.NutritionBarcode.route) {
                Text(text = "NutritionBarcode")
            }
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
                        profileViewModel.onEvent(ProfileEvent.StopLocationMonitoring)
                        authViewModel.onEvent(AuthenticationEvent.LogoutButtonClicked)
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

            composable(RootScreenRoute.Search.route){
                appBarViewModel.onEvent(
                    AppBarEvent.TopBarComposableChanged(
                        {
                            TopBar(
                                title = {
                                    Text("Search Trainer")
                                },
                            )
                        }
                    )
                )
                //Text(text = "Search")
                SearchTrainerScreen( //fixme: TrainersVM needed
                    navController = navController,
                    trainersViewModel = trainersViewModel,
                    //mainViewModel = mainViewModel, //With MainVM
                ) { userId ->
                    navController.navigate(RootScreenRoute.SearchDetail.createRoute(userId))
                }
            }

            composable(RootScreenRoute.SearchDetail.route) { entry ->
                val id = entry.arguments?.getString("id")
                val trainer = trainersSate.trainers.find { it?.userId == id } //With MainVM
                appBarViewModel.onEvent(
                    AppBarEvent.TopBarComposableChanged(
                        {
                            BackNavWithTitle("${trainer?.firstName} ${trainer?.lastName}") {
                                navController.popBackStack()
                            }
                        }
                    )
                )
                //Text(text = "SearchDetail: ${trainer}")
                TrainerDetailsScreen(
                    data = trainer!!,
                    trainersViewModel = trainersViewModel,
                ) // FIXME: !! is not safe
            }

            composable(RootScreenRoute.Trainer.route){
                appBarViewModel.onEvent(
                    AppBarEvent.TopBarComposableChanged(
                        {
                            TopBar(
                                title = {
                                    Text("Trainer(s)")
                                },
                            )
                        }
                    )
                )
                TrainerScreen()
            }

            composable (RootScreenRoute.TrainerDetail.route) { entry ->
                appBarViewModel.onEvent(
                    AppBarEvent.TopBarComposableChanged(
                        {
                            BackNavWithTitle("Trainer Details") {
                                navController.popBackStack()
                            }
                        }
                    )
                )
                val id = entry.arguments?.getString("id")
                Text(text = "TrainerDetail: $id")
            }


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
                /*appBarViewModel.onEvent(
                    AppBarEvent.TopBarComposableChanged(
                        {
                            BackNavWithTitle("Trainee Details") {
                                navController.popBackStack()
                            }
                        }
                    )
                )*/
                val id = entry.arguments?.getString("id")
                //Text(text = "TraineeDetail: ${
                //    traineesViewModel.state.collectAsStateWithLifecycle().value.acceptedRequests.find { it.second.userId == id }
                //}")
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
                /*Text(
                    text = "RequestDetail: $id",
                    modifier = Modifier
                        .clickable{
                            traineesViewModel.onEvent(
                                TraineesEvent.AcceptRequest(
                                    userId = id!!
                                )
                            )
                            navController.popBackStack()
                        }
                )*/
            }


            composable(RootScreenRoute.Menu.route){
                Text(text = "Menu")
            }
        }
    }
}