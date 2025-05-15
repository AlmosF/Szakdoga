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
 * @param unit: if true, the weight will be displayed in kg, otherwise in lbs
 */
@Composable
fun WeightSelector(
    modifier: Modifier = Modifier,
    width: Dp,
    itemHeight: Dp,
    textStyle: TextStyle,
    unit: Boolean = true,
    textColor: Color,
    weight: Double = 50.0,
    onWeightSelected: (weight: Double) -> Unit = { _ -> }
){
    val decimalWeight = remember { mutableIntStateOf(
        if (unit) { //kg
            ((weight * 10) % 10).toInt()
        } else { //lbs
            ((weight * 10 * 2.2) % 10).toInt()
        }
    ) }
    val sWeight = remember { mutableIntStateOf(
        if (unit) { //kg
            weight.toInt()
        } else { //lbs
            (weight * 2.2).toInt()
        }
    ) }

    Row (
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        InfiniteCircularList(
            width = width,
            itemHeight = itemHeight,
            items = (0..250).toList(),
            initialItem = sWeight.intValue,
            textStyle = textStyle,
            textColor = textColor,
            selectedTextColor = Color.Black,
            onItemSelected = { _, item ->
                sWeight.intValue = item
                onWeightSelected(sWeight.intValue.toDouble() + decimalWeight.intValue * 0.1)
            }
        )
        Text(".",
            fontSize = 30.sp
        )

        InfiniteCircularList(
            width = width,
            itemHeight = itemHeight,
            items = (0..9).toList(),
            initialItem = decimalWeight.intValue,
            textStyle = textStyle,
            textColor = textColor,
            selectedTextColor = Color.Black,
            onItemSelected = { _, item ->
                decimalWeight.intValue = item
                onWeightSelected(sWeight.intValue.toDouble() + decimalWeight.intValue * 0.1)
            }
        )
    }
}

fun convertWeightToStoredUnit(weightInKg: Double, unit: Boolean): Double {
    return if (unit) {
        weightInKg
    } else {
        weightInKg * 2.20462
    }
}

fun convertWeightToKg(weightInStoredUnit: Double, unit: Boolean): Double {
    return if (unit) {
        weightInStoredUnit
    } else {
        weightInStoredUnit / 2.20462
    }
}