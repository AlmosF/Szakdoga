package com.example.szakdogafitness.ui.common

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun TrainerTimetableView(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedHours: Map<String, List<Int>> = emptyMap(),
    onHourSelected: (String, Int) -> Unit
) {
    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
    var selectedDay by remember { mutableStateOf(days.first()) }

    Column(modifier = modifier) {
        Text(text = "Select Available Appointments", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(days.size) { day ->
                val day = days[day]
                DayChip(
                    day = day,
                    isSelected = selectedDay == day,
                    onClick = { selectedDay = day },
                )
            }
        }


        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(9) { index ->
                val hour = index + 8
                val isSelected = selectedHours[selectedDay]?.contains(hour) == true

                TimeChip(
                    time = "$hour:00",
                    isSelected = isSelected,
                    onClick = { onHourSelected(selectedDay, hour) },
                    enabled = enabled
                )
            }
        }
    }
}

@Composable
fun TraineeTimetableView(
    modifier: Modifier = Modifier,
    availableHours: Map<String, List<Int>>, // Set by trainer
    selectedHours: Map<String, List<Int>> = emptyMap(), // Set by trainee
    onHourSelected: (String, Int) -> Unit
) {
    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
    var selectedDay by remember { mutableStateOf(days.firstOrNull() ?: "") }


    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Select Your Appointments", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(days.size) { day ->
                val day = days[day]
                val isEnabled = availableHours[day]?.isNotEmpty() == true
                DayChip(
                    day = day,
                    isSelected = selectedDay == day,
                    onClick = { selectedDay = day },
                    isEnabled = isEnabled
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(9) { index ->
                val hour = index + 8
                val isAvailable = availableHours[selectedDay]?.contains(hour) == true
                Log.d("TimetableView", "isAvailable: ${isAvailable} selectedDay: ${selectedDay} - ${availableHours[selectedDay]}")
                val isSelected = selectedHours[selectedDay]?.contains(hour) == true
                Log.d("TimetableView", "isSelected: ${selectedHours[selectedDay]}")

                TimeChip(
                    time = "$hour:00",
                    isSelected = isSelected,
                    onClick = { if (isAvailable) onHourSelected(selectedDay, hour) },
                    enabled = isAvailable
                )
            }
        }
    }
}

@Composable
fun DayChip(day: String, isSelected: Boolean, onClick: () -> Unit, isEnabled : Boolean = true) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) Color.Blue else Color.Transparent,
            contentColor = if (isSelected) Color.White else Color.Black,
            disabledContainerColor = if (isSelected) Color.Blue.copy(alpha = 0.5f) else Color.Transparent //Not fix yet
        ),
        enabled = isEnabled,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = day.take(3))
    }
}

@Composable
fun TimeChip(time: String, isSelected: Boolean, onClick: () -> Unit, enabled: Boolean = true) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (isSelected) Color.Green else Color.Unspecified,
            disabledContainerColor = if (isSelected) Color.Green.copy(alpha = 0.5f) else Color.Unspecified,
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = time)
    }
}
@Composable
fun TimetableColumn(
    day: String,
    selectedHours: Map<String, List<Int>>,
    select: Boolean = false,
    onHourSelected: (String, Int) -> Unit
) {
    Column(
        modifier = Modifier.width(150.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = day.substring(0, 3),
            modifier = Modifier.padding(4.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = Color.Black
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(9) { index ->
                ElevatedButton(
                    onClick = {
                        if (!select) {
                            onHourSelected(day, index)
                        }
                    },
                    enabled = !select || selectedHours[day]?.contains(index) == true,

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 4.dp, start = 8.dp, end = 8.dp),
                    colors = ButtonDefaults.elevatedButtonColors().copy(
                        containerColor = if (selectedHours[day]?.contains(index) == true)
                            Color.Cyan
                        else
                            Color.Unspecified
                    )
                ) {
                    Text(
                        text = "${index + 8}:00-${index + 9}:00",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
