package com.example.szakdogafitness.feature.lifestyle

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.szakdogafitness.presentation.navigation.root.RootScreenRoute
import com.example.szakdogafitness.presentation.navigation.startStepService
import com.example.szakdogafitness.ui.common.appbars.LifeStyleAction
import com.example.szakdogafitness.ui.common.appbars.LifeStyleTitle
import com.example.szakdogafitness.ui.common.appbars.TopBar
import com.example.szakdogafitness.ui.common.lifestyle.DailyCaloriesBlock
import com.example.szakdogafitness.ui.common.lifestyle.StepsBlock
import com.example.szakdogafitness.ui.common.lifestyle.WaterIntakeBlock
import com.example.szakdogafitness.ui.common.lifestyle.WeightControlBlock
import com.example.szakdogafitness.viewmodel.AppBarEvent
import com.example.szakdogafitness.viewmodel.AppBarViewModel
import com.example.szakdogafitness.viewmodel.DailyDatasEvent
import com.example.szakdogafitness.viewmodel.DailyDatasViewModel
import com.example.szakdogafitness.viewmodel.MealTypesEvent
import com.example.szakdogafitness.viewmodel.MealTypesViewModel
import com.example.szakdogafitness.viewmodel.ProfileEvent
import com.example.szakdogafitness.viewmodel.ProfileViewModel
import com.example.szakdogafitness.viewmodel.StepCountViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun LifeStyleScreen(
    modifier: Modifier = Modifier,
    //mainViewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController,
    appBarViewModel: AppBarViewModel = hiltViewModel(),
    dailyDatasViewModel: DailyDatasViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(), //Mimic of the MainVM
    mealTypesViewModel: MealTypesViewModel = hiltViewModel(),
    stepCountViewModel: StepCountViewModel = hiltViewModel(),
    //VMs
){
    /*Scaffold (
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = {
                    LifeStyleTitle(
                        title = "DAYA",
                        onPreviousDayClick = {
                            Log.d("RootNavigation", "onPreviousDayClick")
                        },
                        onNextDayClick = {

                        }
                    )

                },
                navigationIcon = {
                    LifeStyleAction (
                        image = "https://th.bing.com/th/id/R.714e3da50d7cbbdc01a4cfa6d6db7749?rik=WLlLJErQ5mSgQA&pid=ImgRaw&r=0"
                    ) {

                    }
                },
            )
        }
    ){ innerPadding ->*/

    val context = LocalContext.current

    val stepCountState by stepCountViewModel.steps.collectAsStateWithLifecycle()



    val appBarState by appBarViewModel.state.collectAsStateWithLifecycle()

    //val mainState by mainViewModel.state.collectAsStateWithLifecycle()

    val dailyDatasState by dailyDatasViewModel.state.collectAsStateWithLifecycle()

    val profileState by profileViewModel.state.collectAsStateWithLifecycle()

    val mealTypesState by mealTypesViewModel.state.collectAsStateWithLifecycle()

    val permissionState = rememberPermissionState(android.Manifest.permission.ACTIVITY_RECOGNITION)
    val activityPermissionGranted = permissionState.status.isGranted
    /*LaunchedEffect(Unit, activityPermissionGranted) {
        if (!activityPermissionGranted) {
            permissionState.launchPermissionRequest()
        } else {
            stepCountViewModel.loadSteps()
            context.startStepService()
            stepCountViewModel.syncSteps()
        }
    }*/

    /*LaunchedEffect(Unit) {
        stepCountViewModel.loadSteps()
        context.startStepService()
        stepCountViewModel.syncSteps()
    }*/
    val locationPermission = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val permissionsGranted = locationPermission.allPermissionsGranted
    LaunchedEffect(Unit, permissionsGranted, activityPermissionGranted) {
        // Request both permissions
        if (!permissionState.status.isGranted) {
            permissionState.launchPermissionRequest()
        }
        if (!locationPermission.allPermissionsGranted) {
            locationPermission.launchMultiplePermissionRequest()
        }

        // Handle activity recognition permission
        if (permissionState.status.isGranted) {
            stepCountViewModel.loadSteps()
            context.startStepService()
            stepCountViewModel.syncSteps()
        }

        // Handle location permissions
        if (locationPermission.allPermissionsGranted) {
            profileViewModel.onEvent(ProfileEvent.StartLocationMonitoring)
        }
    }
    /*LaunchedEffect(Unit, permissionsGranted) {
        if (permissionsGranted){
            //dailyDatasViewModel.onEvent(DailyDatasEvent.StartLocationMonitoring)
            profileViewModel.onEvent(ProfileEvent.StartLocationMonitoring)
        } else {
            locationPermission.launchMultiplePermissionRequest()
        }
    }*/

    LaunchedEffect(Unit) {
        dailyDatasViewModel.onEvent(DailyDatasEvent.LoadDailyDatas)
        mealTypesViewModel.onEvent(MealTypesEvent.FetchNutritions)
        delay(100) //fixme: Remove delay - somehow

        //Log.d("LifeStyleScreen", "LaunchedEffect: ${mainState.currentDailyData}")
        /*appBarViewModel.onEvent( //WITH MAINSTATE
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
                        navigationIcon = {
                            LifeStyleAction (
                                image = mainState.user?.profileImageUrl ?: "",
                            ) {
                                navController.navigate(RootScreenRoute.Profile.route)
                            }
                        }
                    )
                }
            )
        )*/
        appBarViewModel.onEvent(
            AppBarEvent.TopBarComposableChanged(
                {
                    TopBar(
                        title = {
                            LifeStyleTitle(
                                title = dailyDatasState.lifeStyleTitle,
                                previousButtonEnabled = dailyDatasState.isPreviousDayAvailable,
                                onPreviousDayClick = {
                                    dailyDatasViewModel.onEvent(DailyDatasEvent.PreviousDay)
                                },
                                nextButtonEnabled = dailyDatasState.isNextDayAvailable,
                                onNextDayClick = {
                                    dailyDatasViewModel.onEvent(DailyDatasEvent.NextDay)
                                }
                            )
                        },
                        /*navigationIcon = { //WITH MAINSTATE
                            LifeStyleAction (
                                image = mainState.user?.profileImageUrl ?: "",
                            ) {
                                navController.navigate(RootScreenRoute.Profile.route)
                            }
                        }*/
                        navigationIcon = {
                            LifeStyleAction (
                                image = profileState.user?.profileImageUrl ?: "",
                            ) {
                                navController.navigate(RootScreenRoute.Profile.route)
                            }
                        }
                    )
                }
            )
        )
        //Log.d("LifeStyle", "${mainState.user}")
    }


    ConstraintLayout (
        modifier = modifier//.padding(innerPadding)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){
        val (caloryBlock, waterBlock, weightBlock, stepsBlock) = createRefs()

        /*DailyCaloriesBlock( //WITH MAINSTATE
            modifier = Modifier
                .constrainAs(caloryBlock){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(0.9f),
            dailyData = mainState.currentDailyData
        )*/
        DailyCaloriesBlock(
            modifier = Modifier
                .constrainAs(caloryBlock){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(0.9f),
            dailyData = dailyDatasState.currentDailyData,
            nutritions = mealTypesState.nutritions
        )
        /*WaterIntakeBlock( //WITH MAINSTATE
            modifier = Modifier
                .constrainAs(waterBlock){
                    top.linkTo(caloryBlock.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(0.9f),
            consumedWater = mainState.currentDailyData.waterIntake,
            goalWater = mainState.currentDailyData.targetWater
        )*/
        WaterIntakeBlock(
            modifier = Modifier
                .constrainAs(waterBlock){
                    top.linkTo(caloryBlock.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(0.9f),
            consumedWater = dailyDatasState.currentDailyData.waterIntake,
            goalWater = dailyDatasState.currentDailyData.targetWater,
            onModifyTarget = {
                dailyDatasViewModel.onEvent(
                    DailyDatasEvent.UpdateTargetWater(
                        dailyDatasState.currentDailyData.targetWater + it,
                        dailyDatasState.currentDailyData.date
                    )
                )
            },
            onModifyIntake = {
                dailyDatasViewModel.onEvent(
                    DailyDatasEvent.UpdateWaterIntake(
                        dailyDatasState.currentDailyData.waterIntake + it,
                        dailyDatasState.currentDailyData.date
                    )
                )
            }
        )
        Row (
            modifier = Modifier
                .constrainAs(weightBlock) {
                    top.linkTo(waterBlock.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(0.9f)
                .height(IntrinsicSize.Min),
        ) {
            if (profileState.user?.role == 2){
                WeightControlBlock(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f)
                        .fillMaxHeight(),
                    weight = profileState.user?.trainee?.weight ?: 0.0,
                    unit = profileState.user?.trainee?.unit ?: 1,
                )
            }
            StepsBlock(
                modifier = Modifier
                    .padding(start = if(profileState.user?.role == 2) 8.dp else 0.dp)
                    .weight(1f)
                    .fillMaxHeight(),
                steps = dailyDatasState.currentDailyData.steps,
                goalSteps = dailyDatasState.currentDailyData.targetSteps
            )
        }
        /*WeightControlBlock(
            modifier = Modifier
                .constrainAs(weightBlock){
                    top.linkTo(waterBlock.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(0.9f),
            weight = profileState.user?.trainee?.weight ?: 0.0,
        )
        StepsBlock(
            modifier = Modifier
                .constrainAs(stepsBlock){
                    top.linkTo(weightBlock.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(0.9f),
            steps = dailyDatasState.currentDailyData.steps,
            goalSteps = dailyDatasState.currentDailyData.targetSteps
        )*/


    }
    //}
}

@Composable
@Preview
fun LifeStyleScreenPreview(){
    LifeStyleScreen(navController = rememberNavController())
}
