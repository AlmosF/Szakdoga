package com.example.szakdogafitness.feature.nutrition

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.szakdogafitness.domain.model.FoodAnalysisResult
import com.example.szakdogafitness.domain.model.Nutrition
import com.example.szakdogafitness.ui.common.nutrition.AddNutrientsBlock
import com.example.szakdogafitness.ui.common.nutrition.TargetKcalBlock
import com.example.szakdogafitness.ui.common.nutrition.logblock.LogBlock
import com.example.szakdogafitness.feature.CameraScan.CameraEvent
import com.example.szakdogafitness.feature.CameraScan.CameraViewModel
import com.example.szakdogafitness.feature.lifestyle.DailyDatasEvent
import com.example.szakdogafitness.feature.lifestyle.DailyDatasViewModel
import com.example.szakdogafitness.presentation.navigation.AppBarEvent
import com.example.szakdogafitness.presentation.navigation.AppBarViewModel
import com.example.szakdogafitness.ui.common.appbars.LifeStyleTitle
import com.example.szakdogafitness.ui.common.appbars.TopBar
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
    appBarViewModel: AppBarViewModel = hiltViewModel(),
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

    LaunchedEffect(Unit) {
        appBarViewModel.onEvent(
            AppBarEvent.TopBarComposableChanged(
                {
                    TopBar(
                        title = {
                            LifeStyleTitle(
                                title = dailyDatasState.lifeStyleTitle,
                                previousButtonEnabled = dailyDatasState.isPreviousDayAvailable,
                                onPreviousDayClick = {
                                    Log.d("RootNavigation", "onPreviousDayClick")
                                    dailyDatasViewModel.onEvent(DailyDatasEvent.PreviousDay)
                                },
                                nextButtonEnabled = dailyDatasState.isNextDayAvailable,
                                onNextDayClick = {
                                    Log.d("RootNavigation", "onNextDayClick")
                                    dailyDatasViewModel.onEvent(DailyDatasEvent.NextDay)
                                }
                            )
                        },
                    )
                }
            )
        )
    }

    LaunchedEffect(cameraState.image){
        capturedPhoto = cameraState.image?.asImageBitmap()
        cameraState.image?.let {
            generativeViewModel.onEvent(GenerativeEvent.SendPrompt(it))
        }
        if (cameraState.image != null) {
            bottomSheetVisible = true
        }
    }

    LaunchedEffect(generativeState.response) {
        mealTypesViewModel.onEvent(MealTypesEvent.NutritionSelected(
            if (generativeState.response.foodName != "null") {
                generativeState.response.let {
                    val newId = (1..10000).random()
                        .takeIf { id -> mealTypesState.nutritions.none { it.foodId == id } } ?: 0
                    val nutrition = Nutrition(
                        foodId = newId,
                        foodName = it.foodName,
                        calories = it.kcal,
                        protein = it.protein,
                        carbs = it.carb,
                        fat = it.fat,
                        mealType = "-",
                        image = "https://static.vecteezy.com/system/resources/previews/000/463/565/original/healthy-food-clipart-vector.jpg",
                        servingSize = it.servingSize
                    )
                    nutrition
                }
            } else {
                Nutrition()
            }
        ))
    }

    LaunchedEffect(mealTypesState.selectedNutrition) {
        mealTypesState.selectedNutrition?.let {
            Log.d("NutritionScreen", "Selected Nutrition: ${it.foodName}")
            if (it.servingSize == 0 || (it.fat == 0 && it.carbs == 0 && it.protein == 0)) {
                Log.d("NutritionScreen", "Nutrition is incomplete")
                mealTypesViewModel.onEvent(MealTypesEvent.NutritionSelected(Nutrition()))
            } else {
                if (it.foodName.isNotBlank() && it.foodName != "null" && mealTypesState.nutritions.none { nutrition -> nutrition.foodName == it.foodName }) {
                    //mealTypesViewModel.onEvent(MealTypesEvent.AddNutrition)
                    Log.d("NutritionScreen", mealTypesState.nutritions.toString())
                    Log.d("NutritionScreen", "Adding new nutrition: ${it.foodName}")
                    mealTypesViewModel.onEvent(MealTypesEvent.AddNutrition)
                } else if (it.foodName != "null") {
                    Log.d("NutritionScreen", "Nutrition already exists")
                    mealTypesState.nutritions.find { nutrition -> nutrition.foodName == it.foodName }?.let {
                        mealTypesViewModel.onEvent(MealTypesEvent.EditSelectedNutrition(it))
                    }
                    Log.d("NutritionScreen", mealTypesState.selectedNutrition.toString())
                }
            }
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
                //targetKcal += it
                dailyDatasViewModel.onEvent(
                    DailyDatasEvent.UpdateTargetKcal(
                        dailyDatasState.currentDailyData.targetKcal + it,
                        dailyDatasState.currentDailyData.date
                    )
                )
            },
            onDecrease = {
                //targetKcal -= it
                if (dailyDatasState.currentDailyData.targetKcal - it <= 0) return@TargetKcalBlock
                dailyDatasViewModel.onEvent(
                    DailyDatasEvent.UpdateTargetKcal(
                        dailyDatasState.currentDailyData.targetKcal - it,
                        dailyDatasState.currentDailyData.date
                    )
                )
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
                                text = it.foodName,
                                modifier = Modifier
                                    .padding(top = 4.dp),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Column (
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = "${it.kcal} kcal",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Total portion",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${it.servingSize} g",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "\t - ${it.protein} g protein",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "\t - ${it.carb} g carbs",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "\t - ${it.fat} g fat",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            MealTypeSelector(
                                selectedMealType = mealTypesState.selectedMealTitle,
                                enabled = mealTypesState.selectedNutrition != Nutrition()
                            ) {
                                mealTypesViewModel.onEvent(MealTypesEvent.MealTypeSelected(it))
                            }
                            OutlinedButton(
                                onClick = {
                                    Log.d("NutritionScreen", "Consume Meal for: ${mealTypesState.selectedMealTitle}: ${mealTypesState.selectedNutrition}")
                                    dailyDatasViewModel.onEvent(
                                        DailyDatasEvent.AddNutrition(
                                            nutritionID = mealTypesState.selectedNutrition!!.foodId,
                                            mealType = mealTypesState.selectedMealTitle
                                        )
                                    )
                                    coroutineScope.launch {
                                        sheetState.hide()
                                        bottomSheetVisible = false
                                        cameraViewModel.onEvent(CameraEvent.ResetState)
                                        generativeViewModel.onEvent(GenerativeEvent.ResetState)
                                    }
                                },
                                enabled = mealTypesState.selectedNutrition != Nutrition() &&
                                        mealTypesState.selectedMealTitle != "bf"
                                ,
                                modifier = Modifier.fillMaxWidth(0.5f)
                            ) {
                                Text(
                                    text = "Consume Meal"
                                )
                            }
                        }
                        generativeState.wrongResponse.takeIf { it }?.let {
                            Text(
                                text = "Sorry I couldn't recognize the food",
                                color = Color.Red,
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
fun MealTypeSelector(
    selectedMealType: String,
    enabled: Boolean,
    onMealTypeSelected: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    val mealTypes = mapOf(
        "bf" to "Breakfast",
        "ms" to "Morning_Snack",
        "l" to "Lunch",
        "as" to "Afternoon_Snack",
        "d" to "Dinner",
        "es" to "Evening_Snack"
    )

    Box {
        Text(
            text = mealTypes.entries.find { it.value == selectedMealType }?.value?.replace("_", " ") ?: "Select Meal Type",
            modifier = Modifier
                .padding(16.dp)
                .clickable {
                    if (enabled)
                        expanded = true
                }
                .fillMaxWidth(0.5f)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(8.dp),
            textAlign = TextAlign.Center
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            mealTypes.forEach { (mealType, mealTypeShort) ->
                DropdownMenuItem(
                    text = { Text(
                        text = mealTypeShort.replace("_", " "),
                    ) },
                    onClick = {
                        onMealTypeSelected(mealTypeShort)
                        expanded = false
                    }
                )
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