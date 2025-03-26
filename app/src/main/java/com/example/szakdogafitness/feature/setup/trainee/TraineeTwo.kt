package com.example.szakdogafitness.feature.setup.trainee

import android.R.attr.bottom
import android.R.attr.button
import android.R.attr.end
import android.R.attr.level
import android.R.attr.top
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.szakdogafitness.ui.common.ProgressBar
import com.example.szakdogafitness.viewmodel.SetupEvent
import com.example.szakdogafitness.viewmodel.SetupViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TraineeTwo(
    paddingValues: PaddingValues = PaddingValues(16.dp),
    setupViewModel: SetupViewModel
) {
    val textFieldModifier = Modifier
        .fillMaxWidth(0.9f)
        .padding(top = 8.dp, bottom = 8.dp)
    //var pref by remember { mutableStateOf(0) }
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
        /*ProgressBar(
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
        Text(
            text = "Trainer Preferences (Optional)",
            fontSize = 16.sp,
            modifier = Modifier
                .constrainAs(trainerlabel) {
                    //top.linkTo(label.bottom, margin = 16.dp)
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
                //selected = pref == 1,
                selected = state.user.trainee.trainingPreference == 1,
                onClick = {
                    /*pref = if (pref == 1) 0
                    else 1*/
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
                //selected = pref == 2,
                selected = state.user.trainee.trainingPreference == 2,
                onClick = {
                    /*pref = if (pref == 2) 0
                    else 2*/
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

        //var currentHInfIndices by remember { mutableStateOf(listOf<Int>()) }
        //var healthInformation = listOf("Healthy", "Cardiovascular Disease", "Respiratory Conditions", "Musculoskeletal Issues", "Endocrine Disorders", "Neurological Conditions", "Digestive Disorders", "Mental Health Conditions", "Allergies", "Autoimmune Diseases", "Back Pain", "Knee Problems", "Shoulder Pain", "Neck Pain", "Plantar Fasciitis", "Carpal Tunnel Syndrome", "Pregnancy", "Postpartum", "History of Injuries", "Surgeries", "Medications", "Smoking Status", "Alcohol Consumption")
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
            //selectedIndices = currentHInfIndices,
            selectedIndices = state.user.trainee.healthInformation,
            multiSelect = true,
            onItemsSelected = { index, item ->
                /*currentHInfIndices = if (currentHInfIndices.contains(index)) {
                    currentHInfIndices - index
                } else {
                    currentHInfIndices + index
                }*/
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

        //var currentDietIndices by remember { mutableStateOf(listOf<Int>()) }
        //var dietInf = listOf("None", "Vegetarian", "Vegan", "Gluten-Free", "Dairy-Free", "Lactose Intolerant", "Nut Allergy", "Peanut Allergy", "Tree Nut Allergy", "Soy Allergy", "Egg Allergy", "Shellfish Allergy", "Fish Allergy", "Wheat Allergy", "Other Allergies", "Low-Carb", "Ketogenic (Keto)", "Paleo", "Whole30", "Low-Sodium", "Low-Sugar", "High-Protein", "Kosher", "Halal", "Weight Watchers", "Other Restrictions", "Prefer Not to Say")
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
            //selectedIndices = currentDietIndices,
            selectedIndices = state.user.trainee.dietaryPreferences,
            multiSelect = true,
            onItemsSelected = { index, item ->
                /*currentDietIndices = if (currentDietIndices.contains(index)) {
                    currentDietIndices - index
                } else {
                    currentDietIndices + index
                }*/
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
        //var bioText by remember { mutableStateOf("") }

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

        //var currentEquIndices by remember { mutableStateOf(listOf<Int>()) }
        //var equipment = listOf("None", "Dumbbells", "Kettlebells", "Barbell", "Bench", "Pull-Up Bar", "Resistance Bands", "Medicine Ball", "Stability Ball", "Yoga Mat", "Foam Roller", "TRX", "Box", "Bosu Ball", "Plyo Box", "Jump Rope", "Treadmill", "Stationary Bike", "Rowing Machine", "Elliptical", "Stair Climber", "Other Cardio Equipment", "Other Strength Equipment", "Other")
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
            //selectedIndices = currentEquIndices,
            selectedIndices = state.user.trainee.availableEquipment,
            multiSelect = true,
            onItemsSelected = { index, item ->
                /*currentEquIndices = if (currentEquIndices.contains(index)) {
                    currentEquIndices - index
                } else {
                    currentEquIndices + index
                }*/
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


        /*OutlinedButton(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    start.linkTo(startGuideline25)
                    end.linkTo(endGuideline75)
                },
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Next",
                modifier = Modifier
                    .padding(8.dp),
                fontSize = 20.sp
            )
        }*/

    }
}