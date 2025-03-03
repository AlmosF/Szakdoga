package com.example.szakdogafitness.ui.common.nutrition.logblock

import android.R.attr.data
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.szakdogafitness.domain.model.Nutrition
import com.example.szakdogafitness.viewmodel.MealLogEvent
import com.example.szakdogafitness.viewmodel.MealLogViewModel

enum class BlockState {
    Closed,
    Breakfast,
    Lunch,
    Dinner,
    Morning_Snack,
    afternoon_Snack,
    evening_Snack
}


@Composable
fun LogBlock(
    modifier: Modifier = Modifier,
    //data: Map<String, List<Nutrition>>,
    viewModel: MealLogViewModel = viewModel(),
    onRemove: (String, Int) -> Unit = { _, _ -> }
) {
    //val titles = listOf("Breakfast", "Lunch", "Dinner", "Morning Snack", "Afternoon Snack", "Evening Snack")
    val titles = listOf(
        BlockState.Breakfast,
        BlockState.Lunch,
        BlockState.Dinner,
        BlockState.Morning_Snack,
        BlockState.afternoon_Snack,
        BlockState.evening_Snack
    )
    //val blockState by viewModel.currentBlockState.collectAsStateWithLifecycle()
    //val consumedMeals by viewModel.consumedMeals.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()

    var testData by remember { mutableStateOf(mapOf<String, List<Nutrition>>(
        "Breakfast" to listOf(
            Nutrition(1, "Egg", 100, 10, 10, 10, 10, ""),
            Nutrition(2, "Bread", 100, 10, 10, 10, 10, ""),
            Nutrition(3, "Butter", 100, 10, 10, 10, 10, ""),
            Nutrition(4, "Milk", 100, 10, 10, 10, 10, ""),
        ),
        "Lunch" to listOf(
            Nutrition(5, "Rice", 100, 10, 10, 10, 10, ""),
            Nutrition(6, "Chicken", 100, 10, 10, 10, 10, ""),
            Nutrition(7, "Salad", 100, 10, 10, 10, 10, ""),
            Nutrition(8, "Water", 100, 10, 10, 10, 10, ""),
        ),
        "Dinner" to listOf(
            Nutrition(9, "Pasta", 100, 10, 10, 10, 10, ""),
            Nutrition(10, "Fish", 100, 10, 10, 10, 10, ""),
            Nutrition(11, "Soup", 100, 10, 10, 10, 10, ""),
            Nutrition(12, "Juice", 100, 10, 10, 10, 10, ""),
        ),
        "Morning Snack" to listOf(
            Nutrition(13, "Apple", 100, 10, 10, 10, 10, ""),
            Nutrition(14, "Banana", 100, 10, 10, 10, 10, ""),
            Nutrition(15, "Orange", 100, 10, 10, 10, 10, ""),
            Nutrition(16, "Pear", 100, 10, 10, 10, 10, ""),
        ),
        "Afternoon Snack" to listOf(
            Nutrition(17, "Chocolate", 100, 10, 10, 10, 10, ""),
            Nutrition(18, "Candy", 100, 10, 10, 10, 10, ""),
            Nutrition(19, "Cookie", 100, 10, 10, 10, 10, ""),
            Nutrition(20, "Ice Cream", 100, 10, 10, 10, 10, ""),
        ),
        "Evening Snack" to listOf(
            Nutrition(21, "Chips", 100, 10, 10, 10, 10, ""),
            Nutrition(22, "Popcorn", 100, 10, 10, 10, 10, ""),
            Nutrition(23, "Pretzel", 100, 10, 10, 10, 10, ""),
            Nutrition(24, "Nuts", 100, 10, 10, 10, 10, ""),
        )
    ))
    }
    LaunchedEffect(Unit) {
        viewModel.onEvent(MealLogEvent.SetupDemoMeal(testData))
    }
    //var currentBlockState by remember { mutableStateOf(BlockState.Closed) }
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
            /*LogBlockRow(
                title = it,
                data = data[it] ?: emptyList(),
                state = blockState,
                onOpen = {

                },
                onRemove = { title, index ->
                    onRemove(
                        title, index
                    )
                }
            )*/
            LogBlockRow(
                title = it.name.replaceFirstChar { it.uppercase() },
                //data = data[it.name] ?: emptyList(),
                //data = consumedMeals[it.name] ?: emptyList(),
                data = state.consumedMeals[it.name] ?: emptyList(),
                //state = blockState.currentBlockState,
                state = state.currentBlockState,
                onOpen = {
                    /*currentBlockState = if (currentBlockState == it) {
                        BlockState.Closed
                    } else {
                        it
                    }*/
                    viewModel.onEvent(MealLogEvent.BlockStateChanged(it))
                },
                onRemove = { title, index ->
                    /*onRemove(
                        title, index
                    )*/
                    viewModel.onEvent(MealLogEvent.MealRemoved(title, index))
                }
            )
        }
        Spacer(modifier = Modifier.padding(4.dp))
    }
}



