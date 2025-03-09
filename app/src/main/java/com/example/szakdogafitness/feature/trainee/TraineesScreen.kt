package com.example.szakdogafitness.feature.trainee

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
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.ui.common.NormalTextField
import com.example.szakdogafitness.ui.common.trainee.TraineeCard

@Composable
fun TraineesScreen(
    modifier: Modifier = Modifier,
    //VMs
) {
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
        val filtered = testData.filter {
            (it.firstName+" "+it.lastName).contains(search, ignoreCase = true)
        }
        LazyColumn (
            modifier = Modifier
                .fillMaxWidth(0.95f)
        ) {
            items(filtered.size) {
                TraineeCard(
                    data = filtered[it],
                    onCardClick = {},
                    onMessageClick = {}
                )
            }
        }
    }
}

val testData = listOf<User>(
    User(firstName = "John", lastName = "Doe", phoneNumber = "123456789", profileImageUrl = "https://www.mariefranceasia.com/wp-content/uploads/sites/7/2018/02/breakfast.jpg"),
    User(firstName = "Jane", lastName = "Smith", phoneNumber = "987654321", profileImageUrl = "https://www.mariefranceasia.com/wp-content/uploads/sites/7/2018/02/breakfast.jpg"),
    User(firstName = "Alice", lastName = "Johnson", phoneNumber = "555555555", profileImageUrl = "https://www.mariefranceasia.com/wp-content/uploads/sites/7/2018/02/breakfast.jpg"),
    User(firstName = "Bob", lastName = "Brown", phoneNumber = "444444444", profileImageUrl = "https://www.mariefranceasia.com/wp-content/uploads/sites/7/2018/02/breakfast.jpg"),
    User(firstName = "Charlie", lastName = "Davis", phoneNumber = "333333333", profileImageUrl = "https://www.mariefranceasia.com/wp-content/uploads/sites/7/2018/02/breakfast.jpg"),
    User(firstName = "David", lastName = "Wilson", phoneNumber = "222222222", profileImageUrl = "https://www.mariefranceasia.com/wp-content/uploads/sites/7/2018/02/breakfast.jpg"),
    User(firstName = "Eve", lastName = "Moore", phoneNumber = "111111111", profileImageUrl = "https://www.mariefranceasia.com/wp-content/uploads/sites/7/2018/02/breakfast.jpg"),
)