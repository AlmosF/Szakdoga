package com.example.szakdogafitness.feature.nutrition

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.example.szakdogafitness.domain.model.Nutrition
import com.example.szakdogafitness.ui.common.NormalTextField
import com.example.szakdogafitness.ui.common.appbars.TopBar
import com.example.szakdogafitness.ui.common.nutrition.SearchNutritionCard
import com.example.szakdogafitness.ui.common.nutrition.SearchNutritionLabels
import com.example.szakdogafitness.feature.lifestyle.DailyDatasEvent
import com.example.szakdogafitness.feature.lifestyle.DailyDatasViewModel
import com.example.szakdogafitness.presentation.navigation.AppBarEvent
import com.example.szakdogafitness.presentation.navigation.AppBarViewModel
import com.example.szakdogafitness.presentation.navigation.AppBarViewModel_Factory
import com.example.szakdogafitness.ui.common.appbars.BackNavWithTitle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchNutritionScreen(
    modifier: Modifier = Modifier,
    mealTypesViewModel: MealTypesViewModel = hiltViewModel(),
    dailyDatasViewModel: DailyDatasViewModel = hiltViewModel(),
    appBarViewModel: AppBarViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {

    val mealTypesState by mealTypesViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        appBarViewModel.onEvent(
            AppBarEvent.TopBarComposableChanged(
                {
                    BackNavWithTitle(title = "Search Nutrition") {
                        mealTypesViewModel.onEvent(
                            MealTypesEvent.MealTypeSelected("bf")
                        )
                        onBack()
                    }
                }
            )
        )
        mealTypesViewModel.onEvent(MealTypesEvent.MealTypeSelected("bf"))
    }
    val labelsMap = mapOf(
        "bf" to "Breakfast",
        "ms" to "Morning_Snack",
        "l" to "Lunch",
        "as" to "Afternoon_Snack",
        "d" to "Dinner",
        "es" to "Evening_Snack"
    )

    LaunchedEffect(mealTypesState.nutritions) {
        Log.d("SearchNutritionScreen", "nutritions: ${mealTypesState.nutritions}")
    }

    LaunchedEffect(mealTypesState.mealTypes) {
        Log.d("SearchNutritionScreen", "mealTypesState: ${mealTypesState.mealTypes}")
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            TopBar(
                title = {
                    Column {
                        SearchNutritionLabels (
                            modifier = Modifier
                                .fillMaxWidth(0.95f)
                                .clip(shape = ButtonDefaults.shape),
                            selectedLabel = mealTypesState.selectedMealTitle,
                        ) { selectedType ->
                            mealTypesViewModel.onEvent(MealTypesEvent.MealTypeSelected(selectedType))
                        }
                        NormalTextField(
                            modifier = Modifier
                                .fillMaxWidth(0.95f),
                            value = mealTypesState.queryString,
                            onValueChange = {
                                mealTypesViewModel.onEvent(MealTypesEvent.SearchQuery(it))
                            },
                            onDone = {},
                            label = "Search",
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "Search",
                                    modifier = Modifier
                                        .size(32.dp)
                                )
                            },
                            trailingIcon = {
                                NutritionSorter(
                                    mealTypesViewModel = mealTypesViewModel
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(32.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                disabledContainerColor = MaterialTheme.colorScheme.surface,
                            )
                        )

                    }
                },
                scrollBehavior = scrollBehavior,
                expandedHeight = 120.dp
            )
        },
        modifier = Modifier
            .safeDrawingPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { padding ->
        Column (
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (mealTypesState.isLoading){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    Log.d("SearchNutritionScreen", "Loading")
                    Text(
                        text = "Loading...",
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = modifier
                        .fillMaxWidth(0.95f),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Log.d("SearchNutritionScreen", "selectedMealType: ${mealTypesState.selectedMealType}")
                    items(mealTypesState.selectedMealType.size) { index ->
                        val mealType = mealTypesState.selectedMealType[index]
                        SearchNutritionCard(
                            data = mealType,
                        ) {
                            mealTypesViewModel.onEvent(MealTypesEvent.NutritionSelected(mealType))
                            mealTypesViewModel.onEvent(MealTypesEvent.ChangeDialogState)
                        }
                    }
                }
            }
        }
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = false
        )
        val scope = rememberCoroutineScope()
        AnimatedVisibility(
            visible = mealTypesState.dialogOpen
        ) {
            ModalBottomSheet(
                onDismissRequest = {
                    mealTypesViewModel.onEvent(MealTypesEvent.ChangeDialogState)
                },
                sheetState = sheetState,
            ) {
                ModalBottomSheetContent(
                    nutrition = mealTypesState.selectedNutrition ?: Nutrition(),
                    //mealTypeTitle = mealTypesState.selectedMealTitle
                    mealTypeTitle = labelsMap[mealTypesState.selectedMealTitle] ?: "Unknown"
                ) {
                    scope.launch {
                        sheetState.hide()
                    }
                    mealTypesViewModel.onEvent(MealTypesEvent.ChangeDialogState)
                    Log.d("SearchNutritionScreen", "Add to meal: ${labelsMap[mealTypesState.selectedMealTitle]}")
                    dailyDatasViewModel.onEvent(
                        DailyDatasEvent.AddNutrition(
                            nutritionID = mealTypesState.selectedNutrition!!.foodId,
                            mealType = labelsMap[mealTypesState.selectedMealTitle]!!
                        )
                    )
                }
            }
        }

    }
}