/*@Composable
@Preview
fun LogBlockPreview() {
    var testData by remember { mutableStateOf(mapOf<String, List<Nutrition>>(
        "Breakfast" to listOf(
            Nutrition(1, "Egg", 100, 10, 10, 10, 10, ""),
            Nutrition(2, "Bread", 100, 10, 10, 10, 10, ""),
            Nutrition(3, "Butter", 100, 10, 10, 10, 10, ""),
            Nutrition(4, "Milk", 100, 10, 10, 10, 10, ""),
        ),
        "Lunch" to listOf(
            Nutrition(5, "Rice", 100, 10, 10, 10, 10, ""),
            Nutrition(6, "Chicken", 100, 10, 10, 10, 10, ""),
            Nutrition(7, "Salad", 100, 10, 10, 10, 10, ""),
            Nutrition(8, "Water", 100, 10, 10, 10, 10, ""),
        ),
        "Dinner" to listOf(
            Nutrition(9, "Pasta", 100, 10, 10, 10, 10, ""),
            Nutrition(10, "Fish", 100, 10, 10, 10, 10, ""),
            Nutrition(11, "Soup", 100, 10, 10, 10, 10, ""),
            Nutrition(12, "Juice", 100, 10, 10, 10, 10, ""),
        ),
        "Morning Snack" to listOf(
            Nutrition(13, "Apple", 100, 10, 10, 10, 10, ""),
            Nutrition(14, "Banana", 100, 10, 10, 10, 10, ""),
            Nutrition(15, "Orange", 100, 10, 10, 10, 10, ""),
            Nutrition(16, "Pear", 100, 10, 10, 10, 10, ""),
        ),
        "Afternoon Snack" to listOf(
            Nutrition(17, "Chocolate", 100, 10, 10, 10, 10, ""),
            Nutrition(18, "Candy", 100, 10, 10, 10, 10, ""),
            Nutrition(19, "Cookie", 100, 10, 10, 10, 10, ""),
            Nutrition(20, "Ice Cream", 100, 10, 10, 10, 10, ""),
        ),
        "Evening Snack" to listOf(
            Nutrition(21, "Chips", 100, 10, 10, 10, 10, ""),
            Nutrition(22, "Popcorn", 100, 10, 10, 10, 10, ""),
            Nutrition(23, "Pretzel", 100, 10, 10, 10, 10, ""),
            Nutrition(24, "Nuts", 100, 10, 10, 10, 10, ""),
        )
    ))
    }
    LogBlock(
        modifier = Modifier
            .safeDrawingPadding(),
        data = testData
    ) { title, index ->
        testData[title]?.let {
            val list = it.toMutableList()
            list.removeAt(index)
            testData = testData.toMutableMap().apply { this[title] = list }
        }
    }
}*/