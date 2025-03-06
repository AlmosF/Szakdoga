package com.example.szakdogafitness.feature.setup.trainee

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.szakdogafitness.ui.common.HeightSelector
import com.example.szakdogafitness.ui.common.LargeDropdownMenu
import com.example.szakdogafitness.ui.common.NormalTextField
import com.example.szakdogafitness.ui.common.WeightSelector
import com.example.szakdogafitness.viewmodel.SetupEvent
import com.example.szakdogafitness.viewmodel.SetupViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TraineeOne(
    paddingValues: PaddingValues = PaddingValues(16.dp),
    setupViewModel: SetupViewModel
) {
    val textFieldModifier = Modifier
        .fillMaxWidth(0.9f)
        .padding(top = 8.dp, bottom = 8.dp)
    var unit by remember { mutableStateOf(0) }
    //val state = setupViewModel.state.collectAsState().value
    val state by setupViewModel.state.collectAsState()

    ConstraintLayout (
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        var (bar, label, un, height, weight, training, goal, level, button) = createRefs()

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
            progress = 0.11f
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
        SingleChoiceSegmentedButtonRow (
            modifier = textFieldModifier
                .constrainAs(un) {
                    top.linkTo(label.bottom)
                    top.linkTo(parent.top)
                    start.linkTo(startGuideline5)
                }
        ) {
            SegmentedButton(
                //selected = state.unit == 1,
                selected = state.user.trainee.unit == 1,
                onClick = {
                    //unit = 1
                    setupViewModel.onEvent(SetupEvent.UnitChanged(1))
                },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 1),
            ) {
                Text(text = "kg, cm")
            }
            Spacer(modifier = Modifier.width(48.dp))
            SegmentedButton(
                selected = state.user.trainee.unit == 2,
                onClick = {
                    //unit = 2
                    setupViewModel.onEvent(SetupEvent.UnitChanged(2))
                },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 1),
            ) {
                Text(text = "lb, ft")
            }
        }
        var heightOpened by remember { mutableStateOf(false) }
        var selectedHeight by remember { mutableStateOf(120) }
        var unit by remember { mutableStateOf(state.user.trainee.unit == 1) }
        NormalTextField(
            modifier = textFieldModifier
                .clickable(onClick = {
                    heightOpened = true
                })
                .constrainAs(height) {
                    top.linkTo(un.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Height",
            onValueChange = {},
            value = if (state.user.trainee.height == 0)
                ""
            else if (state.user.trainee.unit == 1)
                "${state.user.trainee.height} cm"
            else if (state.user.trainee.unit == 2) {
                val totalInches = (state.user.trainee.height / 2.54).toInt()
                val feet = totalInches / 12
                val inches = totalInches % 12 + 1
                "$feet ft $inches in"
            } else {
                ""
            }
                ,
            singleLine = true,
            enabled = false,
            readOnly = true,
            onDone = {},
            leadingIcon = null,
            trailingIcon = null,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
            textStyle = TextStyle.Default.copy(
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = Bold
            )
        )
        if (heightOpened){
            var heightDialogCm by remember { mutableStateOf(state.user.trainee.height) }
            Dialog(
                onDismissRequest = { heightOpened = false },
            ) {
                Column (
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    HeightSelector(
                        width = 100.dp,
                        itemHeight = 50.dp,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        textColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        unit = state.user.trainee.unit == 1,
                        onHeightSelected = { cm ->
                            setupViewModel.onEvent(SetupEvent.HeightChanged(cm))
                            Log.d("HeightSelectorVM", "selectedHeight: $cm, unit: $unit")
                            //heightDialogCm = cm
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        heightCm = state.user.trainee.height
                    )
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(
                            onClick = {
                                heightOpened = false
                            }
                        ) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        TextButton(
                            onClick = {
                                selectedHeight = heightDialogCm
                                heightOpened = false
                            }
                        ) {
                            Text("Ok")
                        }
                    }
                }
            }
        }
        var weightOpened by remember { mutableStateOf(false) }
        var selectedWeight by remember { mutableStateOf(50.0) }
        NormalTextField(
            modifier = textFieldModifier
                .clickable(onClick = {
                    weightOpened = true
                })
                .constrainAs(weight) {
                    top.linkTo(height.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Weight",
            onValueChange = {},
            value = if (state.user.trainee.weight == 0.0)
                ""
            else if (unit)
                "${state.user.trainee.weight} kg"
            else {
                val pounds = state.user.trainee.weight * 2.20462
                "$pounds lb"
            },
            singleLine = true,
            enabled = false,
            readOnly = true,
            onDone = {},
            leadingIcon = null,
            trailingIcon = null,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
            textStyle = TextStyle.Default.copy(
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = Bold
            )
        )
        if (weightOpened){ //EZ HIBÁS MÉG ///TODO
            var weightDialog by remember { mutableStateOf(state.user.trainee.weight) }
            Dialog(
                onDismissRequest = { weightOpened = false },
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    WeightSelector(
                        width = 75.dp,
                        itemHeight = 50.dp,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        textColor = Color.Gray,
                        unit = unit,
                        weight = state.user.trainee.weight,
                        onWeightSelected = {
                            weightDialog = it
                        }
                    )
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(
                            onClick = {
                                weightOpened = false
                            }
                        ) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        TextButton(
                            onClick = {
                                //selectedWeight = weightDialog
                                setupViewModel.onEvent(SetupEvent.WeightChanged(weightDialog))
                                weightOpened = false
                            }
                        ) {
                            Text("Ok")
                        }
                    }
                }
            }
        }

        var selectedTriningIndices by remember { mutableStateOf(emptyList<Int>()) }
        val trainingItems = listOf("1", "2", "3", "4", "5")

        LargeDropdownMenu( //TODO: Nincs kész rossz a paraméter
            modifier = textFieldModifier
                .constrainAs(training) {
                    top.linkTo(weight.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Training / Week",
            items = trainingItems,
            selectedIndices = state.user.trainee.trainings.let { if (it == -1) emptyList() else listOf(it) },
            onItemSelected = { index, items ->
                //selectedTriningIndices = listOf(index)
                setupViewModel.onEvent(
                    SetupEvent.TrainingsChanged(index)
                )
            },
        )

        var selectedFitnessGoalIndices by remember { mutableStateOf(emptyList<Int>()) }
        val fitnessGoalItems = listOf("Lose Weight", "Gain Muscle", "Improve Endurance", "Improve Flexibility", "Improve Strength", "Improve Speed", "Improve Power", "Improve Agility", "Improve Balance", "Improve Coordination", "Improve Accuracy", "Improve Stamina", "Improve Cardiovascular Health", "Improve Mental Health", "Improve Sleep", "Improve Nutrition", "Improve Hydration", "Improve Recovery", "Improve Mobility", "Improve Posture", "Improve Technique", "Improve Form", "Improve Breathing", "Improve Confidence", "Improve Discipline", "Improve Consistency", "Improve Patience", "Improve Focus", "Improve Mindfulness", "Improve Awareness", "Improve Presence", "Improve Resilience", "Improve Grit", "Improve Determination", "Improve Perseverance", "Improve Dedication", "Improve Commitment", "Improve Passion", "Improve Love", "Improve Joy", "Improve Happiness", "Improve Fulfillment", "Improve Purpose", "Improve Meaning", "Improve Connection", "Improve Community", "Improve Relationships", "Improve Communication", "Improve Collaboration", "Improve Teamwork", "Improve Leadership", "Improve Management", "Improve Organization", "Improve Planning", "Improve Preparation", "Improve Execution", "Improve Performance", "Improve Results", "Improve Success", "Improve Achievement", "Improve Progress", "Improve Growth", "Improve Development", "Improve Evolution", "Improve Transformation", "Improve Change", "Improve Adaptation", "Improve Innovation", "Improve Creativity", "Improve Imagination", "Improve Inspiration", "Improve Motivation", "Improve Encouragement", "Improve Support", "Improve Empowerment", "Improve Influence", "Improve Impact", "Improve Contribution", "Improve Service", "Improve Value", "Improve Quality", "Improve Excellence", "Improve Greatness", "Improve Mastery", "Improve Wisdom", "Improve Knowledge", "Improve Understanding", "Improve Awareness", "Improve Consciousness", "Improve Spirituality", "Improve Religion", "Improve")
        LargeDropdownMenu(
            modifier = textFieldModifier
                .constrainAs(goal) {
                    top.linkTo(training.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Fitness Goals",
            multiSelect = true,
            items = fitnessGoalItems,
            selectedIndices = state.user.trainee.fitnessGoals,
            onItemsSelected = { index, item ->
                /*selectedFitnessGoalIndices = if (selectedFitnessGoalIndices.contains(index)) {
                    selectedFitnessGoalIndices - index
                } else {
                    selectedFitnessGoalIndices + index
                }*/
                setupViewModel.onEvent(
                    SetupEvent.FitnessGoalsChanged(
                        if (state.user.trainee.fitnessGoals.contains(index)){
                            state.user.trainee.fitnessGoals - index
                        } else {
                            state.user.trainee.fitnessGoals + index
                        }
                    )
                )
            },
        )

        var currentFitnessLevelIndices by remember { mutableStateOf(emptyList<Int>()) }
        val currentFitnessLevelItems = listOf("Beginner", "Intermediate", "Advanced", "Professional", "Elite", "Olympian")
        LargeDropdownMenu( //TODO: Nincs kész rossz a paraméter
            modifier = textFieldModifier
                .constrainAs(level) {
                    top.linkTo(goal.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Current Fitness Level",
            items = currentFitnessLevelItems,
            selectedIndices = state.user.trainee.currentFitnessLevel.let { if (it == -1) emptyList() else listOf(it) },
            onItemSelected = { index, items ->
                //currentFitnessLevelIndices = listOf(index)
                setupViewModel.onEvent(
                    SetupEvent.CurrentFitnessLevelChanged(index)
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

    /*Column (
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ProgressBar(progress = 0.11f)
        Text(
            text = "Who are you?",
            fontSize = 24.sp,
            modifier = Modifier.padding(8.dp),
            fontWeight = Bold
        )
        SingleChoiceSegmentedButtonRow (
            modifier = textFieldModifier
        ) {
            SegmentedButton(
                selected = unit == 1,
                onClick = {
                    unit = 1
                },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 1),
            ) {
                Text(text = "kg, cm")
            }
            Spacer(modifier = Modifier.width(48.dp))
            SegmentedButton(
                selected = unit == 2,
                onClick = {
                    unit = 2
                },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 1),
            ) {
                Text(text = "lb, ft")
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        /*MultiSelectDropdown(
            label = "Select your level",
            modifier = textFieldModifier,
            options = listOf("Beginner", "Intermediate", "Advanced", "Professional", "Elite", "Olympian"),
            selectedOptions = emptyList()
        ) {

        }*/
        /*SelectDropdown(
            label = "Fitness Goals",
            modifier = textFieldModifier,
            multiSelect = true,
            options = listOf("Lose Weight", "Gain Muscle", "Improve Endurance", "Improve Flexibility", "Improve Strength", "Improve Speed", "Improve Power", "Improve Agility", "Improve Balance", "Improve Coordination", "Improve Accuracy", "Improve Stamina", "Improve Cardiovascular Health", "Improve Mental Health", "Improve Sleep", "Improve Nutrition", "Improve Hydration", "Improve Recovery", "Improve Mobility", "Improve Posture", "Improve Technique", "Improve Form", "Improve Breathing", "Improve Confidence", "Improve Discipline", "Improve Consistency", "Improve Patience", "Improve Focus", "Improve Mindfulness", "Improve Awareness", "Improve Presence", "Improve Resilience", "Improve Grit", "Improve Determination", "Improve Perseverance", "Improve Dedication", "Improve Commitment", "Improve Passion", "Improve Love", "Improve Joy", "Improve Happiness", "Improve Fulfillment", "Improve Purpose", "Improve Meaning", "Improve Connection", "Improve Community", "Improve Relationships", "Improve Communication", "Improve Collaboration", "Improve Teamwork", "Improve Leadership", "Improve Management", "Improve Organization", "Improve Planning", "Improve Preparation", "Improve Execution", "Improve Performance", "Improve Results", "Improve Success", "Improve Achievement", "Improve Progress", "Improve Growth", "Improve Development", "Improve Evolution", "Improve Transformation", "Improve Change", "Improve Adaptation", "Improve Innovation", "Improve Creativity", "Improve Imagination", "Improve Inspiration", "Improve Motivation", "Improve Encouragement", "Improve Support", "Improve Empowerment", "Improve Influence", "Improve Impact", "Improve Contribution", "Improve Service", "Improve Value", "Improve Quality", "Improve Excellence", "Improve Greatness", "Improve Mastery", "Improve Wisdom", "Improve Knowledge", "Improve Understanding", "Improve Awareness", "Improve Consciousness", "Improve Spirituality", "Improve Religion", "Improve Faith", "Improve Belief"),
            selectedOptions = emptyList()
        ) { }
        Spacer(modifier = Modifier.width(8.dp))
        SelectDropdown(
            label = "Current Fitness Level",
            modifier = textFieldModifier,
            multiSelect = false,
            options = listOf("Beginner", "Intermediate", "Advanced", "Professional", "Elite", "Olympian"),
            selectedOptions = emptyList()
        ) { }*/

        LargeDropdownMenu(
            label = "Fitness Goals",
            modifier = textFieldModifier,
            multiSelect = true,
            items = listOf("Lose Weight", "Gain Muscle", "Improve Endurance", "Improve Flexibility", "Improve Strength", "Improve Speed", "Improve Power", "Improve Agility", "Improve Balance", "Improve Coordination", "Improve Accuracy", "Improve Stamina", "Improve Cardiovascular Health", "Improve Mental Health", "Improve Sleep", "Improve Nutrition", "Improve Hydration", "Improve Recovery", "Improve Mobility", "Improve Posture", "Improve Technique", "Improve Form", "Improve Breathing", "Improve Confidence", "Improve Discipline", "Improve Consistency", "Improve Patience", "Improve Focus", "Improve Mindfulness", "Improve Awareness", "Improve Presence", "Improve Resilience", "Improve Grit", "Improve Determination", "Improve Perseverance", "Improve Dedication", "Improve Commitment", "Improve Passion", "Improve Love", "Improve Joy", "Improve Happiness", "Improve Fulfillment", "Improve Purpose", "Improve Meaning", "Improve Connection", "Improve Community", "Improve Relationships", "Improve Communication", "Improve Collaboration", "Improve Teamwork", "Improve Leadership", "Improve Management", "Improve Organization", "Improve Planning", "Improve Preparation", "Improve Execution", "Improve Performance", "Improve Results", "Improve Success", "Improve Achievement", "Improve Progress", "Improve Growth", "Improve Development", "Improve Evolution", "Improve Transformation", "Improve Change", "Improve Adaptation", "Improve Innovation", "Improve Creativity", "Improve Imagination", "Improve Inspiration", "Improve Motivation", "Improve Encouragement", "Improve Support", "Improve Empowerment", "Improve Influence", "Improve Impact", "Improve Contribution", "Improve Service", "Improve Value", "Improve Quality", "Improve Excellence", "Improve Greatness", "Improve Mastery", "Improve Wisdom", "Improve Knowledge", "Improve Understanding", "Improve Awareness", "Improve Consciousness", "Improve Spirituality", "Improve Religion", "Improve Faith", "Improve Belief"),
            onItemSelected = { index, item -> },
            selectedItemToString = { it },
        )
    }*/
}
@Composable
fun SelectDropdown(
    modifier : Modifier = Modifier,
    label : String = "",
    options: List<String>,
    multiSelect: Boolean = false,
    selectedOptions: List<String>,
    onSelectionChange: (List<String>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItems by remember { mutableStateOf(selectedOptions) }

    Column (
        modifier = modifier
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = Bold,
        )
        Box(
            modifier = Modifier
        ) {
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { expanded = true },
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    text = if (selectedItems.isEmpty()) "Select options" else selectedItems.joinToString(", "),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                /*DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.heightIn(max = 250.dp).padding(8.dp),
                ) {
                    options.forEach { option ->
                        val isSelected = option in selectedItems
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = isSelected,
                                        onCheckedChange = {
                                            val newSelection = if (isSelected) {
                                                selectedItems - option
                                            } else {
                                                selectedItems + option
                                            }
                                            selectedItems = newSelection
                                            onSelectionChange(newSelection)
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = option)
                                }
                            },
                            onClick = {
                                if (!multiSelect) {
                                    selectedItems = listOf(option)
                                    onSelectionChange(listOf(option))
                                    expanded = false
                                    return@DropdownMenuItem
                                } else {
                                    val newSelection = if (isSelected) {
                                        selectedItems - option
                                    } else {
                                        selectedItems + option
                                    }
                                    selectedItems = newSelection
                                    onSelectionChange(newSelection)
                                }
                            }
                        )
                    }
                }*/
            }
        }
    }
}
@Composable
@Preview
fun PreviewTraineeOne() {
    /*TraineeOne(paddingValues = PaddingValues(0.dp),
        setupViewModel = SetupViewModel())*/
}