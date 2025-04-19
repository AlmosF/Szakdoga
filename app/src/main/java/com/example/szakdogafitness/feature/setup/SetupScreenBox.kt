package com.example.szakdogafitness.feature.setup

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.szakdogafitness.presentation.navigation.setup.SetupNavGraph
import com.example.szakdogafitness.ui.common.ProgressBar
import com.example.szakdogafitness.viewmodel.SetupEvent
import com.example.szakdogafitness.viewmodel.SetupViewModel


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreenBox(
    paddingValues: PaddingValues = PaddingValues(0.dp),
    //profileViewModel: ProfileViewModel = hiltViewModel(),
    setupViewModel: SetupViewModel = hiltViewModel(),
    onMainNavigate: () -> Unit
) {
    //val navHostController = rememberNavController()
    //val setupViewModel: SetupViewModel = viewModel() //Needed while it was called only from the mainscreen
    val state = setupViewModel.state.collectAsState()


    //val profileState = profileViewModel.state.collectAsStateWithLifecycle()

    val previousButtonVisible = setupViewModel.isPreviousButtonVisible.collectAsState()
    val nextButtonEnabled = setupViewModel.isNextButtonEnabled.collectAsState()
    val nextButtonLabel = setupViewModel.nextButtonLabel.collectAsState()
    val navHostController = rememberNavController()
    setupViewModel.setNavController(navHostController)

    var isLoading by remember { mutableStateOf(false) }

    //LaunchedEffect(Unit, profileState, profileState.value.user?.isSetupDone) {
    //    Log.d("SetupScreenBoxLE", "SetupScreenBox: ${profileState.value.user}")
    //    profileViewModel.onEvent(ProfileEvent.GetUser)
    //}

    /*LaunchedEffect(Unit) {
        isLoading = true
        delay(1000)
        isLoading = false
    }*/

    LaunchedEffect(
        state.value.currentScreen
    ) {
        Log.d("SetupScreenBoxLE", "SetupScreenBox: ${setupViewModel.state.value}")
    }

    var finishClicked by remember { mutableStateOf(false) }
    LaunchedEffect(finishClicked, state.value.isLoading) {
        if (finishClicked && !state.value.isLoading) {
            onMainNavigate()
        }
    }

    /*LaunchedEffect(profileState.value.user.isSetupDone == true, Unit) {
        onMainNavigate()
    }*/

    ConstraintLayout (
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        val (progressBar, setupScreen, buttons, div1) = createRefs()
        val startGuideline5 = createGuidelineFromStart(0.05f)
        val endGuideline95 = createGuidelineFromStart(0.95f)
        val startGuideline25 = createGuidelineFromStart(0.25f)
        val endGuideline75 = createGuidelineFromStart(0.75f)

        val startTopGuideline05 = createGuidelineFromTop(0.01f)
        val startTopGuideline10 = createGuidelineFromTop(0.10f)
        val endTopGuideline90 = createGuidelineFromTop(0.90f)
        val endTopGuideline95 = createGuidelineFromTop(0.95f)

        HorizontalDivider(
            modifier = Modifier
                .constrainAs(div1) {
                    top.linkTo(endTopGuideline90)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            color = Color.Black,
            thickness = 1.dp
        )
        //if (!profileState.value.user.isSetupDone) { // Only for DEBUG
        if (!isLoading) { // Only for DEBUG
            Column(
                modifier = Modifier
                    .constrainAs(progressBar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth(0.9f),
                verticalArrangement = Arrangement.Center,
            ) {
                ProgressBar(
                    progress = state.value.barStatus.toFloat(),
                )
                Text(
                    text = "Who are you?",
                    fontSize = 24.sp,
                    modifier = Modifier,
                    fontWeight = Bold
                )

            }

            /*SetupScreen(
            modifier = Modifier
                .constrainAs(setupScreen) {
                    top.linkTo(startTopGuideline10)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxSize()
        )*/
            SetupNavGraph(
                modifier = Modifier
                    .constrainAs(setupScreen) {
                        top.linkTo(startTopGuideline10)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                navController = navHostController,
                setupViewModel = setupViewModel
            )
            /*TrainerOne(
            paddingValues = paddingValues,
            modifier = Modifier
                .constrainAs(setupScreen) {
                    top.linkTo(startTopGuideline10)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxSize()
        )*/

            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp)
                    .constrainAs(buttons) {
                        top.linkTo(endTopGuideline90)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .animateContentSize(),
                horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally)
            ) {
                AnimatedVisibility(
                    modifier = Modifier
                        .weight(1f),
                    visible = previousButtonVisible.value//state.value.currentScreen != 0*/,
                ) {
                    OutlinedButton(
                        onClick = {
                            Log.d("SetupScreenBox", "SetupScreenBox: Back")
                            Log.d("SetupScreenBox", "SetupScreenBox: ${setupViewModel.state.value}")
                            //setupViewModel.onEvent(SetupEvent.BackButtonClicked)
                            //setupViewModel.onEvent(SetupEvent.CurrentScreenChanged(state.value.currentScreen - 1))
                            //navHostController.popBackStack()
                            setupViewModel.onEvent(SetupEvent.BackButtonClicked)
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(
                            text = "Back",
                            modifier = Modifier.padding(8.dp),
                            fontSize = 20.sp
                        )
                    }
                }
                /*if (state.value.currentScreen != 0){
                OutlinedButton(
                    onClick = {
                        Log.d("SetupScreenBox", "SetupScreenBox: Back")
                        Log.d("SetupScreenBox", "SetupScreenBox: ${setupViewModel.state.value}")
                        //setupViewModel.onEvent(SetupEvent.BackButtonClicked)
                        //setupViewModel.onEvent(SetupEvent.CurrentScreenChanged(state.value.currentScreen - 1))
                        //navHostController.popBackStack()
                        setupViewModel.onEvent(SetupEvent.BackButtonClicked)
                    },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Back",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 20.sp
                    )
                }
            }*/
                OutlinedButton(
                    onClick = {
                        Log.d("SetupScreenBox", "SetupScreenBox: Next")
                        Log.d("SetupScreenBox", "SetupScreenBox: ${setupViewModel.state.value}")
                        //setupViewModel.onEvent(SetupEvent.NextButtonClicked)
                        when (nextButtonLabel.value) {
                            "Finish" -> {
                                setupViewModel.onEvent(SetupEvent.FinishButtonClicked)
                                //onMainNavigate()
                                finishClicked = true
                            }

                            "Next" -> {
                                setupViewModel.onEvent(SetupEvent.NextButtonClicked)
                            }
                        }
                        /*when (setupViewModel.state.value.currentScreen){
                        0 -> {
                            setupViewModel.onEvent(SetupEvent.CurrentScreenChanged(state.value.currentScreen + 1))
                            when (setupViewModel.state.value.role) {
                                1 -> navHostController.navigate(SetupScreenRoute.TrainerOne.route)
                                2 -> navHostController.navigate(SetupScreenRoute.TraineeOne.route)
                            }
                        }
                        1 -> {
                            setupViewModel.onEvent(SetupEvent.CurrentScreenChanged(state.value.currentScreen + 1))
                            when (setupViewModel.state.value.role) {
                                1 -> navHostController.navigate(SetupScreenRoute.TrainerTwo.route)
                                2 -> navHostController.navigate(SetupScreenRoute.TraineeTwo.route)
                            }
                        }
                        2 -> {
                            setupViewModel.onEvent(SetupEvent.CurrentScreenChanged(state.value.currentScreen + 1))
                            //FINISH
                        }
                    }*/
                    },
                    enabled = nextButtonEnabled.value,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = nextButtonLabel.value,
                        modifier = Modifier.padding(8.dp),
                        fontSize = 20.sp
                    )
                }

            }
        }else {
            CircularProgressIndicator(
                color = Color.Green
            )
            /*Column (
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(setupScreen){
                        top.linkTo(startTopGuideline10)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Text(
                    text = "Setup is done",
                )
                Text(
                    text = profileState.value.user.firstName
                )
                Button(
                    onClick = {onProfileNavigate()}
                ) {
                    Text("Go to profile")
                }
            }*/
        }

    }
}
