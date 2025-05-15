package com.example.szakdogafitness.ui.common.lifestyle

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
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WeightControlBlock(
    modifier: Modifier = Modifier,
    personal: Boolean = true,
    weight: Double = 80.0,
    unit: Int = 1
) {
    Column (
        modifier = modifier
            .padding(top = 16.dp, bottom = 16.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
            .fillMaxWidth(),
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Default.MonitorWeight,
                contentDescription = "Weight",
                tint = Color.Black,
                modifier = Modifier
                    .size(30.dp)
            )
            Text(
                text = "Weight",
                modifier = Modifier
                    .padding(start = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = if (unit == 1) {
                    "$weight kg"
                } else {
                    "${"%.1f".format(weight * 2.20462)} lbs"
                },
                fontSize = 25.sp,
            )
        }
    }
}

@Composable
@Preview
fun WeightControlBlockPreview() {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WeightControlBlock()

    }
}