package com.example.szakdogafitness.presentation.navigation.root

import android.net.http.SslCertificate.restoreState
import android.net.http.SslCertificate.saveState
import android.nfc.Tag
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.example.szakdogafitness.feature.auth.AuthenticateScreen
import com.example.szakdogafitness.feature.lifestyle.LifeStyleScreen
import com.example.szakdogafitness.feature.nutrition.NutritionScreen
import com.example.szakdogafitness.feature.nutrition.SearchNutritionScreen
import com.example.szakdogafitness.feature.request.RequestScreen
import com.example.szakdogafitness.feature.setup.SetupScreenBox
import com.example.szakdogafitness.feature.trainee.TraineesScreen
import com.example.szakdogafitness.feature.search.SearchTrainerScreen
import com.example.szakdogafitness.feature.trainer.TrainerScreen
import com.example.szakdogafitness.presentation.navigation.main.items.BottomNavigationItems
import com.example.szakdogafitness.ui.common.appbars.LifeStyleAction
import com.example.szakdogafitness.ui.common.appbars.LifeStyleTitle
import com.example.szakdogafitness.ui.common.appbars.TopBar
import com.example.szakdogafitness.util.UiEvent
import com.example.szakdogafitness.viewmodel.AppBarEvent
import com.example.szakdogafitness.viewmodel.AppBarViewModel
import com.example.szakdogafitness.viewmodel.AuthenticationEvent
import com.example.szakdogafitness.viewmodel.AuthenticationViewModel
import com.example.szakdogafitness.viewmodel.MainEvent
import com.example.szakdogafitness.viewmodel.MainViewModel
import com.example.szakdogafitness.viewmodel.ProfileEvent
//import com.example.szakdogafitness.viewmodel.ProfileViewModel
import com.example.szakdogafitness.viewmodel.RootNavEvent
import com.example.szakdogafitness.viewmodel.RootNavViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthenticationViewModel = viewModel(),
    //profileViewModel: ProfileViewModel = hiltViewModel(),
    rootNavViewModel: RootNavViewModel = hiltViewModel(),
    appBarViewModel: AppBarViewModel = hiltViewModel(), //Not fix yet
    mainViewModel: MainViewModel = hiltViewModel() // FIXME: After logging out, the VM is keeping it's state, and wont reINIT. Signout -> Sign in = Old profile's state
){

    object {
        private val TAG = "RootNavigation"
    }

    val state by authViewModel.state.collectAsStateWithLifecycle()

    //val profileState by profileViewModel.state.collectAsStateWithLifecycle()

    val appBarState by appBarViewModel.state.collectAsStateWithLifecycle()

    val mainState by mainViewModel.state.collectAsStateWithLifecycle()


    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()



    LaunchedEffect(Unit, navBackStackEntry) { //NavHostController initialization
        rootNavViewModel.onEvent(RootNavEvent.NavController(navController))
        appBarViewModel.onEvent(AppBarEvent.CurrentNavControllerChanged(navController))
        Log.d("RootNavigation-LE", "NavController Changed: ${navBackStackEntry?.destination?.route}")
    }

    LaunchedEffect(state.isLoggedIn, mainState.user?.isSetupDone, mainState.user) {
        when(state.isLoggedIn){ //Change it to If
            true -> {
                //profileViewModel.onEvent(ProfileEvent.GetUser)
                Log.d("RootNavigation", "Fetching User")
                mainViewModel.onEvent(MainEvent.FetchUser) //fixme: If this is here instead of the above one it navigates to the setup nmw - Fixed (?)
                mainViewModel.onEvent(MainEvent.FetchDailyData)
                mainViewModel.onEvent(MainEvent.GenerateNewDailyData)
            }
            false -> {
                Log.d("RootNavigation", "Clearing State")
                mainViewModel.onEvent(MainEvent.ClearState)
                Log.d("RootNavigation", "Clearing User Done: ${mainState.user}")
            }
        }
    }

    /*LaunchedEffect(state.isLoggedIn) {
        mainViewModel.onEvent(MainEvent.FetchUser) //Fetch User
    }*/

    /*LaunchedEffect(state.isLoggedIn, mainState.user?.isSetupDone == true, mainState.user/*, mainState.user != null*/) {
        if (state.isLoggedIn){
            mainViewModel.onEvent(MainEvent.FetchDailyData)
            mainViewModel.onEvent(MainEvent.GenerateNewDailyData)
        }
    }*/


    /*LaunchedEffect(state.isLoggedIn) {
        when(state.isLoggedIn){
            true -> {
                navController.navigate(RootScreenRoute.MainScreen.route) {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }
            false -> {
                navController.navigate(RootScreenRoute.Authentication.route) {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }
        }
    }*/


    /*LaunchedEffect(state.isLoggedIn == true, profileState.user?.isSetupDone) {

        Log.d("RootNavigation-LE", "isLoggedIn: ${state.isLoggedIn}")
        Log.d("RootNavigation-LE", "isSetupDone: ${profileState.user?.isSetupDone}")
        Log.d("RootNavigation-LE", "state: ${profileState.user}")
        rootNavViewModel.onEvent(
            RootNavEvent.Navigate(
                isLoggedIn = state.isLoggedIn,
                isSetupDone = profileState.user?.isSetupDone == true,
                isProfileNull = profileState.user == null
            )
        )
        /*when(state.isLoggedIn){
            true -> {
                if (profileState.user?.isSetupDone == true && profileState.user?.isSetupDone != null){
                    navController.navigate(RootScreenRoute.MainScreen.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                } else if (profileState.user?.isSetupDone == false || profileState.user?.isSetupDone == null) {

                    navController.navigate(RootScreenRoute.SetupScreen.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            }
            false -> {
                navController.navigate(RootScreenRoute.Authentication.route) {
                    popUpTo(0) {
                        inclusive = true
                    }
                }
            }
        }*/
    }*/
    /*LaunchedEffect(state.isLoggedIn == true, mainState.user?.isSetupDone) {

        Log.d("RootNavigation-LE", "isLoggedIn: ${state.isLoggedIn}")
        Log.d("RootNavigation-LE", "isSetupDone: ${mainState.user?.isSetupDone}")
        Log.d("RootNavigation-LE", "state: ${mainState.user}")
        Log.d("RootNavigation-LE", "loading: ${mainState.isLoading}")
        if (!mainState.isLoading){
            rootNavViewModel.onEvent(
                RootNavEvent.Navigate(
                    isLoggedIn = state.isLoggedIn,
                    isSetupDone = mainState.user?.isSetupDone == true,
                    isProfileNull = mainState.user == null
                )
            )
        } //Bit buggy
    }*/

    LaunchedEffect(state.isLoggedIn, mainState.user, mainState.isLoading) {
        Log.d("RootNavigation-LE", "isLoggedIn: ${state.isLoggedIn}")
        Log.d("RootNavigation-LE", "isSetupDone: ${mainState.user?.isSetupDone}")
        Log.d("RootNavigation-LE", "state: ${mainState.user}")
        //Log.d("RootNavigation-LE", "loading: ${mainState.isLoading}")
        if (state.isLoggedIn == false) {
            rootNavViewModel.onEvent(
                RootNavEvent.Navigate(isLoggedIn = false, isSetupDone = false, isProfileNull = true)
            )
        } else {
            if (mainState.isLoading == false) {
                if (mainState.user?.isSetupDone == true) {
                    rootNavViewModel.onEvent(
                        RootNavEvent.Navigate(isLoggedIn = true, isSetupDone = true, isProfileNull = false)
                    )
                } else {
                    delay(300)
                    rootNavViewModel.onEvent(
                        RootNavEvent.Navigate(isLoggedIn = true, isSetupDone = false, isProfileNull = false)
                    )
                }
            }
        }
    }


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

    LaunchedEffect(navBackStackEntry?.destination?.route) { //AppBarVisibility - Settings
        appBarViewModel.onEvent(AppBarEvent.AppBarVisibility(navBackStackEntry?.destination?.route ?: ""))
    }


    Scaffold (
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier,
        topBar = {
                /*AnimatedVisibility(visible = appBarState.topBarVisibility) {
                    appBarState.topBarComposable.invoke()
                }*/
            if (appBarState.topBarVisibility) appBarState.topBarComposable.invoke()
                 },
        bottomBar = {
            if (appBarState.bottomBarVisibility/*state.isLoggedIn && profileState.user != null*/){
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    val items = when (mainState.user?.role) {
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
        if (mainState.isLoading){
            Text(text = "Loading...")
        }else{
            NavHost(
                modifier = modifier.padding(innerPadding),
                startDestination = RootScreenRoute.Authentication.route,
                navController = navController,
                enterTransition = {
                    fadeIn(animationSpec = tween(500))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(100))
                },
                popEnterTransition = {
                    fadeIn(animationSpec = tween(500))
                },
                popExitTransition = {
                    fadeOut(animationSpec = tween(100))
                }
            ) {
                composable(RootScreenRoute.Authentication.route) {
                    Log.d("RootNavigation", "Authentication Screen")
                    AuthenticateScreen(
                        viewModel = authViewModel,
                        navController = navController
                    )
                }

                composable(RootScreenRoute.SetupScreen.route){

                    Log.d("RootNavigation", "Setup Screen")
                    SetupScreenBox(){
                        //authViewModel.onEvent(AuthenticationEvent.NavigateToProfile(navGraph = navController))
                    }
                }

                /*composable(RootScreenRoute.MainScreen.route){
                    MainNavigation(
                        rootNavController = navController,
                        snackbarHostState = snackbarHostState,
                        authViewModel = authViewModel,
                        profileViewModel = profileViewModel
                    )
                }*/
                //##
                composable(RootScreenRoute.LifeStyle.route){
                    Log.d("RootNavigation", "LifeStyle Screen")
                    Log.d("RootNavigation--", "LifeStyle - ${mainState.lifeStyleTitle}")
                    Log.d("RootNavigation--", "LifeStyle - ${mainState.isPreviousDayAvailable}")
                    Log.d("RootNavigation--", "LifeStyle - ${mainState.isNextDayAvailable}")
                    /*appBarViewModel.onEvent(
                        AppBarEvent.TopBarComposableChanged(
                            {
                                TopBar(
                                    title = {
                                        LifeStyleTitle(
                                            title = mainState.lifeStyleTitle,
                                            previousButtonEnabled = mainState.isPreviousDayAvailable,
                                            onPreviousDayClick = {
                                                Log.d("RootNavigation", "onPreviousDayClick")
                                                mainViewModel.onEvent(MainEvent.PreviousDayDailyData)
                                                //authViewModel.onEvent(AuthenticationEvent.LogoutButtonClicked)
                                            },
                                            nextButtonEnabled = mainState.isNextDayAvailable,
                                            onNextDayClick = {
                                                Log.d("RootNavigation", "onNextDayClick")
                                                mainViewModel.onEvent(MainEvent.NextDayDailyData)
                                            }
                                        )
                                    },
                                )
                            }
                        )
                    )*/
                    LifeStyleScreen(
                        modifier = Modifier
                            .clickable{
                                authViewModel.onEvent(AuthenticationEvent.LogoutButtonClicked)
                            },
                        mainViewModel = mainViewModel,
                        appBarViewModel = appBarViewModel,
                        navController = navController
                    )
                }

                composable(RootScreenRoute.Nutrition.route){
                    appBarViewModel.onEvent(
                        AppBarEvent.TopBarComposableChanged(
                            {
                                TopBar(
                                    title = {
                                        LifeStyleTitle(
                                            title = mainState.lifeStyleTitle,
                                            previousButtonEnabled = mainState.isPreviousDayAvailable,
                                            onPreviousDayClick = {
                                                Log.d("RootNavigation", "onPreviousDayClick")
                                                mainViewModel.onEvent(MainEvent.PreviousDayDailyData)
                                                //authViewModel.onEvent(AuthenticationEvent.LogoutButtonClicked)
                                            },
                                            nextButtonEnabled = mainState.isNextDayAvailable,
                                            onNextDayClick = {
                                                Log.d("RootNavigation", "onNextDayClick")
                                                mainViewModel.onEvent(MainEvent.NextDayDailyData)
                                            }
                                        )
                                    },
                                )
                            }
                        )
                    )
                    NutritionScreen(
                        mainViewModel = mainViewModel,
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
                    Text(text = "NutritionAI")
                }
                composable(RootScreenRoute.NutritionSearch.route) {
                    SearchNutritionScreen()
                }
                composable(RootScreenRoute.NutritionBarcode.route) {
                    Text(text = "NutritionBarcode")
                }


                composable(RootScreenRoute.Profile.route){

                    Text(text = "ProfileG")
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
                    SearchTrainerScreen(
                        navController = navController,
                        mainViewModel = mainViewModel
                    ) {}
                }

                composable(RootScreenRoute.SearchDetail.route) { entry ->
                    val id = entry.arguments?.getString("id")
                    Text(text = "SearchDetail: $id")
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
                    //Text(text = "Trainer")
                    TrainerScreen()
                }

                composable (RootScreenRoute.TrainerDetail.route) { entry ->
                    val id = entry.arguments?.getString("id")
                    Text(text = "TrainerDetail: $id")
                }


                composable(RootScreenRoute.Trainees.route){
                    appBarViewModel.onEvent(
                        AppBarEvent.TopBarComposableChanged(
                            {
                                TopBar(
                                    title = {
                                        Text("Trainee(s)")
                                    },
                                )
                            }
                        )
                    )
                    //Text(text = "Trainees")
                    TraineesScreen()
                }

                composable(RootScreenRoute.TraineeDetail.route) { entry ->
                    val id = entry.arguments?.getString("id")
                    Text(text = "TraineeDetail: $id")
                }


                composable(RootScreenRoute.Requests.route){
                    appBarViewModel.onEvent(
                        AppBarEvent.TopBarComposableChanged(
                            {
                                TopBar(
                                    title = {
                                        Text("Request(s)")
                                    },
                                )
                            }
                        )
                    )
                    //Text(text = "Requests")
                    RequestScreen()
                }

                composable(RootScreenRoute.RequestDetail.route) { entry ->
                    val id = entry.arguments?.getString("id")
                    Text(text = "RequestDetail: $id")
                }


                composable(RootScreenRoute.Menu.route){
                    Text(text = "Menu")
                }
                //###

            }
        }

    }





}