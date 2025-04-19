package com.example.szakdogafitness.ui.common.nutrition

import android.util.Log
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
    selectedLabel: String = "bf",
    onLabelSelected: (String) -> Unit
) {

    /*val labels = listOf(
        "breakfast",
        //"Morning Snack",
        "morning_snack",
        "lunch",
        "afternoon_snack",
        "dinner",
        "evening_snack"
    )*/
    val labelsMap = mapOf(
        "breakfast" to "bf",
        "morning_snack" to "ms",
        "lunch" to "l",
        "afternoon_snack" to "as",
        "dinner" to "d",
        "evening_snack" to "es"
    )

    LazyRow (
        modifier = modifier
    ) {
        items(labelsMap.size) { label ->
            OutlinedButton(
                onClick = {
                    Log.d("SearchNutritionLabels", "onLabelSelected: ${labelsMap.keys.elementAt(label)}")
                    onLabelSelected(labelsMap.values.elementAt(label))
                },
                modifier = Modifier
                    .padding(4.dp),
                colors = ButtonDefaults.buttonColors(

                ).copy(
                    //containerColor = if (labels[label] == selectedLabel) {
                    containerColor = if (labelsMap.values.elementAt(label) == selectedLabel) {
                        Color.DarkGray
                    } else {
                        Color.LightGray
                    }
                )
            ) {
                Text(
                    text = labelsMap.keys.elementAt(label).replace("_", " ").replaceFirstChar(Char::uppercase),
                )
            }
        }
    }



}