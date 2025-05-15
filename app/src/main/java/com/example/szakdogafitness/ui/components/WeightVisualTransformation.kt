package com.example.szakdogafitness.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class WeightVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text

        val transformedText = if (originalText.contains(".")) {
            originalText.trimEnd('0').trimEnd('.')
        } else {
            originalText
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return minOf(offset, transformedText.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                return minOf(offset, originalText.length)
            }
        }

        return TransformedText(AnnotatedString(transformedText), offsetMapping)
    }
}