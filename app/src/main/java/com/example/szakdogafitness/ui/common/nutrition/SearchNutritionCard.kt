package com.example.szakdogafitness.ui.common.nutrition

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.szakdogafitness.domain.model.Nutrition
import com.example.szakdogafitness.R

@Composable
fun SearchNutritionCard(
    data: Nutrition,
    onAddClick: (Nutrition) -> Unit
) {
    Card (
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .size(150.dp)
            .clip(RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = { onAddClick(data) }
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = data.foodName)
        }
    }
}

@Composable
@Preview
fun SearchNutritionCardPreview(){
    SearchNutritionCard(
        data = Nutrition(
            foodId = 1,
            foodName = "Chicken",
            servingSize = 100,
            calories = 200,
            protein = 20,
            carbs = 0,
            fat = 10,
            image = "https://www.google.com"
        ),
        onAddClick = {}
    )
}