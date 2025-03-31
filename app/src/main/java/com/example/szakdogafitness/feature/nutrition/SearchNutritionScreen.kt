package com.example.szakdogafitness.feature.nutrition

import android.R.attr.onClick
import android.R.attr.text
import android.R.attr.top
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
import com.example.szakdogafitness.viewmodel.DailyDatasEvent
import com.example.szakdogafitness.viewmodel.DailyDatasViewModel
import com.example.szakdogafitness.viewmodel.MealTypesEvent
import com.example.szakdogafitness.viewmodel.MealTypesViewModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch


/*@Composable
fun SearchNutritionScreen(
    modifier: Modifier = Modifier
) {

    //val listState = rememberLazyListState()
    val listState = rememberLazyGridState()
    val isCollapsed: Boolean by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }
    Scaffold(
        topBar = {
            if (isCollapsed){
                CollapsedTopBar(
                    isCollapsed = isCollapsed
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
    ) { padding ->
        Column (
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier
                    .fillMaxWidth(0.95f),
                state = listState,
                horizontalArrangement = Arrangement.Center
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ExpandedTopBar()
                    }
                }
                items(list.size) {
                    SearchNutritionCard(
                        data = list[it],

                        ) {

                    }
                }
            }
        }
    }

    *//*Column ( //Constraint Layouttal rossz, mert az utolso elemet nem lehet látni (?)
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        var selectedLabel by remember { mutableStateOf("Breakfast") }

        SearchNutritionLabels (
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(shape = ButtonDefaults.shape),
            selectedLabel = selectedLabel,
        ) {
            selectedLabel = it
        }

        NormalTextField(
            modifier = Modifier
                .fillMaxWidth(0.95f),
            value = "",
            onValueChange = {},
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
                Icon(
                    imageVector = Icons.Filled.FilterAlt,
                    contentDescription = "Filter",
                    modifier = Modifier
                        .size(32.dp)
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
        SearchNutritionList(
            modifier = Modifier
                .fillMaxWidth(0.95f),
            list = list
        ) { }
    }*//*

}*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchNutritionScreen(
    modifier: Modifier = Modifier,
    mealTypesViewModel: MealTypesViewModel = hiltViewModel(),
    dailyDatasViewModel: DailyDatasViewModel = hiltViewModel(),
) {

    val mealTypesState by mealTypesViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        mealTypesViewModel.onEvent(MealTypesEvent.MealTypeSelected("bf"))
        //mealTypesViewModel.onEvent(MealTypesEvent.LoadMealTypes)
        //mealTypesViewModel.onEvent(MealTypesEvent.FetchNutritions)
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
                                IconButton(
                                    onClick = {
                                        mealTypesViewModel.onEvent(MealTypesEvent.SortByCalories)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.FilterAlt,
                                        contentDescription = "Filter",
                                        modifier = Modifier
                                            .size(32.dp)
                                    )
                                }
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
                    /*items(mealTypesState.selectedMealType.size) { index ->
                        val mealType = mealTypesState.selectedMealType[index]
                        SearchNutritionCard(
                            data = mealType,
                        ) {
                            mealTypesViewModel.onEvent(MealTypesEvent.NutritionSelected(mealType))
                            mealTypesViewModel.onEvent(MealTypesEvent.ChangeDialogState)
                        }
                    }*/
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
                    mealTypeTitle = mealTypesState.selectedMealTitle
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
@Preview
fun SearchNutritionScreenPreview() {
    SearchNutritionScreen()
}