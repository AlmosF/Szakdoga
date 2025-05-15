package com.example.szakdogafitness.feature.setup.trainer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.szakdogafitness.R
import com.example.szakdogafitness.feature.setup.toTriple
import com.example.szakdogafitness.ui.common.LargeDropdownMenu
import com.example.szakdogafitness.ui.common.NormalTextField
import com.example.szakdogafitness.ui.common.TrainerTimetableView
import com.example.szakdogafitness.ui.common.YearPickerDialog
import com.example.szakdogafitness.feature.setup.SetupEvent
import com.example.szakdogafitness.feature.setup.SetupViewModel
import com.google.firebase.Timestamp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainerOne(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    setupViewModel: SetupViewModel
) {
    val textFieldModifier = Modifier
        .fillMaxWidth(0.9f)
        .padding(top = 8.dp, bottom = 8.dp)

    val state by setupViewModel.state.collectAsState()

    val context = LocalContext.current

    ConstraintLayout (
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        var (bar, label, cert, areas, style, exp, availability, button) = createRefs()

        val startGuideline5 = createGuidelineFromStart(0.05f)
        val startGuideline25 = createGuidelineFromStart(0.25f)
        val endGuideline75 = createGuidelineFromStart(0.75f)
        val endGuideline95 = createGuidelineFromStart(0.95f)

        val certifications = context.resources.getStringArray(R.array.certifications).toList()

        LargeDropdownMenu(
            modifier = textFieldModifier
                .constrainAs(cert) {
                    top.linkTo(parent.top)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Certifications / Qualifications",
            items = certifications,
            selectedIndices = state.user.trainer.certifications,
            multiSelect = true,
            onItemsSelected = {  index, item ->
                setupViewModel.onEvent(
                    SetupEvent.CertificationsChanged(
                        if (state.user.trainer.certifications.contains(index)) {
                            state.user.trainer.certifications.filter { it != index }
                        } else {
                            state.user.trainer.certifications + index
                        }
                    )
                )
            }
        )

        val areasList = context.resources.getStringArray(R.array.areas_of_expertise).toList()

        LargeDropdownMenu(
            modifier = textFieldModifier
                .constrainAs(areas) {
                    top.linkTo(cert.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Areas of Expertise",
            items = areasList,
            selectedIndices = state.user.trainer.areasOfExperties,
            multiSelect = true,
            onItemsSelected = {  index, item ->
                setupViewModel.onEvent(
                    SetupEvent.AreasOfExpertiesChanged(
                        if (state.user.trainer.areasOfExperties.contains(index)) {
                            state.user.trainer.areasOfExperties.filter { it != index }
                        } else {
                            state.user.trainer.areasOfExperties + index
                        }
                    )
                )
            }
        )

        val stylesList = context.resources.getStringArray(R.array.training_styles).toList()

        LargeDropdownMenu(
            modifier = textFieldModifier
                .constrainAs(style) {
                    top.linkTo(areas.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Training Style / Philosophy",
            items = stylesList,
            selectedIndices = state.user.trainer.trainingStyles,
            multiSelect = true,
            onItemsSelected = {  index, item ->
                setupViewModel.onEvent(
                    SetupEvent.TrainingStylesChanged(
                        if (state.user.trainer.trainingStyles.contains(index)) {
                            state.user.trainer.trainingStyles.filter { it != index }
                        } else {
                            state.user.trainer.trainingStyles + index
                        }
                    )
                )
            }
        )

        var yearPickerDialogOpened by remember { mutableStateOf(false) }

        Box(
            modifier = textFieldModifier
                .height(IntrinsicSize.Min)
                .constrainAs(exp) {
                    top.linkTo(style.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                }
        ) {
            NormalTextField(
                label = "Experience (Since when)",
                value = if (state.user.trainer.experience != 0) state.user.trainer.experience.toString() else "Not set",
                enabled = true,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                trailingIcon = null,
                onDone = {},
                textStyle = TextStyle(fontSize = 16.sp),
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
                    .clickable { yearPickerDialogOpened = true },
                color = Color.Transparent,
            ) {}
        }
        if (yearPickerDialogOpened){
            YearPickerDialog(
                onDismissRequest = {
                    yearPickerDialogOpened = false
                },
                onYearSelected = {
                    setupViewModel.onEvent(
                        SetupEvent.ExperienceChanged(it)
                    )
                    yearPickerDialogOpened = false
                },
            )
        }

        TrainerTimetableView(
            modifier = textFieldModifier
                .constrainAs(availability) {
                    top.linkTo(exp.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            selectedHours = state.user.trainer.availability,
            onHourSelected = { day, hour ->
                val selectedHours = state.user.trainer.availability.toMutableMap().apply {
                    val hours = getOrDefault(day, emptyList()).toMutableList()
                    if (hours.contains(hour)) {
                        hours.remove(hour)
                        if (hours.isEmpty()) {
                            remove(day)
                        } else {
                            put(day, hours)
                        }
                    } else {
                        hours.add(hour)
                        put(day, hours)
                    }
                }
                setupViewModel.onEvent(
                    SetupEvent.AvailabilityChanged(selectedHours)
                )
            }
        )



    }

}