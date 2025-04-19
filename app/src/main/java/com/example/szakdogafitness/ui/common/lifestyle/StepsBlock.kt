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
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
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
fun StepsBlock(
    modifier: Modifier = Modifier,
    steps: Int,
    goalSteps: Int
) {
    Column (
        modifier = modifier
            .padding(top = 16.dp, bottom = 16.dp)
            //.fillMaxWidth()
            //.padding(8.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.DirectionsWalk,
                contentDescription = "Steps",
                tint = Color.Black,
                modifier = Modifier
                    .size(30.dp)
            )
            Text(
                text = "Steps",
                modifier = Modifier
                    .padding(start = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                steps.toString(),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "/",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = goalSteps.toString(),
                fontSize = 20.sp,
            )
        }
    }
}

@Composable
@Preview
fun StepsBlockPreview() {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StepsBlock(steps = 1000, goalSteps = 10000)
    }

}