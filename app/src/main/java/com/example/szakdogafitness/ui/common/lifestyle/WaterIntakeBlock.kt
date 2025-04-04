package com.example.szakdogafitness.ui.common.lifestyle

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


@Composable
fun WaterIntakeBlock(
    modifier: Modifier = Modifier,
    consumedWater: Double,
    goalWater: Double,
    personal: Boolean = true,
    onModifyTarget: (Double) -> Unit = {},
    onModifyIntake: (Double) -> Unit = {},
) {
    var isOpen by remember { mutableStateOf(false) }

    Column (
        modifier = modifier
            .padding(top = 16.dp, bottom = 16.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocalDrink,
                contentDescription = "Water Intake",
                tint = Color.Cyan,
                modifier = Modifier
                    .size(30.dp)
            )
            Text(
                text = "Water Intake",
                modifier = Modifier
                    .padding(start = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            if (personal){
                IconButton(
                    onClick = {
                        isOpen = !isOpen
                    }
                ) {
                    Icon(
                        imageVector = if (isOpen) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                        contentDescription = "Edit",
                        tint = Color.Cyan,
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
            }
        }
        AnimatedVisibility(isOpen){
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Edit Target",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Row (
                    modifier = Modifier
                        .padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = {
                            if (goalWater > 0.5) {
                                onModifyTarget(-0.25)
                            }
                        },
                        enabled = goalWater > 0.5
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease"
                        )
                    }
                    Text(
                        text = "0.25L",
                        fontSize = 20.sp,
                    )
                    IconButton(
                        onClick = {
                            onModifyTarget(0.25)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase"
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row (
                modifier = Modifier
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    consumedWater.toString(),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "/",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${goalWater}L",
                    fontSize = 20.sp,
                )
            }
            if (personal){
                Row (
                    modifier = Modifier
                        .padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = {
                            if (consumedWater > 0.0) {
                                onModifyIntake(-0.25)
                            }
                        },
                        enabled = consumedWater > 0.0
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Decrease"
                        )
                    }
                    Text(
                        text = "0.25L",
                        fontSize = 20.sp,
                    )
                    IconButton(
                        onClick = {
                            onModifyIntake(0.25)
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Increase"
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun WaterIntakeBlockPreview() {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WaterIntakeBlock(
            consumedWater = 1.5,
            goalWater = 2.0
        )

    }
}