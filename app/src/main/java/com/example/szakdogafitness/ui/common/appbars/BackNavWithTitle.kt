package com.example.szakdogafitness.ui.common.appbars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.szakdogafitness.viewmodel.AppBarEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackNavWithTitle(
    title: String,
    onBackClick: () -> Unit
) {
    TopBar(
        title = {Text(title)},
        navigationIcon = {
            IconButton(
                onClick = {
                    onBackClick()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back",
                )
            }
        }
    )
}