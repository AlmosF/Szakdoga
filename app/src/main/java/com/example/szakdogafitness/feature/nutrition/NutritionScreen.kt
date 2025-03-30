package com.example.szakdogafitness.feature.nutrition

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.szakdogafitness.domain.model.FoodAnalysisResult
import com.example.szakdogafitness.ui.common.nutrition.AddNutrientsBlock
import com.example.szakdogafitness.ui.common.nutrition.TargetKcalBlock
import com.example.szakdogafitness.ui.common.nutrition.logblock.LogBlock
import com.example.szakdogafitness.viewmodel.CameraEvent
import com.example.szakdogafitness.viewmodel.CameraViewModel
import com.example.szakdogafitness.viewmodel.DailyDatasEvent
import com.example.szakdogafitness.viewmodel.DailyDatasViewModel
import com.example.szakdogafitness.viewmodel.GenerativeEvent
import com.example.szakdogafitness.viewmodel.GenerativeViewModel
import com.example.szakdogafitness.viewmodel.MealTypesEvent
import com.example.szakdogafitness.viewmodel.MealTypesViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionScreen(
    modifier: Modifier = Modifier,
    //mainViewModel: MainViewModel = hiltViewModel(),
    dailyDatasViewModel: DailyDatasViewModel = hiltViewModel(),
    mealTypesViewModel: MealTypesViewModel = hiltViewModel(),
    cameraViewModel: CameraViewModel = hiltViewModel(),
    generativeViewModel: GenerativeViewModel = hiltViewModel(),
    onAINavigate: () -> Unit,
    onSearchNavigate: () -> Unit,
    onBarcodeNavigate: () -> Unit
) {

    var bottomSheetVisible by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    val cameraState by cameraViewModel.state.collectAsStateWithLifecycle()
    val mealTypesState by mealTypesViewModel.state.collectAsStateWithLifecycle()
    //val mainState by mainViewModel.state.collectAsStateWithLifecycle()
    val dailyDatasState by dailyDatasViewModel.state.collectAsStateWithLifecycle()
    val generativeState by generativeViewModel.state.collectAsStateWithLifecycle()
    var capturedPhoto by remember { mutableStateOf(cameraState.image?.asImageBitmap()) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(cameraState.image){
        capturedPhoto = cameraState.image?.asImageBitmap()
        cameraState.image?.let {
            generativeViewModel.onEvent(GenerativeEvent.SendPrompt(it))
        }
        if (cameraState.image != null) {
            bottomSheetVisible = true
        }
    }

    /*LaunchedEffect(Unit) {
        mealTypesViewModel.onEvent(MealTypesEvent.FetchNutritions)
    }*/

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
        var targetKcal by remember { mutableStateOf(1500) }
        var maxKcal by remember { mutableStateOf(2500) }
        val nutritions = mealTypesState.nutritions
        val foodIntakeList = dailyDatasState.currentDailyData.foodIntakeM.values.flatten().mapNotNull { foodId ->
            nutritions.find { it.foodId == foodId }
        }
        val totalCalories = foodIntakeList.sumOf { it.calories }
        TargetKcalBlock(
            modifier = Modifier
                .fillMaxWidth(0.9f),
            kcal = totalCalories,
            targetKcal = dailyDatasState.currentDailyData.targetKcal,
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
            //data = mainState.currentDailyData.foodIntakeM, //fixme("Workingone - With MAINVM")
            data = dailyDatasState.currentDailyData.foodIntakeM,
            mealTypesViewModel = mealTypesViewModel
        ) { nutritionID, mealType ->
            dailyDatasViewModel.onEvent(DailyDatasEvent.ReplenishNutrition(
                nutritionID = nutritionID,
                mealType = mealType,
                date = dailyDatasState.currentDailyData.date
            ))
        }
        if (bottomSheetVisible){
            ModalBottomSheet(
                onDismissRequest = {
                    coroutineScope.launch {
                        sheetState.hide()
                        bottomSheetVisible = false
                        cameraViewModel.onEvent(CameraEvent.ResetState)
                        generativeViewModel.onEvent(GenerativeEvent.ResetState)
                    }
                },
                sheetState = sheetState,
            ) {
                Column (
                    modifier = modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    capturedPhoto?.let {
                        Image(
                            bitmap = it,
                            contentDescription = "Captured Image",
                            modifier = Modifier
                                .size(200.dp)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                    if (generativeState.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        generativeState.error.takeIf { it.isNotBlank() }?.let {
                            Text(
                                text = it,
                                color = Color.Red,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        generativeState.response.takeIf { it != FoodAnalysisResult() }?.let {
                            Text(
                                text = it.toString(),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
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