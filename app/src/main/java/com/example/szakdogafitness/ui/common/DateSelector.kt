package com.example.szakdogafitness.ui.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DateSelector(
    modifier: Modifier = Modifier,
    width: Dp,
    itemHeight: Dp,
    textStyle: TextStyle,
    textColor: Color,
    selectedTextColor: Color,
    //date: Timestamp = Timestamp.now(),
    date: Long = System.currentTimeMillis(),
    onDateSelected: (day: Int, month: Int, year: Int) -> Unit = { _, _, _ -> }
) {
    val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(date).toInt()
    val month = SimpleDateFormat("MM", Locale.getDefault()).format(date).toInt()
    val day = SimpleDateFormat("dd", Locale.getDefault()).format(date).toInt()

    val currentYear = remember { mutableIntStateOf(year) }
    val currentMonth = remember { mutableIntStateOf(month) }
    val currentDay = remember { mutableIntStateOf(day) }

    // Recalculate days in the selected month and year
    val daysInMonth = remember(currentYear.intValue, currentMonth.intValue) {
        when (currentMonth.intValue) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (isLeapYear(currentYear.intValue)) 29 else 28
            else -> 31
        }
    }

    LaunchedEffect(currentMonth.intValue, currentYear.intValue) {
        if (currentDay.intValue > daysInMonth) {
            currentDay.intValue = daysInMonth
        }
        onDateSelected(currentDay.intValue, currentMonth.intValue, currentYear.intValue)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        InfiniteCircularList(
            width = width,
            itemHeight = itemHeight,
            items = (1..daysInMonth).toList(), // Dynamically update days
            initialItem = currentDay.intValue,
            textStyle = textStyle,
            textColor = textColor,
            selectedTextColor = selectedTextColor,
            onItemSelected = { _, day ->
                currentDay.intValue = day
                onDateSelected(currentDay.intValue, currentMonth.intValue, currentYear.intValue)
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        InfiniteCircularList(
            width = width,
            itemHeight = itemHeight,
            items = (1..12).toList(),
            initialItem = currentMonth.intValue,
            textStyle = textStyle,
            textColor = textColor,
            selectedTextColor = selectedTextColor,
            onItemSelected = { _, month ->
                currentMonth.intValue = month
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        InfiniteCircularList(
            width = width,
            itemHeight = itemHeight,
            items = (1900..2100).toList(),
            initialItem = currentYear.intValue,
            textStyle = textStyle,
            textColor = textColor,
            selectedTextColor = selectedTextColor,
            onItemSelected = { _, year ->
                currentYear.intValue = year
            }
        )
    }
}

fun isLeapYear(year: Int): Boolean {
    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
}

