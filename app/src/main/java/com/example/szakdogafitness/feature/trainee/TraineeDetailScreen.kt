package com.example.szakdogafitness.feature.trainee

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.szakdogafitness.viewmodel.TraineesEvent
import com.example.szakdogafitness.viewmodel.TraineesViewModel

@Composable
fun TraineeDetailScreen(
    modifier: Modifier = Modifier,
    traineesViewModel: TraineesViewModel = hiltViewModel(),
    id: String
){
    val traineesState by traineesViewModel.state.collectAsStateWithLifecycle()
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
        Box(modifier = Modifier.fillMaxSize()) {
            if (!traineesState.detailMode) {
                Text(
                    text = traineesState.acceptedRequests.find {
                        it.second.userId == id
                    }?.first.toString()
                )
            } else {
                TraineeBioScreen(
                    data = traineesState.acceptedRequests.find {
                        it.second.userId == id
                    }?.second ?: error("asd")
                )
            }
        }
    }
}