package com.example.szakdogafitness.feature.nutrition

import android.R.attr.data
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.szakdogafitness.ui.common.nutrition.AddNutrientsBlock
import com.example.szakdogafitness.ui.common.nutrition.TargetKcalBlock
import com.example.szakdogafitness.ui.common.nutrition.logblock.LogBlock
import com.example.szakdogafitness.viewmodel.MainViewModel
import com.example.szakdogafitness.viewmodel.MealTypesEvent
import com.example.szakdogafitness.viewmodel.MealTypesViewModel

@Composable
fun NutritionScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel(),
    mealTypesViewModel: MealTypesViewModel = hiltViewModel(),
    onAINavigate: () -> Unit,
    onSearchNavigate: () -> Unit,
    onBarcodeNavigate: () -> Unit
) {

    val mainState by mainViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        mealTypesViewModel.onEvent(MealTypesEvent.FetchNutritions)
    }

    Column (
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AddNutrientsBlock(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 16.dp),
            onAINavigate = onAINavigate,
            onSearchNavigate = onSearchNavigate,
            onBarcodeNavigate = onBarcodeNavigate
        )
        var kcal = 1500
        var targetKcal by remember { mutableStateOf(1500) }
        var maxKcal by remember { mutableStateOf(2500) }
        /*TargetKcalBlock(
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
        )*/
        TargetKcalBlock(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            kcal = mainState.currentDailyData.foodIntake.values.sumOf { it.sumOf { it.calories } },
            targetKcal = mainState.currentDailyData.targetKcal,
            maxKcal = maxKcal,
            onIncrease = {
                targetKcal += it
            },
            onDecrease = {
                targetKcal -= it
            }
        )
        LogBlock(
            //data = mainState.currentDailyData.foodIntake, //fixme("Workingone")
            data = mainState.currentDailyData.foodIntakeM,
            mealTypesViewModel = mealTypesViewModel
        )
    }


}

@Composable
@Preview
fun NutritionScreenPreview() {
    NutritionScreen(
        onAINavigate = {},
        onSearchNavigate = {},
        onBarcodeNavigate = {}
    )
}