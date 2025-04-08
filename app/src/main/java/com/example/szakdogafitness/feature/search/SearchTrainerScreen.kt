package com.example.szakdogafitness.feature.search

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.ui.common.NormalTextField
import com.example.szakdogafitness.ui.common.TrainerCard
import com.example.szakdogafitness.ui.common.TrainerDemo
import com.example.szakdogafitness.util.calculateDistance
import com.example.szakdogafitness.viewmodel.TrainersEvent
import com.example.szakdogafitness.viewmodel.TrainersViewModel
import com.google.firebase.firestore.GeoPoint

@Composable
fun SearchTrainerScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    trainersViewModel: TrainersViewModel = hiltViewModel(),
    currentUser: User,
    onTrainerClick: (String) -> Unit
    //VMs
) {

    val trainersState by trainersViewModel.state.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        //mainViewModel.onEvent(MainEvent.FetchTrainers)
        trainersViewModel.onEvent(TrainersEvent.LoadTrainers)
        if (trainersState.allTrainersAllRating.isEmpty()){
            trainersViewModel.onEvent(TrainersEvent.LoadAllTrainersAllRating)
        }
        Log.d("SearchTrainerScreen", "AllTrainersAllRating: ${trainersState.allTrainersAllRating}")

    }

    //val mainState by mainViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(trainersState.allTrainersAllRating) {
        Log.d("SearchTrainerScreen", "AllTrainersAllRating: ${trainersState.allTrainersAllRating}")
    }

    Column (
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NormalTextField(
            modifier = Modifier
                .fillMaxWidth(0.95f),
            value = "",
            onValueChange = {},
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
        if (trainersState.isLoading){
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
        }else {
            if (trainersState.trainers.isEmpty()) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                ) {
                    Text("No trainers found")
                }
            } else {
                LazyColumn (
                    modifier = Modifier
                        .fillMaxWidth(0.95f),
                ) {
                    /*items(list.size) {
                        TrainerCard(
                            data = list[it],
                        )
                    }*/

                    items(trainersState.trainers.size) {
                        val user = trainersState.trainers[it]!!
                        val rating = trainersState.allTrainersAllRating.find { rating ->
                            rating?.first == user.userId
                        }?.second?.sumOf {
                            it.rating
                        }?.div(
                            trainersState.allTrainersAllRating.find { rating ->
                                rating?.first == user.userId
                            }?.second?.size ?: 1
                        )
                        val distance = user.location?.calculateDistance(currentUser.location ?: GeoPoint(0.0,0.0))
                        Log.d("SearchTrainerScreen", "Rating: $rating")
                        TrainerCard(
                            currentTrainer = trainersState.trainers[it]!!,
                            ratingData = rating ?: 0,
                            rateSize = trainersState.allTrainersAllRating.find { rating ->
                                rating?.first == user.userId
                            }?.second?.size ?: 0,
                            locationDistance = distance
                        ) { userId ->
                            onTrainerClick(userId)
                        }
                    }
                }
            }
        }

    }
}

val list = List(21) { id ->
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





@Composable
@Preview
fun SearchTrainerScreenPreview() {
    /*SearchTrainerScreen(

    )*/
}
