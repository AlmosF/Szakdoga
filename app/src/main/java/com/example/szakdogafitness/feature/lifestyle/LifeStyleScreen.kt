package com.example.szakdogafitness.feature.lifestyle

import android.R.attr.end
import android.R.attr.top
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontVariation.weight
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.example.szakdogafitness.viewmodel.AuthenticationEvent
import com.example.szakdogafitness.viewmodel.MainEvent
import com.example.szakdogafitness.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifeStyleScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel(),
    appBarViewModel: AppBarViewModel = hiltViewModel()
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

    val mainState by mainViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
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
    }


    ConstraintLayout (
        modifier = modifier//.padding(innerPadding)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){
        val (caloryBlock, waterBlock, weightBlock, stepsBlock) = createRefs()

        DailyCaloriesBlock(
            modifier = Modifier
                .constrainAs(caloryBlock){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(0.9f),
            dailyData = mainState.currentDailyData
        )
        WaterIntakeBlock(
            modifier = Modifier
                .constrainAs(waterBlock){
                    top.linkTo(caloryBlock.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(0.9f),
            consumedWater = mainState.currentDailyData.waterIntake,
            goalWater = mainState.currentDailyData.targetWater
        )
        WeightControlBlock(
            modifier = Modifier
                .constrainAs(weightBlock){
                    top.linkTo(waterBlock.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(0.9f),
            weight = mainState.user?.trainee?.weight ?: 0.0,
        )
        StepsBlock(
            modifier = Modifier
                .constrainAs(stepsBlock){
                    top.linkTo(weightBlock.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(0.9f),
            steps = 1234,
            goalSteps = 6000
        )


    }
    //}
}

@Composable
@Preview
fun LifeStyleScreenPreview(){
    LifeStyleScreen()
}
