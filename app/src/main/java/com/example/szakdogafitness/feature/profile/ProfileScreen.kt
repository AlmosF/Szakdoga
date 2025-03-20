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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import coil3.compose.AsyncImagePainter.State.Empty.painter
import coil3.compose.rememberAsyncImagePainter
import com.example.szakdogafitness.domain.model.Trainer
import com.example.szakdogafitness.feature.setup.DatePick
import com.example.szakdogafitness.feature.setup.toMillis
import com.example.szakdogafitness.feature.setup.toTimestamp
import com.example.szakdogafitness.feature.setup.toTriple
import com.example.szakdogafitness.ui.common.LargeDropdownMenu
import com.example.szakdogafitness.ui.common.NormalTextField
import com.example.szakdogafitness.ui.common.SocialMediaSelector
import com.example.szakdogafitness.ui.common.TrainerTimetableView
import com.example.szakdogafitness.ui.common.appbars.TopBar
import com.example.szakdogafitness.ui.components.PhoneNumberVisualTransformation
import com.example.szakdogafitness.ui.components.countryCodeToFlagEmoji
import com.example.szakdogafitness.ui.components.extractRegionCode
import com.example.szakdogafitness.viewmodel.AppBarEvent
import com.example.szakdogafitness.viewmodel.AppBarViewModel
import com.example.szakdogafitness.viewmodel.ProfileEvent
import com.example.szakdogafitness.viewmodel.ProfileViewModel
import com.example.szakdogafitness.viewmodel.SetupEvent
import com.google.android.play.integrity.internal.f
import com.google.firebase.Timestamp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    appBarViewModel: AppBarViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    onEditFoods: () -> Unit
) {

    val profileState by profileViewModel.state.collectAsState()

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
                        },
                        /*actions = {
                            IconButton(
                                onClick = {
                                    onLogout()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.Logout,
                                    contentDescription = "Logout"
                                )
                            }
                        }*/
                        actions = {
                            IconButton(
                                onClick = {
                                    profileViewModel.onEvent(ProfileEvent.DropDownChanged)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More Options"
                                )
                                DropdownMenu(
                                    expanded = profileState.dropDownExpanded,
                                    onDismissRequest = {
                                        profileViewModel.onEvent(ProfileEvent.DropDownChanged)
                                    }
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text("Logout")
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Default.Logout,
                                                contentDescription = "Logout"
                                            )
                                        },
                                        onClick = {
                                            profileViewModel.onEvent(ProfileEvent.DropDownChanged)
                                            onLogout()
                                        }
                                    )
                                    if(profileState.user?.isAdmin!!){
                                        DropdownMenuItem(
                                            text = {
                                                Text("Edit Foods")
                                            },
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = Icons.Default.Edit,
                                                    contentDescription = "Edit Foods"
                                                )
                                            },
                                            onClick = {
                                                profileViewModel.onEvent(ProfileEvent.DropDownChanged)
                                                onEditFoods()
                                            }
                                        )
                                    }

                                }
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
        var isDateOfBirthVisible by remember { mutableStateOf(false) }

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
                modifier = Modifier
                    .padding(innerPadding)
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
                        onValueChange = { value ->
                            profileViewModel.onEvent(
                                ProfileEvent.EditUser(
                                    profileState.editUser?.copy(
                                        lastName = value
                                    )!!
                                )
                            )
                        },
                        value = profileState.editUser?.lastName ?: "",
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
                            .weight(1f)
                            .clickable {
                                if (profileState.switchEdit)
                                    isDateOfBirthVisible = true
                            }
                        ,
                        label = "Date of Birth",
                        onValueChange = {},
                        value = profileState.editUser?.dateOfBirth?.toTriple().toString(),
                        enabled = false,
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
                    val countryCode = extractRegionCode(profileState.editUser?.phoneNumber ?: "")
                    val flagEmoji = countryCode?.let { countryCodeToFlagEmoji(it) }
                    NormalTextField(
                        label = "Phone number",
                        singleLine = true,
                        readOnly = !profileState.switchEdit,
                        value = profileState.editUser?.phoneNumber ?: "",
                        onValueChange = {
                            profileViewModel.onEvent(ProfileEvent.EditUser(
                                profileState.editUser?.copy(
                                    phoneNumber = it
                                )!!
                            ))
                        },
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
                    value = profileState.editUser?.bio ?: "",
                    onValueChange = {
                        profileViewModel.onEvent(ProfileEvent.EditUser(
                            profileState.editUser?.copy(
                                bio = it
                            )!!
                        ))
                    },
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
                if (isDateOfBirthVisible){
                    DatePick(
                        date = profileState.user?.dateOfBirth?.toMillis() ?: System.currentTimeMillis(),
                        selectRange = Timestamp.now().toMillis(),
                        onDateSelected = {
                            profileViewModel.onEvent(ProfileEvent.EditUser(
                                profileState.editUser?.copy(
                                    dateOfBirth = it?.toTimestamp()
                                )!!
                            ))
                        },
                    ) {
                        isDateOfBirthVisible = false
                    }

                }
            }
        }
    }
}

