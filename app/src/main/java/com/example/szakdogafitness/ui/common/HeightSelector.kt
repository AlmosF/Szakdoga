package com.example.szakdogafitness.ui.common

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * @param unit: if true, the height will be displayed in cm, otherwise in ft
 * @param height: initial height in cm
 * @param onHeightSelected: callback when a height is selected
 * @param itemHeight: height of each item in the list
 * @param selectedTextColor: color of the selected item
 * @param textColor: color of the unselected items
 * @param textStyle: text style of the items
 * @param width: width of the list
 * @param modifier: modifier for the list
 */

@Composable
fun HeightSelector(
    modifier: Modifier = Modifier,
    width: Dp,
    itemHeight: Dp,
    textStyle: TextStyle,
    textColor: Color,
    selectedTextColor: Color,
    unit: Boolean = true,
    heightCm: Int/* = 120*/,
    onHeightSelected: (Int) -> Unit = {}
) {
    val selectedHeight = remember { mutableIntStateOf(heightCm) }
    Log.d("HeightSelector", "selectedHeight: ${selectedHeight.value}, unit: $unit")

    if (unit) {
        InfiniteCircularList(
            width = width,
            itemHeight = itemHeight,
            items = (100..250).toList(),
            initialItem = heightCm,
            textStyle = textStyle,
            textColor = textColor,
            selectedTextColor = selectedTextColor,
            onItemSelected = { _, item ->
                onHeightSelected(item)
            }
        )
    } else {
        // Imperial (inches)
        val totalInches = selectedHeight.intValue.div(2.54).toInt()
        val feet = totalInches / 12
        val inches = totalInches % 12
        Log.d("HeightSelector", "selectedHeight: ${selectedHeight.intValue}, totalInches: $totalInches, feet: $feet, inches: $inches")
        var selectedFeet by remember { mutableIntStateOf(feet) }
        var selectedInches by remember { mutableIntStateOf(inches) }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Feet Selector
            InfiniteCircularList(
                width = width,
                itemHeight = itemHeight,
                items = (3..8).toList(),
                initialItem = selectedFeet,
                textStyle = textStyle,
                textColor = textColor,
                selectedTextColor = selectedTextColor,
                onItemSelected = { _, item ->
                    selectedFeet = item
                    onHeightSelected(
                        (((selectedFeet * 12) + selectedInches) * 2.54).toInt()
                    )
                }
            )
            Text(" ft ", fontSize = 30.sp)

            // Inches Selector
            InfiniteCircularList(
                width = width,
                itemHeight = itemHeight,
                items = (0..11).toList(),
                initialItem = inches,
                textStyle = textStyle,
                textColor = textColor,
                selectedTextColor = selectedTextColor,
                onItemSelected = { _, item ->
                    selectedInches = item
                    onHeightSelected(
                        (((selectedFeet * 12) + selectedInches) * 2.54).toInt()
                    )

                }
            )
            Text(" in", fontSize = 30.sp)
        }
    }
}

fun Int.toFeetInches(): Pair<Int, Int> {
    val feet = this / 12
    val inches = this % 12
    return Pair(feet, inches)
}
