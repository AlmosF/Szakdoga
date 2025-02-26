package com.example.szakdogafitness.feature.setup.trainer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.szakdogafitness.domain.usecases.setup.AllSetupUseCases
import com.example.szakdogafitness.ui.common.LargeDropdownMenu
import com.example.szakdogafitness.ui.common.TrainerTimetableView
import com.example.szakdogafitness.ui.common.YearSelector
import com.example.szakdogafitness.viewmodel.SetupEvent
import com.example.szakdogafitness.viewmodel.SetupViewModel

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
    }*/

    ConstraintLayout (
        modifier = modifier
            //.padding(paddingValues)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        var (bar, label, cert, areas, style, exp, availability, button) = createRefs()

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
            progress = 0.12f
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

        var selectedCertificationsIndices by remember { mutableStateOf((state.certifications)) }
        var certifications = listOf("None", "ACE Certified Personal Trainer (CPT)", "NASM Certified Personal Trainer (CPT)", "ISSA Certified Personal Trainer", "ACSM Certified Personal Trainer", "NSCA Certified Personal Trainer", "AFPA Certified Personal Trainer", "NESTA Certified Personal Trainer", "AFAA Certified Personal Fitness Instructor", "NPTI Certified Personal Trainer", "CrossFit Level 1 Trainer", "CrossFit Level 2 Trainer", "Certified Strength and Conditioning Specialist (CSCS)", "National Strength and Conditioning Association (NSCA)", "USA Weightlifting Level 1 Coach", "USA Weightlifting Level 2 Coach", "Precision Nutrition Level 1", "Precision Nutrition Level 2", "Certified Sports Nutritionist (CSN)", "Registered Dietitian (RD)", "Yoga Alliance Certified Teacher (RYT)", "Pilates Certification", "Group Fitness Instructor Certification", "CPR/AED Certified", "First Aid Certified", "Other Certifications", "Prefer Not to Say")

        LargeDropdownMenu(
            modifier = textFieldModifier
                .constrainAs(cert) {
                    //top.linkTo(label.bottom)
                    top.linkTo(parent.top)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Certifications / Qualifications",
            items = certifications,
            selectedIndices = state.certifications,
            multiSelect = true,
            onItemsSelected = {  index, item ->
                /*selectedCertificationsIndices = if (selectedCertificationsIndices.contains(index)) {
                    selectedCertificationsIndices.filter { it != index }
                } else {
                    selectedCertificationsIndices + index
                }*/
                /*setupViewModel.onEvent(
                    SetupEvent.CertificationsChanged(selectedCertificationsIndices)
                )*/
                setupViewModel.onEvent(
                    SetupEvent.CertificationsChanged(
                        if (state.certifications.contains(index)) {
                            state.certifications.filter { it != index }
                        } else {
                            state.certifications + index
                        }
                    )
                )
            }
        )

        var selectedAreasIndices by remember { mutableStateOf(listOf<Int>()) }
        var areasList = listOf("Weight Loss", "Muscle Gain / Hypertrophy", "Strength Training", "Functional Training", "HIIT (High-Intensity Interval Training)", "Cardiovascular Fitness", "Endurance Training", "Sports-Specific Training", "Bodybuilding", "Powerlifting", "Olympic Weightlifting", "Yoga", "Pilates", "Flexibility and Mobility", "Balance and Coordination", "Injury Rehabilitation", "Pre/Postnatal Fitness", "Senior Fitness", "Youth Fitness", "Nutrition Coaching", "Mindfulness and Stress Reduction", "Other Areas of Expertise", "Prefer Not to Say")

        LargeDropdownMenu(
            modifier = textFieldModifier
                .constrainAs(areas) {
                    top.linkTo(cert.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Areas of Expertise",
            items = areasList,
            selectedIndices = state.areasOfExperties,
            multiSelect = true,
            onItemsSelected = {  index, item ->
                /*selectedAreasIndices = if (selectedAreasIndices.contains(index)) {
                    selectedAreasIndices.filter { it != index }
                } else {
                    selectedAreasIndices + index
                }*/
                setupViewModel.onEvent(
                    SetupEvent.AreasOfExpertiesChanged(
                        if (state.areasOfExperties.contains(index)) {
                            state.areasOfExperties.filter { it != index }
                        } else {
                            state.areasOfExperties + index
                        }
                    )
                )
            }
        )

        var selectedStylesIndices by remember { mutableStateOf(listOf<Int>()) }
        var stylesList = listOf("Holistic Approach", "Results-Oriented", "Client-Centered", "Evidence-Based Practice", "Functional Fitness", "Mind-Body Connection", "Strength and Conditioning", "Positive Reinforcement", "Motivational Coaching", "Personalized Programs", "Small Group Training", "Online Coaching", "Outdoor Fitness", "Corrective Exercise", "Other Training Styles", "Prefer Not to Say")

        LargeDropdownMenu(
            modifier = textFieldModifier
                .constrainAs(style) {
                    top.linkTo(areas.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Training Style / Philosophy",
            items = stylesList,
            selectedIndices = state.trainingStyles,
            multiSelect = true,
            onItemsSelected = {  index, item ->
                /*selectedStylesIndices = if (selectedStylesIndices.contains(index)) {
                    selectedStylesIndices.filter { it != index }
                } else {
                    selectedStylesIndices + index
                }*/
                setupViewModel.onEvent(
                    SetupEvent.TrainingStylesChanged(
                        if (state.trainingStyles.contains(index)) {
                            state.trainingStyles.filter { it != index }
                        } else {
                            state.trainingStyles + index
                        }
                    )
                )
            }
        )

        var selectedExperienceIndice by remember { mutableStateOf(-1) }
        var experienceRangeYear = (1970..2100)
        YearSelector(
            modifier = textFieldModifier
                .constrainAs(exp) {
                    top.linkTo(style.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Experience (Since when)",
            range = experienceRangeYear,
            selectedYear = state.experience,
            initialYear = if (state.experience == 0) 2024 else state.experience,
            textStyle = TextStyle(fontSize = 16.sp),
            textColor = Color.Black,
            selectedTextColor = Color.Black,
            onYearSelected = { year ->
                //selectedExperienceIndice = year
                setupViewModel.onEvent(
                    SetupEvent.ExperienceChanged(year)
                )
            }
        )

        TrainerTimetableView(
            modifier = textFieldModifier
                .constrainAs(availability) {
                    top.linkTo(exp.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            selectedHours = state.availability,
            onHourSelected = { day, hour ->
                val selectedHours = state.availability.toMutableMap().apply {
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
                /*selectedHours = selectedHours.toMutableMap().apply {
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
                }*/
            }
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


@Composable
@Preview
fun PreviewTrainerOne() {
    /*TrainerOne(
        paddingValues = PaddingValues(0.dp),
        setupViewModel = SetupViewModel(
            AllSetupUseCases()
        )
    )*/
}