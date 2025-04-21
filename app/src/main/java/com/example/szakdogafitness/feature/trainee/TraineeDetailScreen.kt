package com.example.szakdogafitness.feature.trainee

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.szakdogafitness.ui.common.appbars.BackNavWithTitle
import com.example.szakdogafitness.presentation.navigation.AppBarEvent
import com.example.szakdogafitness.presentation.navigation.AppBarViewModel
import com.example.szakdogafitness.feature.nutrition.MealTypesViewModel
import kotlinx.coroutines.delay

@Composable
fun TraineeDetailScreen(
    modifier: Modifier = Modifier,
    traineesViewModel: TraineesViewModel = hiltViewModel(),
    appBarViewModel: AppBarViewModel = hiltViewModel(),
    mealTypesViewModel: MealTypesViewModel = hiltViewModel(),
    id: String,
    onNavigateBack: () -> Unit
){
    val traineesState by traineesViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        traineesViewModel.onEvent(TraineesEvent.FetchTraineesDailyData(
            traineesState.selectedTrainee?.second?.userId ?: error("Selected trainee is null")
        ))
        delay(100) //fixme: WITHOUT DELAY
        appBarViewModel.onEvent(
            AppBarEvent.TopBarComposableChanged(
                {
                    BackNavWithTitle(
                        title = traineesState.selectedTrainee?.second?.firstName + " " + traineesState.selectedTrainee?.second?.lastName,
                    ) {
                        onNavigateBack()
                        traineesViewModel.onEvent(TraineesEvent.ClearSelectedTrainee)
                        traineesViewModel.onEvent(TraineesEvent.DetailModeChanged(false))
                    }
                }
            )
        )
    }

    LaunchedEffect(traineesState.acceptedRequests) {
        Log.d("TraineeDetailScreen", "AcceptedRequests: ${traineesState.acceptedRequests}")
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SingleChoiceSegmentedButtonRow {
            SegmentedButton(
                selected = !traineesState.detailMode,
                onClick = { traineesViewModel.onEvent(TraineesEvent.DetailModeChanged(false)) },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                icon = {}
            ) {
                Text(text = "Lifestyle")
            }
            SegmentedButton(
                selected = traineesState.detailMode,
                onClick = { traineesViewModel.onEvent(TraineesEvent.DetailModeChanged(true)) },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                icon = {}
            ) {
                Text(text = "Bio")
            }
        }
        if (!traineesState.isLoading){
            Box(modifier = Modifier.fillMaxSize()) {
                if (!traineesState.detailMode) {
                    TraineeLifestyleScreen(
                        traineesViewModel = traineesViewModel,
                        mealTypesViewModel = mealTypesViewModel,
                        data = traineesState.selectedTrainee?.second ?: error("Selected trainee is null"),
                    )
                } else {
                    /*TraineeBioScreen(
                        data = traineesState.selectedTrainee?.second ?: error("Selected trainee is null")
                    ) {
                        traineesViewModel.onEvent(TraineesEvent.RemoveTrainee(it))
                        onNavigateBack()
                    }*/
                    TraineeBioScreen(
                        data = traineesState.selectedTrainee ?: error("Selected trainee is null")
                    ) {
                        traineesViewModel.onEvent(TraineesEvent.RemoveTrainee(it))
                        onNavigateBack()
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()){
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(250.dp)
                )
            }
        }
    }
}