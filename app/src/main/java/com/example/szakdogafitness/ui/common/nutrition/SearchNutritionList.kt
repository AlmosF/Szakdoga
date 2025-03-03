package com.example.szakdogafitness.ui.common.nutrition

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.szakdogafitness.domain.model.Nutrition


@Composable
fun SearchNutritionList(
    modifier: Modifier = Modifier,
    list: List<Nutrition>,
    onNutritionSelected: (Nutrition) -> Unit
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
    ) {
        items(list.size) {
            SearchNutritionCard(
                data = list[it],
            ) {
                onNutritionSelected(it)
            }
        }
    }

}