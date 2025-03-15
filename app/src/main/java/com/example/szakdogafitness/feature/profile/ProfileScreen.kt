package com.example.szakdogafitness.feature.profile

import android.R.attr.enabled
import android.R.attr.end
import android.R.attr.height
import android.R.attr.label
import android.R.attr.level
import android.R.attr.onClick
import android.R.attr.singleLine
import android.R.attr.text
import android.R.attr.textStyle
import android.R.attr.top
import android.util.Log
import android.widget.GridLayout.spec
import android.widget.Space
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.example.szakdogafitness.ui.common.LargeDropdownMenu
import com.example.szakdogafitness.ui.common.NormalTextField
import com.example.szakdogafitness.ui.common.appbars.TopBar
import com.example.szakdogafitness.ui.components.PhoneNumberVisualTransformation
import com.example.szakdogafitness.ui.components.countryCodeToFlagEmoji
import com.example.szakdogafitness.ui.components.extractRegionCode
import com.example.szakdogafitness.viewmodel.AppBarEvent
import com.example.szakdogafitness.viewmodel.AppBarViewModel
import com.example.szakdogafitness.viewmodel.ProfileEvent
import com.example.szakdogafitness.viewmodel.ProfileViewModel
import com.example.szakdogafitness.viewmodel.SetupEvent
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    appBarViewModel: AppBarViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
) {

    val profileState by profileViewModel.state.collectAsState()
    var edit by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        profileViewModel.onEvent(ProfileEvent.GetUser)
        delay(100)
        appBarViewModel.onEvent(
            AppBarEvent.TopBarComposableChanged(
                {
                    TopBar(
                        title = {
                            Text("Profile Settings")
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    onNavigateBack()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    )
                }
            )
        )
    }
    val painter = rememberAsyncImagePainter(profileState.user?.profileImageUrl)
    val painterState by painter.state.collectAsState()
    val density = LocalDensity.current
    if (profileState.isLoading){
        Log.d("ProfileScreen", "Loading")
        Column (
            modifier = modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(400.dp)
            )
        }
    } else {
        val scrollState = rememberScrollState()

        Log.d("ProfileScreen", "NotLoading")
        Scaffold (
            modifier = modifier
                .fillMaxSize()
            ,
            bottomBar = {
                AnimatedVisibility(
                    enter = slideInVertically{
                        with(density) {0.dp.roundToPx()}
                    } + expandVertically(
                        expandFrom = Alignment.Bottom
                    ) + fadeIn(),
                    exit = slideOutVertically{
                        with(density) {0.dp.roundToPx()}
                    } + shrinkVertically(
                        shrinkTowards = Alignment.Bottom
                    ) + fadeOut(),
                    visible = profileState.switchEdit
                ) {
                    BottomAppBar {
                        Row (
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            OutlinedButton(
                                onClick = {
                                    profileViewModel.onEvent(ProfileEvent.UpdateUser)
                                },
                                shape = CircleShape
                            ) {
                                Text("Save Profile")
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            Column (
                modifier = Modifier.padding(innerPadding)
                    .fillMaxSize()
                    //.verticalScroll(rememberScrollState()),
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (painterState is AsyncImagePainter.State.Success){
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Image(
                            painter = painter,
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Text(
                    text = profileState.user?.firstName + " " + profileState.user?.lastName,
                    fontSize = 24.sp,
                    fontWeight = Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedButton(
                    onClick = {
                        profileViewModel.onEvent(ProfileEvent.SwitchEdit)
                    },
                    shape = CircleShape
                ) {
                    AnimatedVisibility(
                        visible = profileState.switchEdit
                    ) {
                        Text(
                            text = "Cancel"
                        )
                    }
                    AnimatedVisibility(
                        visible = !profileState.switchEdit
                    ) {
                        Text(
                            text = "Edit Profile"
                        )
                    }
                }
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    NormalTextField(
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f),
                        label = "First name",
                        onValueChange = { value ->
                            profileViewModel.onEvent(
                                ProfileEvent.EditUser(
                                    profileState.user?.copy(
                                        firstName = value
                                    )!!
                                )
                            )
                        },
                        value = profileState.editUser?.firstName ?: "",
                        singleLine = true,
                        readOnly = !profileState.switchEdit,
                        onDone = {},
                        leadingIcon = null,
                        trailingIcon = null,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                        ),
                    )
                    NormalTextField(
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f),
                        label = "Last name",
                        onValueChange = {},
                        value = profileState.user?.lastName ?: "",
                        singleLine = true,
                        readOnly = !profileState.switchEdit,
                        onDone = {},
                        leadingIcon = null,
                        trailingIcon = null,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                        ),
                    )
                }
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    NormalTextField(
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f),
                        label = "Date of Birth",
                        onValueChange = {},
                        value = profileState.user?.dateOfBirth.toString(),
                        singleLine = true,
                        readOnly = !profileState.switchEdit,
                        onDone = {},
                        leadingIcon = null,
                        trailingIcon = null,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                        ),
                    )
                    val countryCode = extractRegionCode(profileState.user?.phoneNumber ?: "")
                    val flagEmoji = countryCode?.let { countryCodeToFlagEmoji(it) }
                    NormalTextField(
                        label = "Phone number",
                        singleLine = true,
                        readOnly = !profileState.switchEdit,
                        value = profileState.user?.phoneNumber ?: "",
                        onValueChange = {},
                        visualTransformation = PhoneNumberVisualTransformation(),
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f),
                        trailingIcon = {
                            Text(text = flagEmoji ?: "")
                        },
                        keyboardType = KeyboardType.Phone,
                        onDone = {},
                        leadingIcon = null,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                        ),
                    )
                }
                NormalTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    label = "Bio / Profile",
                    value = profileState.user?.bio ?: "",
                    onValueChange = {},
                    singleLine = false,
                    readOnly = !profileState.switchEdit,
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
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    thickness = 3.dp
                )
                when(profileState.user?.role){
                    1 -> {
                        ProfileScreenTrainer(
                            profileViewModel = profileViewModel
                        )
                    }
                    2 -> {
                        ProfileScreenTrainee(
                            profileViewModel = profileViewModel
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ProfileScreenTrainer(
    profileViewModel: ProfileViewModel = hiltViewModel()
){

    val profileState by profileViewModel.state.collectAsStateWithLifecycle()

    Column (
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Text(
            text = "Trainer"
        )
        Text(
            text = profileState.user?.trainer?.pricing.toString()
        )

    }


}


@Composable
fun ProfileScreenTrainee(
    profileViewModel: ProfileViewModel = hiltViewModel()
){

    val profileState by profileViewModel.state.collectAsStateWithLifecycle()

    Column (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SingleChoiceSegmentedButtonRow (
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            SegmentedButton(
                //selected = state.unit == 1,
                selected = profileState.user?.trainee?.unit == 1,
                onClick = {},
                enabled = profileState.switchEdit,
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 1),
            ) {
                Text(text = "kg, cm")
            }
            Spacer(modifier = Modifier.width(48.dp))
            SegmentedButton(
                selected = profileState.user?.trainee?.unit == 2,
                onClick = {},
                enabled = profileState.switchEdit,
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 1),
            ) {
                Text(text = "lb, ft")
            }
        }
        Row (
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            NormalTextField(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = {}),
                label = "Height",
                onValueChange = {},
                value = if (profileState.user?.trainee?.height == 0)
                    ""
                else if (profileState.user?.trainee?.unit == 1)
                    "${profileState.user?.trainee?.height} cm"
                else if (profileState.user?.trainee?.unit == 2) {
                    val totalInches = (profileState.user?.trainee?.height?.div(2.54))?.toInt() ?: 0
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
            Spacer(modifier = Modifier.width(8.dp))
            NormalTextField(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = {}),
                label = "Weight",
                onValueChange = {},
                value = profileState.user?.trainee?.weight.toString(),
                singleLine = true,
                enabled = false,
                readOnly = true,
                onDone = {},
                leadingIcon = null,
                trailingIcon = {
                    Text(
                        text = if (profileState.user?.trainee?.weight == 0.0)
                            ""
                        else if (profileState.user?.trainee?.unit == 1)
                            "kg"
                        else
                            "lb",
                        fontWeight = Bold
                    )
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
            Spacer(modifier = Modifier.width(8.dp))
            NormalTextField(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = {}),
                label = "Training / Week",
                onValueChange = {},
                value = profileState.user?.trainee?.trainings?.toString() ?: "",
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
        }
        val fitnessGoalItems = listOf("Lose Weight", "Gain Muscle", "Improve Endurance", "Improve Flexibility", "Improve Strength", "Improve Speed", "Improve Power", "Improve Agility", "Improve Balance", "Improve Coordination", "Improve Accuracy", "Improve Stamina", "Improve Cardiovascular Health", "Improve Mental Health", "Improve Sleep", "Improve Nutrition", "Improve Hydration", "Improve Recovery", "Improve Mobility", "Improve Posture", "Improve Technique", "Improve Form", "Improve Breathing", "Improve Confidence", "Improve Discipline", "Improve Consistency", "Improve Patience", "Improve Focus", "Improve Mindfulness", "Improve Awareness", "Improve Presence", "Improve Resilience", "Improve Grit", "Improve Determination", "Improve Perseverance", "Improve Dedication", "Improve Commitment", "Improve Passion", "Improve Love", "Improve Joy", "Improve Happiness", "Improve Fulfillment", "Improve Purpose", "Improve Meaning", "Improve Connection", "Improve Community", "Improve Relationships", "Improve Communication", "Improve Collaboration", "Improve Teamwork", "Improve Leadership", "Improve Management", "Improve Organization", "Improve Planning", "Improve Preparation", "Improve Execution", "Improve Performance", "Improve Results", "Improve Success", "Improve Achievement", "Improve Progress", "Improve Growth", "Improve Development", "Improve Evolution", "Improve Transformation", "Improve Change", "Improve Adaptation", "Improve Innovation", "Improve Creativity", "Improve Imagination", "Improve Inspiration", "Improve Motivation", "Improve Encouragement", "Improve Support", "Improve Empowerment", "Improve Influence", "Improve Impact", "Improve Contribution", "Improve Service", "Improve Value", "Improve Quality", "Improve Excellence", "Improve Greatness", "Improve Mastery", "Improve Wisdom", "Improve Knowledge", "Improve Understanding", "Improve Awareness", "Improve Consciousness", "Improve Spirituality", "Improve Religion", "Improve")
        LargeDropdownMenu(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            label = "Fitness Goals",
            multiSelect = true,
            enabled = profileState.switchEdit,
            items = fitnessGoalItems,
            selectedIndices = profileState.user?.trainee?.fitnessGoals ?: emptyList(),
            onItemsSelected = { _, _ -> },
        )
        val currentFitnessLevelItems = listOf("Beginner", "Intermediate", "Advanced", "Professional", "Elite", "Olympian")
        LargeDropdownMenu( //TODO: Nincs kész rossz a paraméter
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            label = "Current Fitness Level",
            enabled = profileState.switchEdit,
            items = currentFitnessLevelItems,
            selectedIndices = profileState.user?.trainee?.currentFitnessLevel?.let { listOf(it) } ?: emptyList(),
            onItemSelected = { _, _ -> },
        )
        Text(
            text = "Trainer Preferences (Optional)",
            fontSize = 16.sp,
            modifier = Modifier
                .padding(start = 8.dp),
            fontWeight = Bold
        )
        SingleChoiceSegmentedButtonRow (
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            SegmentedButton(
                selected = profileState.user?.trainee?.trainingPreference == 1,
                onClick = {},
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
                selected = profileState.user?.trainee?.trainingPreference == 2,
                onClick = {},
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
        var healthInformation = listOf("Healthy", "Cardiovascular Disease", "Respiratory Conditions", "Musculoskeletal Issues", "Endocrine Disorders", "Neurological Conditions", "Digestive Disorders", "Mental Health Conditions", "Allergies", "Autoimmune Diseases", "Back Pain", "Knee Problems", "Shoulder Pain", "Neck Pain", "Plantar Fasciitis", "Carpal Tunnel Syndrome", "Pregnancy", "Postpartum", "History of Injuries", "Surgeries", "Medications", "Smoking Status", "Alcohol Consumption")
        LargeDropdownMenu(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            label = "Health Information",
            items = healthInformation,
            selectedIndices = profileState.user?.trainee?.healthInformation ?: emptyList(),
            multiSelect = true,
            onItemsSelected = { _, _ -> },
        )
        var dietInf = listOf("None", "Vegetarian", "Vegan", "Gluten-Free", "Dairy-Free", "Lactose Intolerant", "Nut Allergy", "Peanut Allergy", "Tree Nut Allergy", "Soy Allergy", "Egg Allergy", "Shellfish Allergy", "Fish Allergy", "Wheat Allergy", "Other Allergies", "Low-Carb", "Ketogenic (Keto)", "Paleo", "Whole30", "Low-Sodium", "Low-Sugar", "High-Protein", "Kosher", "Halal", "Weight Watchers", "Other Restrictions", "Prefer Not to Say")
        LargeDropdownMenu(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            label = "Dietary Information / Restrictions",
            items = dietInf,
            selectedIndices = profileState.user?.trainee?.dietaryPreferences ?: emptyList(),
            multiSelect = true,
            onItemsSelected = { _, _ -> },
        )
        var equipment = listOf("None", "Dumbbells", "Kettlebells", "Barbell", "Bench", "Pull-Up Bar", "Resistance Bands", "Medicine Ball", "Stability Ball", "Yoga Mat", "Foam Roller", "TRX", "Box", "Bosu Ball", "Plyo Box", "Jump Rope", "Treadmill", "Stationary Bike", "Rowing Machine", "Elliptical", "Stair Climber", "Other Cardio Equipment", "Other Strength Equipment", "Other")
        LargeDropdownMenu(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            label = "Equipment Available",
            items = equipment,
            selectedIndices = profileState.user?.trainee?.availableEquipment ?: emptyList(),
            multiSelect = true,
            onItemsSelected = { _, _ -> },
        )

    }



}


