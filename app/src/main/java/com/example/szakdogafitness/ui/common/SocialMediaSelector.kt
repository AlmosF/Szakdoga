package com.example.szakdogafitness.ui.common

import android.R.attr.top
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.szakdogafitness.R

@Composable
fun SocialMediaSelector(
    modifier: Modifier = Modifier,
    enabled : Boolean = true,
    dialogWidth: Int = 70,
    dialogHeight: Int = 50,
    label: String,
    notSetLabel: String? = null,
    textStyle: TextStyle = TextStyle.Default,
    selectedTextColor: Color,
    socialMedia: Map<String, String>,
    onSocialMediaSelected: (socialMedia: Map<String, String>) -> Unit = { _ -> },
) {
    /*var dialogOpened by remember { mutableStateOf(false) }
    Box(
        modifier = modifier.height(IntrinsicSize.Min)
    ) {
        NormalTextField(
            label = label,
            value = "",
            enabled = enabled,
            onValueChange = {},
            onDone = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = null,
            leadingIcon = null,
            singleLine = true,
            textStyle = textStyle,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
        )
        Surface (
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .clickable(enabled = enabled) { dialogOpened = true },
            color = Color.Transparent
        ) {}
    }*/
    var selectedSocialMedia by remember { mutableStateOf("") }
    var socialMediaDialogOpened by remember { mutableStateOf(false) }
    val socialMediaImages = mapOf(
        "Facebook" to R.drawable.facebook,
        "Instagram" to R.drawable.instagram,
        "LinkedIn" to R.drawable.linkedin,
        "X" to R.drawable.twitter,
        "YouTube" to R.drawable.youtube,
        "TikTok" to R.drawable.tiktok,
        "Pinterest" to R.drawable.pinterest,
        "Twitch" to R.drawable.twitch,
    )
    Column (
        modifier = modifier
    ) {

        Text(
            text = label,
            fontSize = 16.sp,
            modifier = Modifier,
            fontWeight = Bold
        )
        LazyRow (
            modifier = Modifier
        ) {
            items(socialMedia.size) { index ->
                Image(
                    painter = painterResource(id = socialMediaImages[socialMedia.keys.elementAt(index)]!!),
                    contentDescription = socialMedia.keys.elementAt(index),
                    modifier = Modifier
                        .padding(8.dp)
                        .size(50.dp)
                        .clickable{
                            if (enabled){
                                selectedSocialMedia = socialMedia.keys.elementAt(index)
                                socialMediaDialogOpened = true
                            } else {
                                Log.d("SocialMediaSelector", "Social media is disabled")
                            }
                        },
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.colorMatrix(ColorMatrix().apply {
                        if (socialMedia[socialMedia.keys.elementAt(index)].isNullOrEmpty().not()) {
                            setToSaturation(1f)
                        } else {
                            setToSaturation(0f)
                        }
                    })
                )
            }
        }
    }

    if (socialMediaDialogOpened) {
        Dialog(
            onDismissRequest = { socialMediaDialogOpened = false }
        ) {
            Column (
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                var socialMediaLink by remember { mutableStateOf(socialMedia[selectedSocialMedia]!!) }
                NormalTextField(
                    label = selectedSocialMedia,
                    value = socialMediaLink,
                    enabled = true,
                    onValueChange = { socialMediaLink = it },
                    onDone = {},
                    readOnly = false,
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = null,
                    leadingIcon = null,
                    singleLine = true,
                    textStyle = textStyle,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                    ),
                )
                OutlinedButton(
                    onClick = {
                        socialMediaDialogOpened = false
                        onSocialMediaSelected(socialMedia + mapOf(selectedSocialMedia to socialMediaLink))
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(0.5f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Save",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 20.sp
                    )
                }
            }
        }
    }

}