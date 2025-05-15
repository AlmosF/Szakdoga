package com.example.szakdogafitness.feature.setup.trainee

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.szakdogafitness.R
import com.example.szakdogafitness.ui.common.LargeDropdownMenu
import com.example.szakdogafitness.ui.common.NormalTextField
import com.example.szakdogafitness.feature.setup.SetupEvent
import com.example.szakdogafitness.feature.setup.SetupViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TraineeTwo(
    paddingValues: PaddingValues = PaddingValues(16.dp),
    setupViewModel: SetupViewModel
) {
    val textFieldModifier = Modifier
        .fillMaxWidth(0.9f)
        .padding(top = 8.dp, bottom = 8.dp)
    val state by setupViewModel.state.collectAsState()
    val context = LocalContext.current

    ConstraintLayout(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        var (bar, label, trainerlabel, trainer, health, dietary, bio, equ, button) = createRefs()

        val startGuideline5 = createGuidelineFromStart(0.05f)
        val startGuideline25 = createGuidelineFromStart(0.25f)
        val endGuideline75 = createGuidelineFromStart(0.75f)
        val endGuideline95 = createGuidelineFromStart(0.95f)
        Text(
            text = "Trainer Preferences (Optional)",
            fontSize = 16.sp,
            modifier = Modifier
                .constrainAs(trainerlabel) {
                    top.linkTo(parent.top)
                    start.linkTo(startGuideline5)
                },
            fontWeight = Bold
        )
        SingleChoiceSegmentedButtonRow (
            modifier = textFieldModifier
                .constrainAs(trainer) {
                    top.linkTo(trainerlabel.bottom)
                    start.linkTo(startGuideline5)
                }
        ) {
            SegmentedButton(
                selected = state.user.trainee.trainingPreference == 1,
                onClick = {
                    setupViewModel.onEvent(
                        SetupEvent.TrainingPreferenceChanged(
                            if (state.user.trainee.trainingPreference == 1) 0
                            else 1
                        )
                    )
                },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 1),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Male,
                        contentDescription = null
                    )
                },
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = Color(0xFF0040FF),
                    activeContentColor = Color.White
                )
            ) {
                Text(text = "Male")
            }
            Spacer(modifier = Modifier.width(48.dp))
            SegmentedButton(
                selected = state.user.trainee.trainingPreference == 2,
                onClick = {
                    setupViewModel.onEvent(
                        SetupEvent.TrainingPreferenceChanged(
                            if (state.user.trainee.trainingPreference == 2) 0
                            else 2
                        )
                    )
                },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 1),
                icon = {
                    Icon(
                        imageVector = Icons.Default.Female,
                        contentDescription = null
                    )
                },
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = Color(0xFFEB00FF),
                    activeContentColor = Color.White
                )
            ) {
                Text(text = "Female")
            }
        }

        val healthInformation = context.resources.getStringArray(R.array.health_information).toList()

        LargeDropdownMenu(
            modifier = textFieldModifier
                .constrainAs(health) {
                    top.linkTo(trainer.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Health Information",
            items = healthInformation,
            selectedIndices = state.user.trainee.healthInformation,
            multiSelect = true,
            onItemsSelected = { index, item ->
                setupViewModel.onEvent(
                    SetupEvent.HealthInformationChanged(
                        if (state.user.trainee.healthInformation.contains(index)){
                            state.user.trainee.healthInformation.filter { it != index }
                        } else {
                            state.user.trainee.healthInformation + index
                        }
                    )
                )
            },
        )

        val dietInf = context.resources.getStringArray(R.array.diet_information).toList()
        LargeDropdownMenu(
            modifier = textFieldModifier
                .constrainAs(dietary) {
                    top.linkTo(health.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Dietary Information / Restrictions",
            items = dietInf,
            selectedIndices = state.user.trainee.dietaryPreferences,
            multiSelect = true,
            onItemsSelected = { index, item ->
                setupViewModel.onEvent(
                    SetupEvent.DietaryPreferencesChanged(
                        if (state.user.trainee.dietaryPreferences.contains(index)){
                            state.user.trainee.dietaryPreferences.filter { it != index }
                        } else {
                            state.user.trainee.dietaryPreferences + index
                        }
                    )
                )
            },
        )

        NormalTextField(
            modifier = textFieldModifier
                .constrainAs(bio) {
                    top.linkTo(dietary.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Bio / Profile",
            value = state.user.bio,
            onValueChange = {
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

        val equipment = context.resources.getStringArray(R.array.equipment).toList()
        LargeDropdownMenu(
            modifier = textFieldModifier
                .constrainAs(equ) {
                    top.linkTo(bio.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Equipment Available",
            items = equipment,
            selectedIndices = state.user.trainee.availableEquipment,
            multiSelect = true,
            onItemsSelected = { index, item ->
                setupViewModel.onEvent(
                    SetupEvent.AvailableEquipmentChanged(
                        if (state.user.trainee.availableEquipment.contains(index)){
                            state.user.trainee.availableEquipment.filter { it != index }
                        } else {
                            state.user.trainee.availableEquipment + index
                        }
                    )
                )
            },
        )
    }
}