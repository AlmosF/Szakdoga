package com.example.szakdogafitness.ui.common.nutrition.logblock

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.szakdogafitness.feature.nutrition.MealLogEvent
import com.example.szakdogafitness.feature.nutrition.MealLogViewModel
import com.example.szakdogafitness.feature.nutrition.MealTypesViewModel

enum class BlockState {
    Closed,
    Breakfast,
    Lunch,
    Dinner,
    Morning_Snack,
    Afternoon_Snack,
    Evening_Snack
}


@Composable
fun LogBlock(
    modifier: Modifier = Modifier,
    data: Map<String, List<Int>>,
    viewModel: MealLogViewModel = hiltViewModel(),
    mealTypesViewModel: MealTypesViewModel = hiltViewModel(),
    onRemove: (Int, String) -> Unit = { _, _ -> }
) {

    val titles = listOf(
        BlockState.Breakfast,
        BlockState.Lunch,
        BlockState.Dinner,
        BlockState.Morning_Snack,
        BlockState.Afternoon_Snack,
        BlockState.Evening_Snack
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val mealState by mealTypesViewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit, data) {
        viewModel.onEvent(MealLogEvent.SetupDemoMeal(data))
    }
    Column (
        modifier = modifier
            .fillMaxWidth(0.95f)
            .padding(8.dp)
            .background(Color.LightGray.copy(0.5f), shape = RoundedCornerShape(16.dp))
        ,
        horizontalAlignment = Alignment.Start
    ) {
        Row (
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "Log meal",
                fontWeight = FontWeight.Bold
            )
        }
        titles.forEach {
            Log.d("LogBlock", "Title: ${it.name}")
            Log.d("LogBlock", "Title: ${state.consumedMeals}")

            LogBlockRow(
                title = it.name.replaceFirstChar { it.uppercase() },
                data = state.consumedMeals[it.name] ?: emptyList(),
                state = state.currentBlockState,
                meals = mealState.nutritions,
                onOpen = {
                    viewModel.onEvent(MealLogEvent.BlockStateChanged(it))
                },
                onRemove = { nutritionID, mealType ->
                    onRemove(
                        nutritionID, mealType
                    )
                }
            )
        }
        Spacer(modifier = Modifier.padding(4.dp))
    }
}