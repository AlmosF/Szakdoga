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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.szakdogafitness.R
import com.example.szakdogafitness.ui.common.HeightSelector
import com.example.szakdogafitness.ui.common.LargeDropdownMenu
import com.example.szakdogafitness.ui.common.NormalTextField
import com.example.szakdogafitness.ui.common.WeightSelector
import com.example.szakdogafitness.feature.setup.SetupEvent
import com.example.szakdogafitness.feature.setup.SetupViewModel


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
    val state by setupViewModel.state.collectAsState()
    val context = LocalContext.current

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

        SingleChoiceSegmentedButtonRow (
            modifier = textFieldModifier
                .constrainAs(un) {
                    top.linkTo(label.bottom)
                    top.linkTo(parent.top)
                    start.linkTo(startGuideline5)
                }
        ) {
            SegmentedButton(
                selected = state.user.trainee.unit == 1,
                onClick = {
                    if (state.user.trainee.unit == 1) return@SegmentedButton
                    else setupViewModel.onEvent(SetupEvent.UnitChanged(1))
                },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 1),
            ) {
                Text(text = "kg, cm")
            }
            Spacer(modifier = Modifier.width(48.dp))
            SegmentedButton(
                selected = state.user.trainee.unit == 2,
                onClick = {
                    if (state.user.trainee.unit == 2) return@SegmentedButton
                    else setupViewModel.onEvent(SetupEvent.UnitChanged(2))
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
                .constrainAs(weight) {
                    top.linkTo(height.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Weight",
            onValueChange = {
                if (it.matches(Regex("^\\d*\\.?\\d*\$"))) {
                    setupViewModel.onEvent(SetupEvent.WeightChanged(it.toDoubleOrNull() ?: 0.0))
                }
            },
            value = state.user.trainee.weight.let { weight ->
                if (weight == 0.0) ""
                else if(weight.toString().endsWith(".0")) weight.toString().dropLast(2)
                else weight.toString()
            },
            singleLine = true,
            enabled = true,
            readOnly = false,
            onDone = {},
            leadingIcon = null,
            trailingIcon = {
                if (state.user.trainee.unit != 0){
                    Text(
                        text = if (state.user.trainee.unit == 1) "kg" else "lb",
                        fontSize = 20.sp,
                        fontWeight = Bold,
                    )
                }
            },
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
        val trainingItems = context.resources.getStringArray(R.array.trainings_per_week).toList()

        LargeDropdownMenu( //TODO: Nincs kész rossz a paraméter
            modifier = textFieldModifier
                .constrainAs(training) {
                    top.linkTo(weight.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Training / Week",
            items = trainingItems,
            selectedIndices = state.user.trainee.trainings.let { if (it == 0) emptyList() else listOf(it) },
            onItemSelected = { index, items ->
                setupViewModel.onEvent(
                    SetupEvent.TrainingsChanged(index)
                )
            },
        )

        val fitnessGoalItems = context.resources.getStringArray(R.array.fitness_goal).toList()
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
        val currentFitnessLevelItems = context.resources.getStringArray(R.array.current_fitness_level).toList()
        LargeDropdownMenu( //TODO: Nincs kész rossz a paraméter
            modifier = textFieldModifier
                .constrainAs(level) {
                    top.linkTo(goal.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Current Fitness Level",
            items = currentFitnessLevelItems,
            selectedIndices = state.user.trainee.currentFitnessLevel.let { if (it == 0) emptyList() else listOf(it) },
            onItemSelected = { index, items ->
                setupViewModel.onEvent(
                    SetupEvent.CurrentFitnessLevelChanged(index)
                )
            },
        )
    }
}