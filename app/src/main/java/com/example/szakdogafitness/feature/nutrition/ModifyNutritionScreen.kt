package com.example.szakdogafitness.feature.nutrition

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.szakdogafitness.ui.common.appbars.BackNavWithTitle
import com.example.szakdogafitness.ui.common.appbars.TopBar
import com.example.szakdogafitness.ui.common.nutrition.SearchNutritionCard
import com.example.szakdogafitness.ui.common.nutrition.SearchNutritionLabels
import com.example.szakdogafitness.presentation.navigation.AppBarEvent
import com.example.szakdogafitness.presentation.navigation.AppBarViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyNutritionScreen(
    modifier: Modifier = Modifier,
    mealTypesViewModel: MealTypesViewModel = hiltViewModel(),
    appBarVIewModel: AppBarViewModel = hiltViewModel(),
    onItemClicked: (String) -> Unit,
    onBack: () -> Unit,
) {
    val mealTypesState by mealTypesViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        appBarVIewModel.onEvent(
            AppBarEvent.TopBarComposableChanged(
                {
                    BackNavWithTitle(title = "Modify Nutritions") {
                        onBack()
                    }
                }
            )
        )
        mealTypesViewModel.onEvent(MealTypesEvent.FetchNutritions)
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
                                        mealTypesViewModel.onEvent(MealTypesEvent.SortByCaloriesDesc)
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
                    items(mealTypesState.selectedMealType.size) { index ->
                        val mealType = mealTypesState.selectedMealType[index]
                        SearchNutritionCard(
                            data = mealType,
                        ) {
                            onItemClicked(mealType.foodId.toString())
                        }
                    }
                    item{
                        SearchNutritionCard(
                            data = Nutrition(
                                image = "https://www.pngall.com/wp-content/uploads/10/Plus-Symbol-Vector-PNG-Cutout.png",
                                foodName = "Add New Nutrition",
                            )
                        ) {
                            onItemClicked("add")
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
                }
            }
        }
    }
}

