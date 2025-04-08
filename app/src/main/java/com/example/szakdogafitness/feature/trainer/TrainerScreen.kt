package com.example.szakdogafitness.feature.trainer

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
import androidx.compose.material3.HorizontalDivider
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
import com.example.szakdogafitness.feature.search.list
import com.example.szakdogafitness.ui.common.NormalTextField
import com.example.szakdogafitness.ui.common.TrainerCard
import com.example.szakdogafitness.ui.common.TrainerDemo
import com.example.szakdogafitness.ui.common.appbars.TopBar
import com.example.szakdogafitness.util.calculateDistance
import com.example.szakdogafitness.viewmodel.AppBarEvent
import com.example.szakdogafitness.viewmodel.AppBarViewModel
import com.example.szakdogafitness.viewmodel.AppBarViewModel_Factory
import com.example.szakdogafitness.viewmodel.TrainersEvent
import com.example.szakdogafitness.viewmodel.TrainersViewModel
import com.google.firebase.firestore.GeoPoint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainerScreen(
    modifier : Modifier = Modifier,
    currentUser: User,
    appBarViewModel: AppBarViewModel = hiltViewModel(),
    trainersViewModel: TrainersViewModel = hiltViewModel()
    //VMs
) {

    val trainersState by trainersViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
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
        trainersViewModel.onEvent(TrainersEvent.LoadAcceptedTrainers)
        if (trainersState.allTrainersAllRating.isEmpty()) {
            trainersViewModel.onEvent(TrainersEvent.LoadAllTrainersAllRating)
        }
    }

    Column (
        modifier = modifier
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
        val filtered = testList.filter {
            (it.Fname+ " " + it.Lname).contains(search, ignoreCase = true)
        }
        if (trainersState.isLoading){
            Column (
                modifier = Modifier
                    .fillMaxSize(
                        0.95f
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        } else {
            LazyColumn (
                modifier = Modifier
                    .fillMaxWidth(0.95f),
            ) {
                items(trainersState.acceptedTrainers.size) {
                    val currentTrainer = trainersState.acceptedTrainers[it]
                    val rating = trainersState.allTrainersAllRating.find { rating ->
                        Log.d("TAG", "TrainerScreen: ${currentTrainer.second.userId}")
                        rating?.first == currentTrainer.second.userId
                    }?.second?.sumOf {
                        Log.d("TAG", "TrainerScreen: ${it.rating}")
                        it.rating
                    }?.div(
                        trainersState.allTrainersAllRating.find { rating ->
                            rating?.first == currentTrainer.second.userId
                        }?.second?.size ?: 1
                    )
                    val distance = currentTrainer.second.location?.calculateDistance(
                        currentUser.location ?: GeoPoint(0.0, 0.0)
                    )

                    TrainerCard(
                        currentTrainer = currentTrainer.second,
                        ratingData = rating ?: 0,
                        rateSize = trainersState.allTrainersAllRating.find { rating ->
                            rating?.first == currentTrainer.second.userId
                        }?.second?.size ?: 0,
                        locationDistance = distance
                    ) {}
                }
                /*items(trainersState.acceptedTrainers.size) {
                    Text(
                        text = trainersState.acceptedTrainers[it].toString()
                    )
                    HorizontalDivider()
                }*/
            }

        }
    }
}

val testList = List(3) { id ->
    TrainerDemo(
        Fname = "Johnny",
        Lname = "Depp $id",
        image = "https://wepublishnews.com/wp-content/uploads/2021/05/Johnny-Depp-Net-Worth.jpg",
        rating = id%11,
        price = 50,
        distance = 5.0f,
        gender = "male",
        specializations = listOf("cardio", "yoga", "pilates", "weightlifting", "crossfit")
    )
}