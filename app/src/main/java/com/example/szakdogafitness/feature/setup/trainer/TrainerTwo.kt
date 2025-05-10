package com.example.szakdogafitness.feature.setup.trainer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.szakdogafitness.R
import com.example.szakdogafitness.ui.common.LargeDropdownMenu
import com.example.szakdogafitness.ui.common.NormalTextField
import com.example.szakdogafitness.ui.common.SocialMediaSelector
import com.example.szakdogafitness.feature.setup.SetupEvent
import com.example.szakdogafitness.feature.setup.SetupViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainerTwo(
    paddingValues: PaddingValues = PaddingValues(16.dp),
    setupViewModel: SetupViewModel
) {
    val textFieldModifier = Modifier
        .fillMaxWidth(0.9f)
        .padding(top = 8.dp, bottom = 8.dp)

    val state = setupViewModel.state.collectAsState().value

    val context = LocalContext.current

    ConstraintLayout (
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        var (bar, label, pric, spec, bio, socmed, button) = createRefs()

        val startGuideline5 = createGuidelineFromStart(0.05f)
        val startGuideline25 = createGuidelineFromStart(0.25f)
        val endGuideline75 = createGuidelineFromStart(0.75f)
        val endGuideline95 = createGuidelineFromStart(0.95f)

       /* ProgressBar(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .constrainAs(bar) {
                    top.linkTo(parent.top)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            progress = 0.8f
        )
        Text(
            text = "Who are you?",
            fontSize = 24.sp,
            modifier = Modifier
                .constrainAs(label) {
                    top.linkTo(bar.bottom)
                    start.linkTo(startGuideline5)
                },
            fontWeight = Bold
        )*/

        var pricing by remember {mutableStateOf(0)}
        NormalTextField(
            value = state.user.trainer.pricing.toString(),
            onValueChange = {
                //state.pricing = it.toIntOrNull() ?: 0
                setupViewModel.onEvent(SetupEvent.PricingChanged(it.toIntOrNull() ?: 0))
                            },
            label = "Pricing / Packages",
            modifier = textFieldModifier
                .constrainAs(pric) {
                    //top.linkTo(label.bottom)
                    top.linkTo(parent.top)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            onDone = {},
            trailingIcon = {
                Text(
                    text = "$",
                    modifier = Modifier.padding(end = 8.dp)
                )
            },
            leadingIcon = null,
            keyboardType = KeyboardType.Decimal,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
        )

        var selectedSpecializationsIndices by remember { mutableStateOf(listOf<Int>()) }
        //val specializations = listOf("None", "Working with Seniors", "Working with Athletes", "Working with Children/Teenagers", "Pre/Postnatal Fitness", "Weight Management (Overweight/Obesity)", "Corrective Exercise/Injury Prevention", "Chronic Disease Management (Diabetes, Heart Disease, etc.)", "Disability Fitness", "Mental Health and Wellness", "Body Transformation", "Competition Preparation (Bodybuilding, Powerlifting, etc.)", "Other Specializations", "Prefer Not to Say")
        val specializations = context.resources.getStringArray(R.array.specializations).toList()

        LargeDropdownMenu(
            label = "Specializations (Optional)",
            items = specializations,
            selectedIndices = state.user.trainer.specializations,
            multiSelect = true,
            onItemsSelected = { index, items ->
                /*selectedSpecializationsIndices = if (selectedSpecializationsIndices.contains(index)) {
                    selectedSpecializationsIndices - index
                } else {
                    selectedSpecializationsIndices + index
                }*/
                setupViewModel.onEvent(
                    SetupEvent.SpecializationsChanged(
                        if (state.user.trainer.specializations.contains(index)){
                            state.user.trainer.specializations - index
                        } else {
                            state.user.trainer.specializations + index
                        }
                    )
                )
            },
            modifier = textFieldModifier
                .constrainAs(spec) {
                    top.linkTo(pric.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
        )
        var bioText by remember { mutableStateOf("") }

        NormalTextField(
            modifier = textFieldModifier
                .constrainAs(bio) {
                    top.linkTo(spec.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Bio / Profile",
            value = state.user.bio,
            onValueChange = {
                //bioText = it
                setupViewModel.onEvent(SetupEvent.BioChanged(it))
                            },
            singleLine = false,
            onDone = {},
            leadingIcon = null,
            trailingIcon = null,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
            maxLines = 5
        )

        var socialMedia by remember { mutableStateOf("") }
        var socialMediaMap by remember { mutableStateOf(mapOf<String, String>(
            "Facebook" to "",
            "Instagram" to "",
            "LinkedIn" to "",
            "X" to "",
            "YouTube" to "",
            "TikTok" to "",
            "Pinterest" to "",
            "Twitch" to "",
        ) ) }
        SocialMediaSelector(
            modifier = textFieldModifier
                .constrainAs(socmed) {
                    top.linkTo(bio.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Social Media",
            selectedTextColor = MaterialTheme.colorScheme.primary,
            socialMedia = socialMediaMap,
            onSocialMediaSelected = {
                socialMediaMap = it
                setupViewModel.onEvent(
                    SetupEvent.SocialMediaLinksChanged(
                        it
                    )
                )
            },
        )



    }

}

