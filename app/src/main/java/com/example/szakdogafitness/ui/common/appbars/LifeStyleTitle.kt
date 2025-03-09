package com.example.szakdogafitness.ui.common.appbars

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter

@Composable
fun LifeStyleTitle(
    modifier : Modifier = Modifier,
    title: String,
    onPreviousDayClick: () -> Unit,
    onNextDayClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onPreviousDayClick
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
            onClick = onNextDayClick

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

    IconButton(
        onClick = {
            onActionClick()
        }
    ) {
        Icon(
            painter = painter,
            contentDescription = "Action",
            modifier = Modifier.size(30.dp)
        )
    }
}