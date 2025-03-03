package com.example.szakdogafitness.ui.common.nutrition

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun SearchNutritionLabels(
    modifier: Modifier = Modifier,
    selectedLabel: String = "Breakfast",
    onLabelSelected: (String) -> Unit
) {

    val labels = listOf(
        "Breakfast",
        "Morning Snack",
        "Lunch",
        "Afternoon Snack",
        "Dinner",
        "Evening Snack"
    )

    LazyRow (
        modifier = modifier
    ) {
        items(labels.size) { label ->
            OutlinedButton(
                onClick = {
                    onLabelSelected(labels[label])
                },
                modifier = Modifier
                    .padding(4.dp),
                colors = ButtonDefaults.buttonColors(

                ).copy(
                    containerColor = if (labels[label] == selectedLabel) {
                        Color.DarkGray
                    } else {
                        Color.LightGray
                    }
                )
            ) {
                Text(
                    text = labels[label]
                )
            }
        }
    }



}