//
@Composable
fun ProfileScreenTrainer(
    profileViewModel: ProfileViewModel = hiltViewModel()
){

    val profileState by profileViewModel.state.collectAsStateWithLifecycle()

    Column (
        modifier = Modifier
            .fillMaxWidth()
    ) {

        var certifications = listOf("None", "ACE Certified Personal Trainer (CPT)", "NASM Certified Personal Trainer (CPT)", "ISSA Certified Personal Trainer", "ACSM Certified Personal Trainer", "NSCA Certified Personal Trainer", "AFPA Certified Personal Trainer", "NESTA Certified Personal Trainer", "AFAA Certified Personal Fitness Instructor", "NPTI Certified Personal Trainer", "CrossFit Level 1 Trainer", "CrossFit Level 2 Trainer", "Certified Strength and Conditioning Specialist (CSCS)", "National Strength and Conditioning Association (NSCA)", "USA Weightlifting Level 1 Coach", "USA Weightlifting Level 2 Coach", "Precision Nutrition Level 1", "Precision Nutrition Level 2", "Certified Sports Nutritionist (CSN)", "Registered Dietitian (RD)", "Yoga Alliance Certified Teacher (RYT)", "Pilates Certification", "Group Fitness Instructor Certification", "CPR/AED Certified", "First Aid Certified", "Other Certifications", "Prefer Not to Say")
        LargeDropdownMenu(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            label = "Certifications / Qualifications",
            items = certifications,
            selectedIndices = profileState.editUser?.trainer?.certifications ?: emptyList(),
            multiSelect = true,
            enabled = profileState.switchEdit,
            onItemsSelected = { index, item ->
                profileState.editUser?.let { user ->
                    val updatedCertifications = if(user.trainer.certifications.contains(index)){
                        user.trainer.certifications.filter { it != index }
                    } else {
                        user.trainer.certifications + index
                    }

                    profileViewModel.onEvent(
                        ProfileEvent.EditUser(
                            user.copy(
                                trainer = user.trainer.copy(
                                    certifications = updatedCertifications
                                )
                            )
                        )
                    )
                }

            }
        )

        var areasList = listOf("Weight Loss", "Muscle Gain / Hypertrophy", "Strength Training", "Functional Training", "HIIT (High-Intensity Interval Training)", "Cardiovascular Fitness", "Endurance Training", "Sports-Specific Training", "Bodybuilding", "Powerlifting", "Olympic Weightlifting", "Yoga", "Pilates", "Flexibility and Mobility", "Balance and Coordination", "Injury Rehabilitation", "Pre/Postnatal Fitness", "Senior Fitness", "Youth Fitness", "Nutrition Coaching", "Mindfulness and Stress Reduction", "Other Areas of Expertise", "Prefer Not to Say")
        LargeDropdownMenu(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            label = "Areas of Expertise",
            items = areasList,
            selectedIndices = profileState.editUser?.trainer?.areasOfExperties ?: emptyList(),
            multiSelect = true,
            enabled = profileState.switchEdit,
            onItemsSelected = { index, item ->
                profileState.editUser?.let { user ->
                    val updatedAOE = if (user.trainer.areasOfExperties.contains(index)){
                        user.trainer.areasOfExperties.filter { it != index }
                    } else {
                        user.trainer.areasOfExperties + index
                    }
                    profileViewModel.onEvent(
                        ProfileEvent.EditUser(
                            user.copy(
                                trainer = user.trainer.copy(
                                    areasOfExperties = updatedAOE
                                )
                            )
                        )
                    )
                }
            }
        )
        var stylesList = listOf("Holistic Approach", "Results-Oriented", "Client-Centered", "Evidence-Based Practice", "Functional Fitness", "Mind-Body Connection", "Strength and Conditioning", "Positive Reinforcement", "Motivational Coaching", "Personalized Programs", "Small Group Training", "Online Coaching", "Outdoor Fitness", "Corrective Exercise", "Other Training Styles", "Prefer Not to Say")
        LargeDropdownMenu(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            label = "Training Style / Philosophy",
            items = stylesList,
            selectedIndices = profileState.editUser?.trainer?.trainingStyles ?: emptyList(),
            multiSelect = true,
            enabled = profileState.switchEdit,
            onItemsSelected = { index, item ->
                profileState.editUser?.let { user ->
                    val updatedStyles = if(user.trainer.trainingStyles.contains(index)){
                        user.trainer.trainingStyles.filter { it != index }
                    } else {
                        user.trainer.trainingStyles + index
                    }

                    profileViewModel.onEvent(
                        ProfileEvent.EditUser(
                            user.copy(
                                trainer = user.trainer.copy(
                                    trainingStyles = updatedStyles
                                )
                            )
                        )
                    )
                }
            }
        )
        NormalTextField(
            value = profileState.editUser?.trainer?.pricing.toString(),
            onValueChange = {
                profileViewModel.onEvent(
                    ProfileEvent.EditUser(
                        profileState.editUser?.copy(
                            trainer = profileState.editUser?.trainer?.copy(
                                pricing = it.toIntOrNull() ?: 0
                            )!!
                        )!!
                    )
                )
            },
            label = "Pricing / Packages",
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            onDone = {},
            trailingIcon = {
                Text(
                    text = "HUF",
                    modifier = Modifier.padding(end = 8.dp)
                )
            },
            leadingIcon = null,
            readOnly = !profileState.switchEdit,
            keyboardType = KeyboardType.Decimal,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
        )
        val specializations = listOf("None", "Working with Seniors", "Working with Athletes", "Working with Children/Teenagers", "Pre/Postnatal Fitness", "Weight Management (Overweight/Obesity)", "Corrective Exercise/Injury Prevention", "Chronic Disease Management (Diabetes, Heart Disease, etc.)", "Disability Fitness", "Mental Health and Wellness", "Body Transformation", "Competition Preparation (Bodybuilding, Powerlifting, etc.)", "Other Specializations", "Prefer Not to Say")
        LargeDropdownMenu(
            label = "Specializations (Optional)",
            items = specializations,
            selectedIndices = profileState.editUser?.trainer?.specializations ?: emptyList(),
            multiSelect = true,
            enabled = profileState.switchEdit,
            onItemsSelected = { index, item ->
                profileState.editUser?.let { user ->
                    val updatedSpecializations = if(user.trainer.specializations.contains(index)){
                        user.trainer.specializations.filter { it != index }
                    } else {
                        user.trainer.specializations + index
                    }

                    profileViewModel.onEvent(
                        ProfileEvent.EditUser(
                            user.copy(
                                trainer = user.trainer.copy(
                                    specializations = updatedSpecializations
                                )
                            )
                        )
                    )
                }

            },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
        )

        TrainerTimetableView(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            enabled = profileState.switchEdit,
            selectedHours = profileState.editUser?.trainer?.availability ?: emptyMap(),
            onHourSelected = { day, hour ->
                profileState.editUser?.let { user ->
                    val selectedHours = user.trainer.availability.toMutableMap().apply {
                        if (this[day]?.contains(hour) == true){
                            this[day] = this[day]?.filter { it != hour }!!
                        } else {
                            this[day] = this[day]?.plus(hour) ?: listOf(hour)
                        }
                    }
                    profileViewModel.onEvent(
                        ProfileEvent.EditUser(
                            user.copy(
                                trainer = user.trainer.copy(
                                    availability = selectedHours
                                )
                            )
                        )
                    )
                }
            }
        )

        SocialMediaSelector(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            label = "Social Media",
            enabled = profileState.switchEdit,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            socialMedia = profileState.editUser?.trainer?.socialMediaLinks ?: emptyMap(),
            onSocialMediaSelected = {
                profileViewModel.onEvent(
                    ProfileEvent.EditUser(
                        profileState.editUser?.copy(
                            trainer = profileState.editUser?.trainer?.copy(
                                socialMediaLinks = it
                            )!!
                        )!!
                    )
                )
            },
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
            selectedIndices = profileState.editUser?.trainee?.fitnessGoals ?: emptyList(),
            onItemsSelected = { index, item ->
                profileState.editUser?.let { user ->
                    val updatedGoals = if (user.trainee.fitnessGoals.contains(index)){
                        user.trainee.fitnessGoals.filter { it != index }
                    } else {
                        user.trainee.fitnessGoals + index
                    }
                    profileViewModel.onEvent(
                        ProfileEvent.EditUser(
                            user.copy(
                                trainee = user.trainee.copy(
                                    fitnessGoals = updatedGoals
                                )
                            )
                        )
                    )
                }
            },
        )
        val currentFitnessLevelItems = listOf("Beginner", "Intermediate", "Advanced", "Professional", "Elite", "Olympian")
        LargeDropdownMenu(
            //TODO: Nincs kész rossz a paraméter
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            label = "Current Fitness Level",
            enabled = profileState.switchEdit,
            items = currentFitnessLevelItems,
            selectedIndices = profileState.editUser?.trainee?.currentFitnessLevel?.let { listOf(it) }
                ?: emptyList(),
            onItemSelected = { index, item ->
                profileViewModel.onEvent(
                    ProfileEvent.EditUser(
                        profileState.editUser?.copy( // FIXME: !! not safe call
                            trainee = profileState.editUser?.trainee?.copy(
                                currentFitnessLevel = index
                            )!!
                        )!!
                    )
                )

            },
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
                selected = profileState.editUser?.trainee?.trainingPreference == 1,
                onClick = {
                    profileViewModel.onEvent(
                        ProfileEvent.EditUser(
                            profileState.editUser?.copy(
                                trainee = profileState.editUser?.trainee?.copy(
                                    trainingPreference = if (profileState.editUser?.trainee?.trainingPreference == 1) 0 else 1
                                )!!
                            )!!
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
                selected = profileState.editUser?.trainee?.trainingPreference == 2,
                onClick = {
                    profileViewModel.onEvent(
                        ProfileEvent.EditUser(
                            profileState.editUser?.copy(
                                trainee = profileState.editUser?.trainee?.copy(
                                    trainingPreference = if (profileState.editUser?.trainee?.trainingPreference == 2) 0 else 2
                                )!!
                            )!!
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
        var healthInformation = listOf("Healthy", "Cardiovascular Disease", "Respiratory Conditions", "Musculoskeletal Issues", "Endocrine Disorders", "Neurological Conditions", "Digestive Disorders", "Mental Health Conditions", "Allergies", "Autoimmune Diseases", "Back Pain", "Knee Problems", "Shoulder Pain", "Neck Pain", "Plantar Fasciitis", "Carpal Tunnel Syndrome", "Pregnancy", "Postpartum", "History of Injuries", "Surgeries", "Medications", "Smoking Status", "Alcohol Consumption")
        LargeDropdownMenu(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            label = "Health Information",
            items = healthInformation,
            selectedIndices = profileState.editUser?.trainee?.healthInformation ?: emptyList(),
            multiSelect = true,
            onItemsSelected = { index, item ->
                profileState.editUser?.let {
                    val updatedHealthInf = if (it.trainee.healthInformation.contains(index)){
                        it.trainee.healthInformation.filter { it != index }
                    } else {
                        it.trainee.healthInformation + index
                    }
                    profileViewModel.onEvent(
                        ProfileEvent.EditUser(
                            it.copy(
                                trainee = it.trainee.copy(
                                    healthInformation = updatedHealthInf
                                )
                            )
                        )
                    )
                }
            },
        )
        var dietInf = listOf("None", "Vegetarian", "Vegan", "Gluten-Free", "Dairy-Free", "Lactose Intolerant", "Nut Allergy", "Peanut Allergy", "Tree Nut Allergy", "Soy Allergy", "Egg Allergy", "Shellfish Allergy", "Fish Allergy", "Wheat Allergy", "Other Allergies", "Low-Carb", "Ketogenic (Keto)", "Paleo", "Whole30", "Low-Sodium", "Low-Sugar", "High-Protein", "Kosher", "Halal", "Weight Watchers", "Other Restrictions", "Prefer Not to Say")
        LargeDropdownMenu(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            label = "Dietary Information / Restrictions",
            items = dietInf,
            selectedIndices = profileState.editUser?.trainee?.dietaryPreferences ?: emptyList(),
            multiSelect = true,
            onItemsSelected = { index, item ->
                profileState.editUser?.let {
                    val updatedDietInf = if (it.trainee.dietaryPreferences.contains(index)){
                        it.trainee.dietaryPreferences.filter { it != index }
                    } else {
                        it.trainee.dietaryPreferences + index
                    }
                    profileViewModel.onEvent(
                        ProfileEvent.EditUser(
                            it.copy(
                                trainee = it.trainee.copy(
                                    dietaryPreferences = updatedDietInf
                                )
                            )
                        )
                    )
                }
            },
        )
        var equipment = listOf("None", "Dumbbells", "Kettlebells", "Barbell", "Bench", "Pull-Up Bar", "Resistance Bands", "Medicine Ball", "Stability Ball", "Yoga Mat", "Foam Roller", "TRX", "Box", "Bosu Ball", "Plyo Box", "Jump Rope", "Treadmill", "Stationary Bike", "Rowing Machine", "Elliptical", "Stair Climber", "Other Cardio Equipment", "Other Strength Equipment", "Other")
        LargeDropdownMenu(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            label = "Equipment Available",
            items = equipment,
            selectedIndices = profileState.editUser?.trainee?.availableEquipment ?: emptyList(),
            multiSelect = true,
            onItemsSelected = { index, item ->
                profileState.editUser?.let {
                    val updatedEquipment = if (it.trainee.availableEquipment.contains(index)){
                        it.trainee.availableEquipment.filter { it != index }
                    } else {
                        it.trainee.availableEquipment + index
                    }
                    profileViewModel.onEvent(
                        ProfileEvent.EditUser(
                            it.copy(
                                trainee = it.trainee.copy(
                                    availableEquipment = updatedEquipment
                                )
                            )
                        )
                    )
                }
            },
        )

    }
}