@Composable
fun ManageNutritionItemScreen(
    modifier: Modifier = Modifier,
    mealTypesViewModel: MealTypesViewModel = hiltViewModel(),
    appBarVIewModel: AppBarViewModel = hiltViewModel(),
    id: String,
    onBack: () -> Unit,
){

    val state by mealTypesViewModel.state.collectAsStateWithLifecycle()
    val painter = rememberAsyncImagePainter(
        state.selectedNutrition?.image
    )
    val painterState by painter.state.collectAsState()
    LaunchedEffect(Unit) {
        //mealTypesViewModel.onEvent(MealTypesEvent.FetchNutritionDetail(id))
        //mealTypesViewModel.onEvent(MealTypesEvent.FetchNutritions)
        appBarVIewModel.onEvent(
            AppBarEvent.TopBarComposableChanged(
                {
                    BackNavWithTitle(
                        title = if (id == "add") "Add New Nutrition" else "Modify Nutrition"
                    ) {
                        onBack()
                    }
                }
            )
        )
    }
    LaunchedEffect(state.nutritions) {
        val newId = (1..10000).random().takeIf { id -> state.nutritions.none { it.foodId == id } } ?: 0
        mealTypesViewModel.onEvent(
            MealTypesEvent.NutritionSelected(state.nutritions.find {
                it.foodId == id.toIntOrNull()
            } ?: Nutrition(
                image = "https://static.vecteezy.com/system/resources/previews/000/463/565/original/healthy-food-clipart-vector.jpg",
                foodId = newId
            )))
    }
    Scaffold (
        bottomBar = {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                OutlinedButton(
                    onClick = {
                        mealTypesViewModel.onEvent(MealTypesEvent.NutritionSelected(Nutrition()))
                        onBack()
                    },
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(
                    onClick = {
                        TODO("DELETE")
                        onBack()
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors().copy(
                        containerColor = Color(0xFFBB0000)
                    )
                ) {
                    Text("Delete")
                }
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedButton(
                    onClick = {
                        when(id){
                            "add" -> mealTypesViewModel.onEvent(MealTypesEvent.AddNutrition)
                            else -> mealTypesViewModel.onEvent(MealTypesEvent.UpdateNutrition)
                        }
                        onBack()
                    },
                    shape = RoundedCornerShape(8.dp),
                    enabled = state.selectedNutrition?.let{
                        it.foodName.isNotEmpty() && it.servingSize > 0 && it.calories > 0 && it.protein > 0 && it.foodId > 0 && it.mealType.isNotEmpty() && it.image.isNotEmpty()
                    } ?: false
                ) {
                    Text("Save")
                }
            }
        },
        modifier = modifier
            .fillMaxSize(),
    ){ padding ->
        if (state.isLoading){
            Text("Loading")
        }else {

            Column (
                modifier = modifier
                    .padding(padding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ){
                    if (painterState is AsyncImagePainter.State.Success){
                        Image(
                            painter = painter,
                            contentDescription = "Nutrition Image",
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(1f)
                                .height(170.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .weight(1f)
                                .height(170.dp)
                        )
                    }
                    MealTypeSelector(
                        modifier = Modifier
                            .weight(1.5f)
                            .padding(8.dp),
                        selectedMealTypes = state.selectedNutrition?.mealType ?: ""
                    ) {
                        if (state.selectedNutrition?.mealType?.contains(it) == true) {
                            val updatedMealType = state.selectedNutrition?.mealType
                                ?.split(", ")
                                ?.filter { type -> type != it }
                                ?.joinToString(", ")
                                ?.trimEnd(',') ?: ""

                            mealTypesViewModel.onEvent(
                                MealTypesEvent.EditSelectedNutrition(
                                    state.selectedNutrition?.copy(mealType = updatedMealType) ?: Nutrition()
                                )
                            )
                        } else {
                            val updatedMealType = listOfNotNull(state.selectedNutrition?.mealType?.takeIf { it.isNotEmpty() }, it)
                                .joinToString(", ")

                            mealTypesViewModel.onEvent(
                                MealTypesEvent.EditSelectedNutrition(
                                    state.selectedNutrition?.copy(mealType = updatedMealType) ?: Nutrition()
                                )
                            )
                        }

                    }
                }
                NormalTextField(
                    modifier = Modifier
                        .fillMaxWidth(0.95f),
                    value = state.selectedNutrition?.foodName ?: "",
                    onValueChange = {
                        mealTypesViewModel.onEvent(
                            MealTypesEvent.EditSelectedNutrition(
                                state.selectedNutrition?.copy(foodName = it) ?: Nutrition()
                            )
                        )
                    },
                    onDone = {},
                    label = "Food Name",
                    leadingIcon = null,
                    trailingIcon = null,
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                    )
                )
                Row (
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                ) {
                    NormalTextField(
                        modifier = Modifier
                            .weight(1f),
                        value = state.selectedNutrition?.servingSize.toString(),
                        onValueChange = {
                            mealTypesViewModel.onEvent(
                                MealTypesEvent.EditSelectedNutrition(
                                    state.selectedNutrition?.copy(servingSize = it.toIntOrNull() ?: 0) ?: Nutrition()
                                )
                            )
                        },
                        onDone = {},
                        label = "Serving size",
                        leadingIcon = null,
                        trailingIcon = null,
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    NormalTextField(
                        modifier = Modifier
                            .weight(1f),
                        value = state.selectedNutrition?.calories.toString(),
                        onValueChange = {
                            mealTypesViewModel.onEvent(
                                MealTypesEvent.EditSelectedNutrition(
                                    state.selectedNutrition?.copy(calories = it.toIntOrNull() ?: 0) ?: Nutrition()
                                )
                            )
                        },
                        onDone = {},
                        label = "Calories",
                        leadingIcon = null,
                        trailingIcon = null,
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                        )
                    )
                }
                Row (
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                ) {
                    NormalTextField(
                        modifier = Modifier
                            .weight(1f),
                        value = state.selectedNutrition?.carbs.toString(),
                        onValueChange = {
                            mealTypesViewModel.onEvent(
                                MealTypesEvent.EditSelectedNutrition(
                                    state.selectedNutrition?.copy(carbs = it.toIntOrNull() ?: 0) ?: Nutrition()
                                )
                            )
                        },
                        onDone = {},
                        label = "Carbs",
                        leadingIcon = null,
                        trailingIcon = null,
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    NormalTextField(
                        modifier = Modifier
                            .weight(1f),
                        value = state.selectedNutrition?.fat.toString(),
                        onValueChange = {
                            mealTypesViewModel.onEvent(
                                MealTypesEvent.EditSelectedNutrition(
                                    state.selectedNutrition?.copy(fat = it.toIntOrNull() ?: 0) ?: Nutrition()
                                )
                            )
                        },
                        onDone = {},
                        label = "Fat",
                        leadingIcon = null,
                        trailingIcon = null,
                        readOnly = true,
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                        )
                    )
                }
                Row (
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                ) {
                    NormalTextField(
                        modifier = Modifier
                            .weight(1f),
                        value = state.selectedNutrition?.protein.toString(),
                        onValueChange = {
                            mealTypesViewModel.onEvent(
                                MealTypesEvent.EditSelectedNutrition(
                                    state.selectedNutrition?.copy(protein = it.toIntOrNull() ?: 0) ?: Nutrition()
                                )
                            )
                        },
                        onDone = {},
                        label = "Protein",
                        leadingIcon = null,
                        trailingIcon = null,
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    NormalTextField(
                        modifier = Modifier
                            .weight(1f),
                        value = state.selectedNutrition?.foodId.toString(),
                        onValueChange = {},
                        onDone = {},
                        label = "Food ID",
                        leadingIcon = null,
                        trailingIcon = {
                            if (id == "add") {
                                IconButton(
                                    onClick = {
                                        //val newId = (1..10000).find { id -> state.nutritions.none {it.foodId == id} } ?: 0 // Might be good too.
                                        val newId = (1..10000).random().takeIf { id -> state.nutritions.none { it.foodId == id } } ?: 0
                                        mealTypesViewModel.onEvent(
                                            MealTypesEvent.EditSelectedNutrition(
                                                state.selectedNutrition?.copy(foodId = newId) ?: Nutrition()
                                            )
                                        )
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Refresh",
                                    )
                                }
                            }
                        },
                        readOnly = true,
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ManageScreen(
    modifier: Modifier = Modifier,
) {
    var data = Nutrition(
        foodName = "Test Food",
        image = "https://fitfoodiefinds.com/wp-content/uploads/2021/01/ip-oatmealsq.jpg",
        mealType = "bf, l, d",
        servingSize = 100,
        calories = 100,
        protein = 10,
        fat = 10,
        carbs = 10
    )
    val painter = rememberAsyncImagePainter(
        data.image
    )
    val painterState by painter.state.collectAsState()

    Column (
        modifier = modifier
            .fillMaxSize()
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ){
            if (painterState is AsyncImagePainter.State.Success){
                Image(
                    painter = painter,
                    contentDescription = "Nutrition Image",
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f)
                        .height(170.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                CircularProgressIndicator(
                    modifier = Modifier
                        .weight(1f)
                        .height(170.dp)
                )
            }
            MealTypeSelector(
                modifier = Modifier
                    .weight(1.5f)
                    .padding(8.dp),
                selectedMealTypes = data.mealType
            ) {
                if (data.mealType.contains(it)) {
                    data = data.copy(
                        mealType = data.mealType
                            .split(", ")
                            .filter { type -> type != it }
                            .joinToString(", ")
                            .trimEnd(',')
                    )
                } else {
                    //data = data.copy(mealType = if (data.mealType.isEmpty()) it else "${data.mealType}, $it")
                    data = data.copy(
                        mealType = listOf(data.mealType, it)
                            .filter { type -> type.isNotEmpty() }
                            .joinToString(", ")
                    )
                }
            }
        }
    }
}

@Composable
fun MealTypeSelector(
    modifier: Modifier = Modifier,
    selectedMealTypes: String,
    onMealTypeSelected: (String) -> Unit
) {
    Column (
        modifier = modifier
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column {
                Text(
                    text = "Main meals",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OutlinedButton(
                        onClick = {
                            onMealTypeSelected("bf")
                        },
                        modifier = Modifier,
                        contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
                        colors = ButtonDefaults.outlinedButtonColors().copy(
                            containerColor = if (selectedMealTypes.contains("bf")) Color.Green else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = "Breakfast"
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            onMealTypeSelected("l")
                        },
                        modifier = Modifier,
                        contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
                        colors = ButtonDefaults.outlinedButtonColors().copy(
                            containerColor = if (selectedMealTypes.contains("l")) Color.Green else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = "Lunch"
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            onMealTypeSelected("d")
                        },
                        modifier = Modifier,
                        contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
                        colors = ButtonDefaults.outlinedButtonColors().copy(
                            containerColor = if (selectedMealTypes.contains("d")) Color.Green else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = "Dinner"
                        )
                    }
                }
            }
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column {
                Text(
                    text = "Snacks",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OutlinedButton(
                        onClick = {
                            onMealTypeSelected("ms")
                        },
                        modifier = Modifier,
                        contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
                        colors = ButtonDefaults.outlinedButtonColors().copy(
                            containerColor = if (selectedMealTypes.contains("ms")) Color.Green else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = "Morning"
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            onMealTypeSelected("as")
                        },
                        modifier = Modifier,
                        contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
                        colors = ButtonDefaults.outlinedButtonColors().copy(
                            containerColor = if (selectedMealTypes.contains("as")) Color.Green else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = "Afternoon"
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            onMealTypeSelected("es")
                        },
                        modifier = Modifier,
                        contentPadding = PaddingValues(start = 8.dp, end = 8.dp),
                        colors = ButtonDefaults.outlinedButtonColors().copy(
                            containerColor = if (selectedMealTypes.contains("es")) Color.Green else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = "Evening"
                        )
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun PreviewManageScreen(){
    ManageScreen(
        modifier = Modifier
            .safeDrawingPadding()
    )
}