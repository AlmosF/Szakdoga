package com.example.szakdogafitness.ui.common.appbars

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextPainter.paint
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter

@Composable
fun LifeStyleTitle(
    modifier : Modifier = Modifier,
    title: String,
    onPreviousDayClick: () -> Unit,
    onNextDayClick: () -> Unit,
    previousButtonEnabled: Boolean,
    nextButtonEnabled: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = {
                onPreviousDayClick()
            },
            enabled = previousButtonEnabled
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Menu",
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(30.dp)
            )
        }
        Text(
            text = title,
        )
        IconButton(
            onClick = {
                onNextDayClick()
            },
            enabled = nextButtonEnabled

        ) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Menu",
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(30.dp)
            )
        }
    }
}

@Composable
fun LifeStyleAction(
    modifier: Modifier = Modifier,
    image: String = "",
    onActionClick: () -> Unit
) {
    val painter = rememberAsyncImagePainter(image)

    Image(
        painter = painter,
        contentDescription = "Action",
        modifier = Modifier
            .padding(8.dp)
            .size(40.dp)
            .clip(CircleShape)
            .clickable{
                onActionClick()
            },
        contentScale = ContentScale.Crop
    )
}