package com.example.szakdogafitness.feature.nutrition

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.szakdogafitness.ui.common.nutrition.AddNutrientsBlock
import com.example.szakdogafitness.ui.common.nutrition.TargetKcalBlock

@Composable
fun NutritionScreen(
    modifier: Modifier = Modifier
) {


    Column (
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AddNutrientsBlock(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 16.dp)
        )
        var kcal = 1500
        var targetKcal by remember { mutableStateOf(1500) }
        var maxKcal by remember { mutableStateOf(2500) }
        TargetKcalBlock(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            kcal = kcal,
            targetKcal = targetKcal,
            maxKcal = maxKcal,
            onIncrease = {
                targetKcal += it
            },
            onDecrease = {
                targetKcal -= it
            }

        )
    }


}