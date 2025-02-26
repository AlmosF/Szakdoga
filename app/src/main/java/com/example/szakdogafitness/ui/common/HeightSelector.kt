package com.example.szakdogafitness.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
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
    unit : Boolean = true,
    height: Int = 120,
    onHeightSelected: (cm: Int) -> Unit = { _ -> }
) {
    val selectedHeight = remember { mutableIntStateOf(height) }

    if (unit){
        InfiniteCircularList(
            width = width,
            itemHeight = itemHeight,
            items = (100..250).toList(),
            initialItem = selectedHeight.intValue,
            textStyle = textStyle,
            textColor = textColor,
            selectedTextColor = selectedTextColor,
            onItemSelected = { _, item ->
                selectedHeight.intValue = item
                onHeightSelected(selectedHeight.intValue)
            }
        )
    } else {
        // Convert CM to Feet and Inches
        val totalInches = (selectedHeight.intValue / 2.54).toInt()  // Convert CM to total inches
        val feet = totalInches / 12  // Calculate feet from total inches
        val inches = totalInches % 12  // Calculate inches from the remaining total inches

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
                items = (3..8).toList(),  // Feet range
                initialItem = feet,  // Correctly initialize the feet value
                textStyle = textStyle,
                textColor = textColor,
                selectedTextColor = selectedTextColor,
                onItemSelected = { _, item ->
                    // Calculate total inches based on the selected feet and current inches
                    val newInches = (item * 12) + inches
                    val newCm = newInches * 2.54  // Convert back to CM
                    selectedHeight.intValue = newCm.toInt()  // Update stored CM value
                    onHeightSelected(selectedHeight.intValue)
                }
            )
            Text(" ft ", fontSize = 30.sp)

            // Inches Selector
            InfiniteCircularList(
                width = width,
                itemHeight = itemHeight,
                items = (0..11).toList(),  // Inches range
                initialItem = inches+1,  // Correctly initialize the inches value
                textStyle = textStyle,
                textColor = textColor,
                selectedTextColor = selectedTextColor,
                onItemSelected = { _, item ->
                    // Calculate total inches based on the selected feet and current inches
                    val newInches = (feet * 12) + item
                    val newCm = newInches * 2.54  // Convert back to CM
                    selectedHeight.intValue = newCm.toInt()  // Update stored CM value
                    onHeightSelected(selectedHeight.intValue)
                }
            )
            Text(" in", fontSize = 30.sp)
        }
    }
}