package com.example.szakdogafitness.feature.nutrition

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.szakdogafitness.domain.model.Nutrition
import com.example.szakdogafitness.ui.common.NormalTextField
import com.example.szakdogafitness.ui.common.appbars.TopBar
import com.example.szakdogafitness.ui.common.nutrition.SearchNutritionCard
import com.example.szakdogafitness.ui.common.nutrition.SearchNutritionLabels


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
    modifier: Modifier = Modifier
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var search by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopBar(
                title = {
                    Column {
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
                            value = search,
                            onValueChange = {
                                search = it
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
        var filteredSearch = list.filter {
            it.foodName.contains(search, ignoreCase = true)
        }
        Column (
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier
                    //.padding(padding)
                    .fillMaxWidth(0.95f),
                horizontalArrangement = Arrangement.Center
            ) {
                items(filteredSearch.size) {
                    SearchNutritionCard(
                        data = filteredSearch[it],
                    ) {

                    }
                }
            }
        }

    }
}


val list = List(51) { id ->
    Nutrition(
        foodId = id,
        foodName = "Example food $id",
        servingSize = 100,
        calories = 52,
        protein = 30,
        carbs = 30,
        fat = 40,
        image = "https://www.mariefranceasia.com/wp-content/uploads/sites/7/2018/02/breakfast.jpg"
    )
}


val COLLAPSED_TOP_BAR_HEIGHT = 56.dp
val EXPANDED_TOP_BAR_HEIGHT = 200.dp
@Composable
private fun ExpandedTopBar() {
    Column {
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

    }
}
@Composable
private fun CollapsedTopBar(modifier: Modifier = Modifier, isCollapsed: Boolean) {
    val color: Color by animateColorAsState(
        if (isCollapsed) Color.Red else Color.Green
    )
    Row (
        modifier = modifier
            .fillMaxWidth()
            .height(COLLAPSED_TOP_BAR_HEIGHT)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AnimatedVisibility(visible = true) {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Filter",
                    modifier = Modifier
                        .size(32.dp)
                )
            }
        }
        AnimatedVisibility(visible = true) {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Filled.FilterAlt,
                    contentDescription = "Filter",
                    modifier = Modifier
                        .size(32.dp)
                )
            }
        }
    }
}



@Composable
@Preview
fun SearchNutritionScreenPreview() {
    SearchNutritionScreen()
}