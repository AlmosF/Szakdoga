package com.example.szakdogafitness.feature.trainee

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.szakdogafitness.viewmodel.TraineesViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.key.Key.Companion.Ro
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.ui.common.lifestyle.DailyCaloriesBlock
import com.example.szakdogafitness.ui.common.lifestyle.StepsBlock
import com.example.szakdogafitness.ui.common.lifestyle.WaterIntakeBlock
import com.example.szakdogafitness.ui.common.lifestyle.WeightControlBlock
import com.example.szakdogafitness.viewmodel.MealTypesViewModel
import com.example.szakdogafitness.viewmodel.TraineesEvent

@Composable
fun TraineeLifestyleScreen(
    modifier: Modifier = Modifier,
    traineesViewModel: TraineesViewModel = hiltViewModel(),
    mealTypesViewModel: MealTypesViewModel = hiltViewModel(),
    data: User
) {

    val traineesState by traineesViewModel.state.collectAsStateWithLifecycle()
    val mealTypesState by mealTypesViewModel.state.collectAsStateWithLifecycle()

    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row (
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    traineesViewModel.onEvent(TraineesEvent.PreviousDailyDataDay)
                },
                enabled = traineesState.isPreviousDayAvailable
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Previous Day",
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(30.dp)
                )
            }
            Text(
                text = traineesState.lifeStyleTitle,
                modifier = modifier
                    .padding(horizontal = 16.dp),
                fontSize = 20.sp
            )
            IconButton(
                onClick = {
                    traineesViewModel.onEvent(TraineesEvent.NextDailyDataDay)
                },
                enabled = traineesState.isNextDayAvailable
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Next Day",
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(30.dp)
                )
            }
        }
        Column (
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            DailyCaloriesBlock(
                dailyData = traineesState.selectedTraineesDailyData ?: DailyData(),
                nutritions = mealTypesState.nutritions
            )
            WaterIntakeBlock(
                consumedWater = traineesState.selectedTraineesDailyData?.waterIntake ?: 0.0,
                goalWater = traineesState.selectedTraineesDailyData?.targetWater ?: 0.0,
                personal = false,
                onModifyIntake = {},
                onModifyTarget = {}
            )
            WeightControlBlock(
                weight = data.trainee.weight,
                personal = false
            )
            StepsBlock(
                steps = traineesState.selectedTraineesDailyData?.steps ?: 0,
                goalSteps = traineesState.selectedTraineesDailyData?.targetSteps ?: 0,
            )
        }
    }

}