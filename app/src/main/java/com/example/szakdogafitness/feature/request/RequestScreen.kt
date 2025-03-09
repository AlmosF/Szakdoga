package com.example.szakdogafitness.feature.request

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.szakdogafitness.domain.model.Trainee
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.feature.search.list
import com.example.szakdogafitness.ui.common.NormalTextField
import com.example.szakdogafitness.ui.common.TrainerCard
import com.example.szakdogafitness.ui.common.request.TraineeRequestCard
import com.google.common.collect.Multimaps.index
import com.google.firebase.Timestamp

@Composable
fun RequestScreen(
    modifier: Modifier = Modifier,
    //VMs
) {
    val testRequestList = List(10) { index: Int ->
        User(
            firstName = "John",
            lastName = "Doe $index",
            profileImageUrl = "https://www.mariefranceasia.com/wp-content/uploads/sites/7/2018/02/breakfast.jpg",
            email = "johnDoe$index@asd.com",
            phoneNumber = "123456789",
            dateOfBirth = Timestamp.now(),
            trainee = Trainee(
                fitnessGoals = listOf(1, 2, 3, 4)
            )
        )
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
        val filtered = testRequestList.filter {
            (it.firstName+ " " + it.lastName).contains(search, ignoreCase = true)
        }
        LazyColumn (
            modifier = Modifier
                .fillMaxWidth(0.95f),
        ) {
            items(filtered.size) {
                TraineeRequestCard(
                    data = filtered[it],
                ) {}
            }
        }
    }
}
