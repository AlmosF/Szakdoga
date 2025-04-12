package com.example.szakdogafitness.ui.common

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun NormalTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
    onDone: (KeyboardActionScope.() -> Unit)?,
    leadingIcon: @Composable() (() -> Unit)?,
    trailingIcon: @Composable() (() -> Unit)?,
    imeAction: ImeAction = ImeAction.Done,
    singleLine: Boolean,
    keyboardType: KeyboardType = KeyboardType.Text,
    colors: TextFieldColors,
    maxLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textStyle: TextStyle = TextStyle.Default
) {
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        //value = value.trim(),
        onValueChange = onValueChange,
        label = { Text(text = label) },
        leadingIcon = leadingIcon,
        trailingIcon = if (isError) {
            {
                Icon(
                    imageVector = Icons.Default.ErrorOutline,
                    contentDescription = null
                )
            }
        } else {
            {
                if (trailingIcon != null) {
                    trailingIcon()
                }
            }
        },
        maxLines = if (isFocused) maxLines else 1,
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged {
                isFocused = it.isFocused
            }
            .width(TextFieldDefaults.MinWidth),
        singleLine = singleLine,
        readOnly = readOnly,
        isError = isError,
        enabled = enabled,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = onDone
        ),
        colors = colors,
        shape = shape,
        visualTransformation = visualTransformation,
        textStyle = textStyle,
    )
}