@Composable
fun ModalBottomSheetContent(
    modifier: Modifier = Modifier,
    nutrition: Nutrition,
    mealTypeTitle: String,
    onAddToMeal: (Nutrition) -> Unit
) {
    val painter = rememberAsyncImagePainter(nutrition.image)
    val painterState by painter.state.collectAsState()
    Column (
        modifier = modifier
            //.fillMaxSize()
            .padding(16.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (painterState is AsyncImagePainter.State.Success){
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(300.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Text(
            text = nutrition.foodName,
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
                text = "${nutrition.calories} kcal",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Total portion",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${nutrition.servingSize} g",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "\t - ${nutrition.protein} g protein",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "\t - ${nutrition.carbs} g carbs",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "\t - ${nutrition.fat} g fat",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
        OutlinedButton(
            onClick = {
                onAddToMeal(nutrition)
            }
        ) {
            Text(
                text = "Add to ${mealTypeTitle.replace("_", " ").replaceFirstChar(Char::uppercase)}"
            )
        }
    }
}

@Composable
fun NutritionSorter(
    mealTypesViewModel: MealTypesViewModel = hiltViewModel(),
){
    var isSortMenuExpanded by remember { mutableStateOf(false) }
    val sortState by mealTypesViewModel.sortState.collectAsState()
    Box {
        IconButton(onClick = {
            isSortMenuExpanded = true
        }) {
            Icon(
                imageVector = Icons.Filled.FilterAlt,
                contentDescription = "Sort Options",
                modifier = Modifier.size(32.dp),
                tint = if (!sortState.sortedByDefault) Color.Red else Color.Black
            )
        }

        DropdownMenu(
            expanded = isSortMenuExpanded,
            onDismissRequest = { isSortMenuExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Default") },
                onClick = {
                    isSortMenuExpanded = false
                    mealTypesViewModel.onEvent(MealTypesEvent.SortByDefault)
                },
                modifier = Modifier.background(
                    if (sortState.sortedByDefault) Color.LightGray
                    else Color.Transparent
                )
            )
            HorizontalDivider()
            DropdownMenuItem(
                text = { Text("Calories ↑") },
                onClick = {
                    isSortMenuExpanded = false
                    mealTypesViewModel.onEvent(MealTypesEvent.SortByCaloriesAsc)
                },
                modifier = Modifier.background(
                    if (sortState.sortedByCalAsc) Color.LightGray
                    else Color.Transparent
                )
            )
            DropdownMenuItem(
                text = { Text("Calories ↓") },
                onClick = {
                    isSortMenuExpanded = false
                    mealTypesViewModel.onEvent(MealTypesEvent.SortByCaloriesDesc)
                },
                modifier = Modifier.background(
                    if (sortState.sortedByCalDesc) Color.LightGray
                    else Color.Transparent
                )
            )
            DropdownMenuItem(
                text = { Text("Fat ↑") },
                onClick = {
                    isSortMenuExpanded = false
                    mealTypesViewModel.onEvent(MealTypesEvent.SortByFatAsc)
                },
                modifier = Modifier.background(
                    if (sortState.sortedByFatAsc) Color.LightGray
                    else Color.Transparent
                )
            )
            DropdownMenuItem(
                text = { Text("Fat ↓") },
                onClick = {
                    isSortMenuExpanded = false
                    mealTypesViewModel.onEvent(MealTypesEvent.SortByFatDesc)
                },
                modifier = Modifier.background(
                    if (sortState.sortedByFatDesc) Color.LightGray
                    else Color.Transparent
                )
            )
            DropdownMenuItem(
                text = { Text("Carbs ↑") },
                onClick = {
                    isSortMenuExpanded = false
                    mealTypesViewModel.onEvent(MealTypesEvent.SortByCarbsAsc)
                },
                modifier = Modifier.background(
                    if (sortState.sortedByCarbsAsc) Color.LightGray
                    else Color.Transparent
                )
            )
            DropdownMenuItem(
                text = { Text("Carbs ↓") },
                onClick = {
                    isSortMenuExpanded = false
                    mealTypesViewModel.onEvent(MealTypesEvent.SortByCarbsDesc)
                },
                modifier = Modifier.background(
                    if (sortState.sortedByCarbsDesc) Color.LightGray
                    else Color.Transparent
                )
            )
            DropdownMenuItem(
                text = { Text("Protein ↑") },
                onClick = {
                    isSortMenuExpanded = false
                    mealTypesViewModel.onEvent(MealTypesEvent.SortByProteinAsc)
                },
                modifier = Modifier.background(
                    if (sortState.sortedByProteinAsc) Color.LightGray
                    else Color.Transparent
                )
            )
            DropdownMenuItem(
                text = { Text("Protein ↓") },
                onClick = {
                    isSortMenuExpanded = false
                    mealTypesViewModel.onEvent(MealTypesEvent.SortByProteinDesc)
                },
                modifier = Modifier.background(
                    if (sortState.sortedByProteinDesc) Color.LightGray
                    else Color.Transparent
                )
            )
        }
    }
}

@Composable
@Preview
fun SearchNutritionScreenPreview() {
    SearchNutritionScreen()
}