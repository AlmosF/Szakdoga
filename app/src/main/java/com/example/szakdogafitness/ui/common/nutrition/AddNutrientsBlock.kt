package com.example.szakdogafitness.ui.common.nutrition

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddNutrientsBlock(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color(0xFFE8E8E8), shape = RoundedCornerShape(16.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        Text(
            text = "Add nutrients",
            modifier = Modifier.Companion,
            fontSize = 20.sp,
            fontWeight = FontWeight.Companion.Bold
        )
        Row(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NutritionIcons(
                modifier = Modifier.Companion,
                icon = Icons.Default.CameraAlt,
                title = "AI"
            ) {}
            NutritionIcons(
                modifier = Modifier.Companion,
                icon = Icons.Default.Fastfood,
                title = "Search"
            ) {}
            NutritionIcons(
                modifier = Modifier.Companion,
                icon = Icons.Default.QrCode,
                title = "Barcode"
            ) {}

        }
    }
}

@Composable
fun NutritionIcons(
    modifier: Modifier = Modifier.Companion,
    icon: ImageVector,
    title: String,
    onClick: () -> Unit = {}
){
    Column(
        modifier = modifier
            .background(
                Color(0xFFBDBDBD).copy(0.5f),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
            )
            .sizeIn(maxWidth = 100.dp, maxHeight = 100.dp)
            .fillMaxSize()
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.Companion.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.Companion
                .size(50.dp)
                .padding(top = 8.dp)
        )
        Text(
            text = title,
            modifier = Modifier.Companion
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp, top = 4.dp),
            fontSize = 15.sp
        )
    }
}

@Composable
@Preview
fun NutritionIconsPreview() {
    NutritionIcons(
        icon = Icons.Default.CameraAlt,
        title = "Scan for AI"
    )
}

@Composable
@Preview
fun AddNutrientsBlockPreview() {
    AddNutrientsBlock()
}