package com.example.szakdogafitness.feature.search

import android.R.attr.checked
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.szakdogafitness.ui.common.appbars.TopBar
import com.example.szakdogafitness.util.calculateDistance
import com.example.szakdogafitness.viewmodel.AppBarEvent
import com.example.szakdogafitness.viewmodel.AppBarViewModel
import com.example.szakdogafitness.viewmodel.MealTypesEvent
import com.example.szakdogafitness.viewmodel.TrainersEvent
import com.example.szakdogafitness.viewmodel.TrainersViewModel
import com.google.firebase.firestore.GeoPoint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.D

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTrainerScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    trainersViewModel: TrainersViewModel = hiltViewModel(),
    appBarViewModel: AppBarViewModel = hiltViewModel(),
    currentUser: User,
    onTrainerClick: (String) -> Unit
    //VMs
) {

    val trainersState by trainersViewModel.state.collectAsStateWithLifecycle()
    val trainersFiltersState by trainersViewModel.filter.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        appBarViewModel.onEvent(
            AppBarEvent.TopBarComposableChanged(
                {
                    TopBar(
                        title = {
                            Text("Search Trainer")
                        },
                    )
                }
            )
        )
        //mainViewModel.onEvent(MainEvent.FetchTrainers)
        trainersViewModel.onEvent(TrainersEvent.LoadTrainers)
        //if (trainersState.allTrainersAllRating.isEmpty()){
        //    trainersViewModel.onEvent(TrainersEvent.LoadAllTrainersAllRating)
        //} //removed because its loading with the loadtrainers fun, bercause it was stopping the loading var.
        Log.d("SearchTrainerScreen", "AllTrainersAllRating: ${trainersState.allTrainersAllRating}")

    }

    //val mainState by mainViewModel.state.collectAsStateWithLifecycle()
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
                /*Icon(
                    imageVector = Icons.Filled.FilterAlt,
                    contentDescription = "Filter",
                    modifier = Modifier
                        .size(32.dp)
                )*/
                TrainerFilter(
                    trainersViewModel = trainersViewModel,
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
                val filteredList = trainersState.trainers.filter { trainer ->
                    val fullName = "${trainer?.firstName} ${trainer?.lastName}"

                    val matchesName = search.isBlank() || fullName.contains(search, ignoreCase = true)

                    val matchesGender = when {
                        trainersFiltersState.filterMen && !trainersFiltersState.filterWomen -> trainer?.gender == 1
                        trainersFiltersState.filterWomen && !trainersFiltersState.filterMen -> trainer?.gender == 2
                        trainersFiltersState.filterMen && trainersFiltersState.filterWomen -> trainer?.gender == 1 || trainer?.gender == 2
                        else -> true
                    }

                    matchesName && matchesGender
                }
                LazyColumn (
                    modifier = Modifier
                        .fillMaxWidth(0.95f),
                ) {
                    /*items(list.size) {
                        TrainerCard(
                            data = list[it],
                        )
                    }*/

                    /*items(trainersState.trainers.size) {
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
                    }*/
                    items(filteredList.size) {
                        val user = filteredList[it]!!
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
                            currentTrainer = filteredList[it]!!,
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
fun TrainerFilter(
    trainersViewModel: TrainersViewModel = hiltViewModel(),
){
    var isFilterMenuExpanded by remember { mutableStateOf(false) }
    val filtersState by trainersViewModel.filter.collectAsStateWithLifecycle()
    Box  {
        IconButton(onClick = {
            isFilterMenuExpanded = true
        }) {
            Icon(
                imageVector = Icons.Filled.FilterAlt,
                contentDescription = "Filter",
                modifier = Modifier.size(32.dp),
                tint = if (filtersState.filterMen || filtersState.filterWomen) {
                    Color.Red
                } else {
                    Color.Black
                }
            )
        }

        DropdownMenu(
            expanded = isFilterMenuExpanded,
            onDismissRequest = { isFilterMenuExpanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = filtersState.filterMen,
                            onCheckedChange = {
                                trainersViewModel.onEvent(
                                    TrainersEvent.FilterTrainers(
                                        filterMen = !filtersState.filterMen,
                                        filterWomen = filtersState.filterWomen
                                    )
                                )
                            }
                        )
                        Text(
                            text = "Men"
                        )
                    }
                },
                onClick = {
                    isFilterMenuExpanded = false
                    trainersViewModel.onEvent(
                        TrainersEvent.FilterTrainers(
                            filterMen = !filtersState.filterMen,
                            filterWomen = filtersState.filterWomen
                        )
                    )
                }
            )
            DropdownMenuItem(
                text = {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = filtersState.filterWomen,
                            onCheckedChange = {
                                trainersViewModel.onEvent(
                                    TrainersEvent.FilterTrainers(
                                        filterMen = filtersState.filterMen,
                                        filterWomen = !filtersState.filterWomen
                                    )
                                )
                            }
                        )
                        Text(
                            text = "Women"
                        )
                    }
                },
                onClick = {
                    isFilterMenuExpanded = false
                    trainersViewModel.onEvent(
                        TrainersEvent.FilterTrainers(
                            filterMen = filtersState.filterMen,
                            filterWomen = !filtersState.filterWomen
                        )
                    )
                },
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Clear Filter", color = MaterialTheme.colorScheme.error) },
                onClick = {
                    isFilterMenuExpanded = false
                    trainersViewModel.onEvent(
                        TrainersEvent.FilterTrainers(
                            filterMen = false,
                            filterWomen = false
                        )
                    )
                }
            )
        }
    }
}





@Composable
@Preview
fun SearchTrainerScreenPreview() {
    /*SearchTrainerScreen(

    )*/
}
