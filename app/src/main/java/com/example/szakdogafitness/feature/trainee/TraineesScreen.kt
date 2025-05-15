package com.example.szakdogafitness.feature.trainee

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
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.ui.common.NormalTextField
import com.example.szakdogafitness.ui.common.appbars.TopBar
import com.example.szakdogafitness.ui.common.trainee.TraineeCard
import com.example.szakdogafitness.presentation.navigation.AppBarEvent
import com.example.szakdogafitness.presentation.navigation.AppBarViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TraineesScreen(
    modifier: Modifier = Modifier,
    traineesViewModel: TraineesViewModel = hiltViewModel(),
    appBarViewModel: AppBarViewModel = hiltViewModel(),
    onClick: (String) -> Unit = {}
) {

    LaunchedEffect(Unit) {
        delay(50)
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
        traineesViewModel.onEvent(TraineesEvent.LoadAcceptedRequests)
    }

    val traineesState by traineesViewModel.state.collectAsStateWithLifecycle()

    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var search by remember { mutableStateOf("") }
        NormalTextField(
            modifier = Modifier
                .fillMaxWidth(0.95f),
            value = search,
            onValueChange = {
                search = it
            },
            onDone = {},
            label = "Search",
            singleLine = true,
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
            shape = RoundedCornerShape(32.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            )
        )

        if(traineesState.isLoading){
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
            if (traineesState.acceptedRequests.isEmpty()){
                Column (
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                ) {
                    Text("No accepted trainees yet.")
                }
            } else {
                val filtered = traineesState.acceptedRequests.filter {
                    (it.second.firstName+" "+it.second.lastName).contains(search, ignoreCase = true)
                }
                LazyColumn (
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                ) {
                    items(filtered.size) {
                        TraineeCard(
                            data = filtered[it].second,
                            onCardClick = {
                                onClick(filtered[it].second.userId)
                            },
                            onMessageClick = {}
                        )
                    }
                }
            }
        }

    }
}