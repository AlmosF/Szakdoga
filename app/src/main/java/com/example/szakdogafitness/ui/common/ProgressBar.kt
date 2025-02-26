package com.example.szakdogafitness.ui.common

import android.R.attr.text
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ProgressBar(
    modifier: Modifier = Modifier,
    progress: Float
){
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 200,
            easing = LinearOutSlowInEasing
        ), label = "progess_animation"
    )

    Column (
        modifier = modifier
    ) {

        Box (
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(9.dp))
                    .background(Color.Gray)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(9.dp))
                    .background(Color.Green)
                    .animateContentSize()
            )
        }
        Row (
            modifier = Modifier
                .widthIn(min = 30.dp)
                .fillMaxWidth()
                .padding(10.dp)
            ,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = String.format("%.0f%%", animatedProgress * 100),
                color = Color.Cyan
            )
        }
    }
}


@Composable
@Preview
fun ProgressBarPreview(){

    ProgressBar(
        progress = 0.5f
    )

}