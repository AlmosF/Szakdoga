package com.example.szakdogafitness.ui.common

import android.R.attr.textColor
import android.R.attr.textStyle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.szakdogafitness.ui.theme.SzakdogaFitnessTheme

@Composable
fun YearSelector(
    modifier : Modifier = Modifier,
    enabled: Boolean = true,
    dialogWidth: Dp = 70.dp,
    dialogHeight: Dp = 50.dp,
    label: String,
    notSetLabel: String? = null,
    range: IntRange,
    textStyle: TextStyle,
    textColor: Color,
    selectedTextColor: Color,
    initialYear: Int = -1,
    selectedYear: Int = -1,
    onYearSelected: (year: Int) -> Unit = { _ -> },
) {
    var dialogOpened by remember { mutableStateOf(false) }
    Box(
        modifier = modifier.height(IntrinsicSize.Min)
    ) {
        NormalTextField(
            label = label,
            value = if (selectedYear != -1) selectedYear.toString() else notSetLabel ?: "",
            enabled = enabled,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            trailingIcon = null,
            onDone = {},
            textStyle = textStyle,
            leadingIcon = null,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
        )
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .clickable(enabled = enabled) { dialogOpened = true },
            color = Color.Transparent,
        ) {}

        if (dialogOpened) {
            Dialog(
                onDismissRequest = { dialogOpened = false }
            ) {
                Column (
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    var selectedYear by remember { mutableStateOf(initialYear) }
                    val items = range.toList()
                    InfiniteCircularList(
                        items = items,
                        initialItem = initialYear,
                        textStyle = textStyle,
                        textColor = textColor,
                        width = dialogWidth,
                        itemHeight = dialogHeight,
                        selectedTextColor = selectedTextColor,
                        onItemSelected = { index, item ->
                            selectedYear = item
                        }
                    )
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        TextButton(
                            onClick = {
                                dialogOpened = false
                            }
                        ) {
                            Text("Cancel")
                        }
                        TextButton(
                            onClick = {
                                onYearSelected(selectedYear)
                                dialogOpened = false
                            }
                        ) {
                            Text("Ok")
                        }
                    }
                }
                /*SzakdogaFitnessTheme {
                    Surface (
                        modifier = Modifier
                            .padding(32.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        var selectedYear by remember { mutableStateOf(initialYear) }
                        val items = range.toList()
                        InfiniteCircularList(
                            items = items,
                            initialItem = initialYear,
                            textStyle = textStyle,
                            textColor = textColor,
                            width = dialogWidth,
                            itemHeight = dialogHeight,
                            selectedTextColor = selectedTextColor,
                            onItemSelected = { index, item ->
                                selectedYear = item
                            }
                        )
                        OutlinedButton(
                            onClick = {
                                onYearSelected(selectedYear)
                                dialogOpened = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Done",
                                modifier = Modifier.padding(8.dp),
                                fontSize = 20.sp
                            )
                        }

                    }
                }*/
            }
        }

    }
}