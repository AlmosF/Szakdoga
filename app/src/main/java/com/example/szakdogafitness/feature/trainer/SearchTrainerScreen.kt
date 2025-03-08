package com.example.szakdogafitness.feature.trainer

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawingPadding
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.szakdogafitness.domain.model.Trainer
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.ui.common.NormalTextField
import com.example.szakdogafitness.ui.common.TrainerCard
import com.example.szakdogafitness.ui.common.TrainerDemo
import kotlin.collections.filter

@Composable
fun SearchTrainerScreen(
    modifier: Modifier = Modifier,
    //VMs
    trainers: List<User> = emptyList()
) {
    Log.d("SearchTrainerScreen", "SearchTrainerScreen: $trainers")

    var search by remember {
        mutableStateOf("")
    }
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
        val filtered = trainers.filter {
            (it.firstName+" "+it.lastName).contains(search, ignoreCase = true)
        }
        LazyColumn (
            modifier = Modifier
                .fillMaxWidth(0.95f),
        ) {
            items(filtered.size) {
                TrainerCard(
                    data = filtered[it].toTrainerDemo(),
                )
            }
        }
    }
}

fun User.toTrainerDemo(): TrainerDemo {
    return TrainerDemo(
        Fname = firstName,
        Lname = lastName,
        image = profileImageUrl,
        rating = trainer.rating.toInt(),
        price = trainer.pricing,
        distance = 0.0f,
        gender = this.gender.toString(),
        specializations = trainer.specializations.map { it.toString() }
    )
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
    SearchTrainerScreen(
    )
}
