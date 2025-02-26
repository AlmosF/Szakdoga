package com.example.szakdogafitness.ui.common.nutrition

import android.R.attr.top
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TargetKcalBlock(
    modifier: Modifier = Modifier,
    targetKcal: Int,
    kcal: Int,
    maxKcal: Int = 2500,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Target kcal",
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp)
        )
        TargetIncreaseRow(
            targetKcal = targetKcal,
            onIncrease = { kcal ->
                onIncrease(kcal)
            },
            onDecrease = { kcal ->
                onDecrease(kcal)
            }
        )
        KcalBar(
            kcal = kcal,
            targetKcal = targetKcal,
            maxKcal = maxKcal,
            modifier = Modifier
                .fillMaxWidth()
        )

    }
}


@Composable
fun TargetIncreaseRow(
    modifier: Modifier = Modifier,
    targetKcal: Int,
    onIncrease: (Int) -> Unit,
    onDecrease: (Int) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = { onDecrease(100) }
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "Decrease"
            )
        }
        Text(
            text = "$targetKcal",
            fontSize = 20.sp
        )
        IconButton(
            onClick = { onIncrease(100) },
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Increase"
            )
        }
    }
}



@Composable
fun KcalBar(
    modifier : Modifier = Modifier,
    kcal : Int,
    targetKcal : Int,
    maxKcal : Int,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = (kcal.toFloat() / maxKcal.toFloat()),
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ),
        label = ""
    )
    val target by animateFloatAsState(
        targetValue = (minOf(targetKcal, maxKcal).toFloat() / maxKcal.toFloat()),
        animationSpec = tween(
            durationMillis = 800,
            delayMillis = 0,
            easing = LinearOutSlowInEasing
        ),
        label = ""
    )

    //val target = (targetKcal.toFloat() / maxKcal.toFloat())
    Column (
        modifier = modifier
    ) {
        Row (
            modifier =  Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "0",
            )
            Text(
                text = maxKcal.toString(),
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(9.dp))
                    .background(Color.Gray)
            )
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val progressWidth = animatedProgress * size.width
                val targetWidth = target * size.width
                val rectWidth = size.width * 0.15f

                val startX = maxOf(0f, targetWidth - (rectWidth / 2))
                val endX = minOf(size.width, startX + rectWidth)
                val clippedWidth = endX - startX
                drawRoundRect(
                    color = Color.Green.copy(alpha = 0.5f),
                    topLeft = Offset(startX, 0f),
                    size = androidx.compose.ui.geometry.Size(clippedWidth, size.height),
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                )
                drawLine(
                    color = Color.Black,
                    start = Offset(progressWidth, 0f),
                    end = Offset(progressWidth, size.height),
                    strokeWidth = 2.dp.toPx()
                )

            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$kcal kcal",
                color = Color.Black,
                modifier = Modifier
                    .padding(4.dp)
            )
        }

    }
}

@Composable
@Preview
fun TargetKcalBlockPreview() {
    TargetKcalBlock(
        targetKcal = 2000,
        kcal = 1500,
        onIncrease = {},
        onDecrease = {}
    )
}

