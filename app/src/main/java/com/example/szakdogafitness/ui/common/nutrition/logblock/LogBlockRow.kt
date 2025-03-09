package com.example.szakdogafitness.ui.common.nutrition.logblock

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.example.szakdogafitness.domain.model.Nutrition


@Composable
fun LogBlockRow(
    modifier : Modifier = Modifier,
    title: String,
    data: List<Nutrition>,
    onOpen: () -> Unit,
    state: BlockState = BlockState.Closed,
    onRemove: (String, Int) -> Unit
) {
    var isOpened = state.name == title.replace(" ", "")
    val value = data.sumOf { it.calories }

    LaunchedEffect(data, isOpened) {
        if (data.isEmpty() && isOpened) {
            onOpen()
        }
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            RowValue(
                value = value
            )
            Spacer(modifier = Modifier.width(30.dp))
            Text(
                text = title.replace("_", " "),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {
                    onOpen()
                },
                enabled = !data.isEmpty()
            ) {
                Icon(
                    imageVector = if (isOpened) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Open"
                )
            }
        }
        AnimatedVisibility(
            visible = isOpened
        ) {
            Row (
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.width(86.dp))
                Column {
                    data.forEachIndexed { index, nutrition ->
                        RowDetailRow(
                            nutrition = nutrition
                        ) {
                            onRemove(
                                title,
                                index
                            )
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun RowDetailRow(
    modifier: Modifier = Modifier,
    nutrition: Nutrition,
    onRemove: () -> Unit
){
    val painter = rememberAsyncImagePainter(
        if (nutrition.image.isBlank()) "https://wepublishnews.com/wp-content/uploads/2021/05/Johnny-Depp-Net-Worth.jpg" else nutrition.image
    )
    Row (
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painter,
            contentDescription = "Food image",
            modifier = Modifier
                .clip(shape = RoundedCornerShape(16.dp))
                .size(40.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = nutrition.foodName
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = {
                onRemove()
            }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Remove",
                tint = Color.Red
            )
        }
    }
}

@Composable
fun RowValue(
    value: Int
) {
    Column (
        modifier = Modifier
            .background(color = Color.LightGray, shape = CircleShape)
            .padding(4.dp)
            .then(Modifier.size(50.dp))
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = value.toString()
        )
        Text(
            text = "kcal",
        )
    }
}



@Composable
@Preview
fun LogBlockRowPreview() {
    
    LogBlockRow(
        modifier = Modifier
            .safeDrawingPadding(),
        title = "Breakfast",
        data = listOf(
            Nutrition(1, "Egg", 100, 10, 10, 10, 10, ""),
            Nutrition(2, "Bread", 100, 10, 10, 10, 10, ""),
            Nutrition(3, "Butter", 100, 10, 10, 10, 10, ""),
            Nutrition(4, "Milk", 100, 10, 10, 10, 10, ""),
        ),
        onOpen = {},
        onRemove = { _, _ ->

        }
    )
}
