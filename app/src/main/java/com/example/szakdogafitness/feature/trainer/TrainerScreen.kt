package com.example.szakdogafitness.feature.trainer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
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
import com.example.szakdogafitness.viewmodel.TrainersEvent
import com.example.szakdogafitness.viewmodel.TrainersViewModel

@Composable
fun TrainerScreen(
    modifier : Modifier = Modifier,
    trainersViewModel: TrainersViewModel = hiltViewModel()
    //VMs
) {

    val trainersState by trainersViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        trainersViewModel.onEvent(TrainersEvent.LoadAcceptedTrainers)
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
        LazyColumn (
            modifier = Modifier
                .fillMaxWidth(0.95f),
        ) {
            /*items(filtered.size) {
                TrainerCard(
                    //data = filtered[it],
                    data = User(),
                ) {}
            }*/
            items(trainersState.acceptedTrainers.size) {
                Text(
                    text = trainersState.acceptedTrainers[it].toString()
                )
                HorizontalDivider()
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