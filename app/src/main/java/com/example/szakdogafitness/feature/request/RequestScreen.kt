package com.example.szakdogafitness.feature.request

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.szakdogafitness.domain.model.Trainee
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.ui.common.NormalTextField
import com.example.szakdogafitness.ui.common.appbars.TopBar
import com.example.szakdogafitness.ui.common.request.TraineeRequestCard
import com.example.szakdogafitness.presentation.navigation.AppBarEvent
import com.example.szakdogafitness.presentation.navigation.AppBarViewModel
import com.example.szakdogafitness.feature.trainee.TraineesEvent
import com.example.szakdogafitness.feature.trainee.TraineesViewModel
import com.google.firebase.Timestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestScreen(
    modifier: Modifier = Modifier,
    traineesViewModel: TraineesViewModel = hiltViewModel(),
    appBarViewModel: AppBarViewModel = hiltViewModel(),
    onClick: (String) -> Unit = {}
) {

    val traineesState by traineesViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        traineesViewModel.onEvent(TraineesEvent.LoadPendingRequests)
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
    }
    LaunchedEffect(traineesState.pendingRequests) {
        Log.d("RequestScreen", "PendingRequests: ${traineesState.pendingRequests}")
    }

    var search by remember { mutableStateOf("") }
    Column (
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NormalTextField(
            modifier = Modifier
                .fillMaxWidth(0.95f),
            value = search,
            onValueChange = {
                search = it
            },
            onDone = {},
            label = "Search",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    modifier = Modifier
                        .size(32.dp)
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.FilterAlt,
                    contentDescription = "Filter",
                    modifier = Modifier
                        .size(32.dp)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(32.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            )
        )
        if (traineesState.isLoading){
            Column (
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize(
                            0.95f
                        )
                )
            }
        } else {
            if (traineesState.pendingRequests.isEmpty()){
                Column (
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No pending requests",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                val filtered = traineesState.pendingRequests.filter {
                    (it.second.firstName+ " " + it.second.lastName).contains(search, ignoreCase = true)
                }
                LazyColumn (
                    modifier = Modifier
                        .fillMaxWidth(0.95f),
                ) {
                    items(filtered.size) {
                        TraineeRequestCard(
                            data = filtered[it].second,
                        ) {
                            val request = filtered[it]
                            onClick(
                                request.first.traineeId
                            )
                        }
                    }
                }
            }
        }
    }
}
