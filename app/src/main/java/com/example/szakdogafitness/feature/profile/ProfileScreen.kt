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
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.GridLayout.spec
import android.widget.Space
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.AsyncImagePainter.State.Empty.painter
import coil3.compose.rememberAsyncImagePainter
import com.example.szakdogafitness.R
import com.example.szakdogafitness.domain.model.Trainer
import com.example.szakdogafitness.feature.setup.DatePick
import com.example.szakdogafitness.feature.setup.toMillis
import com.example.szakdogafitness.feature.setup.toTimestamp
import com.example.szakdogafitness.feature.setup.toTriple
import com.example.szakdogafitness.ui.common.HeightSelector
import com.example.szakdogafitness.ui.common.LargeDropdownMenu
import com.example.szakdogafitness.ui.common.NormalTextField
import com.example.szakdogafitness.ui.common.SocialMediaSelector
import com.example.szakdogafitness.ui.common.TrainerTimetableView
import com.example.szakdogafitness.ui.common.WeightSelector
import com.example.szakdogafitness.ui.common.appbars.TopBar
import com.example.szakdogafitness.ui.components.PhoneNumberVisualTransformation
import com.example.szakdogafitness.ui.components.countryCodeToFlagEmoji
import com.example.szakdogafitness.ui.components.extractRegionCode
import com.example.szakdogafitness.viewmodel.AppBarEvent
import com.example.szakdogafitness.viewmodel.AppBarViewModel
import com.example.szakdogafitness.viewmodel.ProfileEvent
import com.example.szakdogafitness.viewmodel.ProfileViewModel
import com.example.szakdogafitness.viewmodel.SetupEvent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.play.integrity.internal.f
import com.google.firebase.Timestamp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
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

    val permissionState = rememberPermissionState(
        permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
    )
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                profileViewModel.onEvent(
                    ProfileEvent.UploadProfilePicture(it)
                )
            }
        }
    )

    LaunchedEffect(Unit) {
        profileViewModel.onEvent(ProfileEvent.GetUser)
        delay(100) //fixme: DELAY
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
//                if (painterState is AsyncImagePainter.State.Success){
//                    Box(
//                        contentAlignment = Alignment.Center,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable{
//                                when {
//                                    permissionState.status.isGranted -> {
//                                        launcher.launch("image/*")
//                                    }
//                                    permissionState.status.shouldShowRationale -> {
//                                        permissionState.launchPermissionRequest()
//                                    }
//                                    else -> {
//                                        permissionState.launchPermissionRequest()
//                                    }
//                                }
//                            }
//                    ) {
//                        Image(
//                            painter = painter,
//                            contentDescription = "Profile Image",
//                            modifier = Modifier
//                                .size(100.dp)
//                                .clip(CircleShape),
//                            contentScale = ContentScale.Crop
//                        )
//                    }
//                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .clickable{
                            when {
                                permissionState.status.isGranted -> {
                                    launcher.launch("image/*")
                                }
                                permissionState.status.shouldShowRationale -> {
                                    permissionState.launchPermissionRequest()
                                }
                                else -> {
                                    permissionState.launchPermissionRequest()
                                }
                            }
                        }
                ) {
                    when(painterState){
                        AsyncImagePainter.State.Empty -> {
                            Image(
                                painter = painterResource(id = R.drawable.ic_profile),
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        is AsyncImagePainter.State.Error -> {
                            Image(
                                painter = painterResource(id = R.drawable.ic_error),
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        is AsyncImagePainter.State.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                            )
                        }
                        is AsyncImagePainter.State.Success -> {
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
                        value = profileState.editUser?.dateOfBirth?.toTriple()?.let { (first, second, third) ->
                            "$first / $second / $third"
                        } ?: "",
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
    val context = LocalContext.current

    var heightOpened by remember { mutableStateOf(false) }
    var weightOpened by remember { mutableStateOf(false) }

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
                selected = profileState.editUser?.trainee?.unit == 1,
                onClick = {
                    profileViewModel.onEvent(
                        ProfileEvent.EditUser(
                            profileState.editUser?.copy(
                                trainee = profileState.editUser?.trainee?.copy(
                                    unit = 1
                                )!!
                            )!!
                        )
                    )
                },
                enabled = profileState.switchEdit,
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 1),
            ) {
                Text(text = "kg, cm")
            }
            Spacer(modifier = Modifier.width(48.dp))
            SegmentedButton(
                selected = profileState.editUser?.trainee?.unit == 2,
                onClick = {
                    profileViewModel.onEvent(
                        ProfileEvent.EditUser(
                            profileState.editUser?.copy(
                                trainee = profileState.editUser?.trainee?.copy(
                                    unit = 2
                                )!!
                            )!!
                        )
                    )
                },
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
            if(heightOpened){
                var heightDialog by remember { mutableStateOf(profileState.editUser?.trainee?.height?:0) }
                Dialog(
                    onDismissRequest = {heightOpened = false},
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
                            unit = profileState.editUser?.trainee?.unit == 1,
                            onHeightSelected = { cm ->
                                heightDialog = cm
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            heightCm = profileState.editUser?.trainee?.height ?: 0,
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
                                    profileViewModel.onEvent(
                                        ProfileEvent.EditUser(
                                            profileState.editUser?.copy(
                                                trainee = profileState.editUser?.trainee?.copy(
                                                    height = heightDialog
                                                )!!
                                            )!!
                                        )
                                    )
                                    heightOpened = false
                                }
                            ) {
                                Text("Ok")
                            }
                        }
                    }

                }
            }
            NormalTextField(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = {
                        if (profileState.switchEdit)
                            heightOpened = true
                    }),
                label = "Height",
                onValueChange = {},
                value = if (profileState.editUser?.trainee?.height == 0)
                    ""
                else if (profileState.editUser?.trainee?.unit == 1)
                    "${profileState.editUser?.trainee?.height} cm"
                else if (profileState.editUser?.trainee?.unit == 2) {
                    val totalInches = (profileState.editUser?.trainee?.height?.div(2.54))?.toInt() ?: 0
                    val feet = totalInches / 12
                    val inches = totalInches % 12
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
                    .clickable(onClick = {
                        if (profileState.switchEdit)
                            weightOpened = true
                    }),
                label = "Weight",
                onValueChange = {},
                value = if (profileState.editUser?.trainee?.unit == 1)
                    (profileState.editUser?.trainee?.weight?.let { String.format("%.1f", it) }) ?: ""
                        else
                    (profileState.editUser?.trainee?.weight?.times(2.2)?.let { String.format("%.1f", it) }) ?: "",
                singleLine = true,
                enabled = false,
                readOnly = true,
                onDone = {},
                leadingIcon = null,
                trailingIcon = {
                    Text(
                        text = if (profileState.editUser?.trainee?.weight == 0.0)
                            ""
                        else if (profileState.editUser?.trainee?.unit == 1)
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
            if (weightOpened){
                Dialog(
                    onDismissRequest = {weightOpened = false}
                ) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        var weightDialog by remember { mutableStateOf(profileState.editUser?.trainee?.weight) }
                        WeightSelector(
                            width = 75.dp,
                            itemHeight = 50.dp,
                            textStyle = MaterialTheme.typography.bodyLarge,
                            textColor = Color.Gray,
                            unit = profileState.editUser?.trainee?.unit == 1,
                            weight = profileState.editUser?.trainee?.weight ?: 0.0,
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
                                    Log.d("ProfileScreen", "Weight: $weightDialog")
                                    profileViewModel.onEvent(
                                        ProfileEvent.EditUser(
                                            profileState.editUser?.copy(
                                                trainee = profileState.editUser?.trainee?.copy(
                                                    weight = (if (profileState.editUser?.trainee?.unit?.equals(1) == true) weightDialog
                                                        else
                                                        weightDialog?.div(2.2))?.let {String.format("%.1f", it).toDouble()}
                                                            ?: 0.0
                                                )!!
                                            )!!
                                        )
                                    )
                                    weightOpened = false
                                }
                            ) {
                                Text("Ok")
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            val trainingItems = listOf("1", "2", "3", "4", "5")

            LargeDropdownMenu( //TODO: Nincs kész rossz a paraméter
                modifier = Modifier
                    .weight(1f),
                label = "Training / Week",
                items = trainingItems,
                enabled = profileState.switchEdit,
                selectedIndices = profileState.editUser?.trainee?.trainings.let { listOf(it?.minus(1) ?: 0) },
                onItemSelected = { index, items ->
                    profileViewModel.onEvent(
                        ProfileEvent.EditUser(
                            profileState.editUser?.copy(
                                trainee = profileState.editUser?.trainee?.copy(
                                    trainings = index + 1
                                )!!
                            )!!
                        )
                    )
                },
            )
            /*NormalTextField(
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
            )*/
        }
        val fitnessGoalList = context.resources.getStringArray(R.array.fitness_goal).toList()
        LargeDropdownMenu(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            label = "Fitness Goals",
            multiSelect = true,
            enabled = profileState.switchEdit,
            items = fitnessGoalList,
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
        val currentFitnessLevelList = context.resources.getStringArray(R.array.current_fitness_level).toList()
        LargeDropdownMenu(
            //TODO: Nincs kész rossz a paraméter
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            label = "Current Fitness Level",
            enabled = profileState.switchEdit,
            items = currentFitnessLevelList,
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
        val healtInformationList = context.resources.getStringArray(R.array.health_information).toList()
        LargeDropdownMenu(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            label = "Health Information",
            items = healtInformationList,
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
        val dietInfList = context.resources.getStringArray(R.array.diet_information).toList()
        LargeDropdownMenu(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            label = "Dietary Information / Restrictions",
            items = dietInfList,
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
        val equipmentList = context.resources.getStringArray(R.array.equipment).toList()
        LargeDropdownMenu(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            label = "Equipment Available",
            items = equipmentList,
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


