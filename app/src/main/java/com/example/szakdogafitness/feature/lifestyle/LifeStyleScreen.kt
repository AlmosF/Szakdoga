package com.example.szakdogafitness.feature.lifestyle

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.szakdogafitness.presentation.navigation.root.RootScreenRoute
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
import com.example.szakdogafitness.viewmodel.ProfileViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifeStyleScreen(
    modifier: Modifier = Modifier,
    //mainViewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController,
    appBarViewModel: AppBarViewModel = hiltViewModel(),
    dailyDatasViewModel: DailyDatasViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(), //Mimic of the MainVM
    mealTypesViewModel: MealTypesViewModel = hiltViewModel(),
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

    val appBarState by appBarViewModel.state.collectAsStateWithLifecycle()

    //val mainState by mainViewModel.state.collectAsStateWithLifecycle()

    val dailyDatasState by dailyDatasViewModel.state.collectAsStateWithLifecycle()

    val profileState by profileViewModel.state.collectAsStateWithLifecycle()

    val mealTypesState by mealTypesViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        dailyDatasViewModel.onEvent(DailyDatasEvent.LoadDailyDatas)
        mealTypesViewModel.onEvent(MealTypesEvent.FetchNutritions)
        delay(100)
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
        /*WeightControlBlock( //WITH MAINSTATE
            modifier = Modifier
                .constrainAs(weightBlock){
                    top.linkTo(waterBlock.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(0.9f),
            weight = mainState.user?.trainee?.weight ?: 0.0,
        )*/
        WeightControlBlock(
            modifier = Modifier
                .constrainAs(weightBlock){
                    top.linkTo(waterBlock.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(0.9f),
            weight = profileState.user?.trainee?.weight ?: 0.0,
        )
        /*StepsBlock(
            modifier = Modifier
                .constrainAs(stepsBlock){
                    top.linkTo(weightBlock.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(0.9f),
            steps = 1234,
            goalSteps = 6000
        )*/
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
        )


    }
    //}
}

@Composable
@Preview
fun LifeStyleScreenPreview(){
    LifeStyleScreen(navController = rememberNavController())
}
