package com.example.szakdogafitness.feature.setup

import android.R.attr.bottom
import android.R.attr.button
import android.R.attr.end
import android.R.attr.fontWeight
import android.R.attr.onClick
import android.R.attr.phoneNumber
import android.R.attr.top
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.szakdogafitness.ui.common.DateSelector
import com.example.szakdogafitness.ui.common.NormalTextField
import com.example.szakdogafitness.ui.common.ProgressBar
import com.example.szakdogafitness.ui.components.DateTransformation
import com.example.szakdogafitness.ui.components.PhoneNumberVisualTransformation
import com.example.szakdogafitness.ui.components.countryCodeToFlagEmoji
import com.example.szakdogafitness.ui.components.extractRegionCode
import com.example.szakdogafitness.viewmodel.SetupEvent
import com.example.szakdogafitness.viewmodel.SetupViewModel
import com.google.firebase.Timestamp
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    setupViewModel: SetupViewModel
) {
    val textFieldModifier = Modifier
        .fillMaxWidth(0.9f)
        .padding(top = 8.dp, bottom = 8.dp)

    //var firstName by remember { mutableStateOf("") }
    //var lastName by remember { mutableStateOf("") }
    //var dateOfBirth by remember { mutableStateOf("") }
    //var dateOfBirthDialog by remember { mutableStateOf("") }
    var isDateOfBirthVisible by remember { mutableStateOf(false) }
    //var gender by remember {mutableStateOf(0)}
    //var role by remember {mutableStateOf(0)}
    //var phoneNumber by remember { mutableStateOf("") }
    val state by setupViewModel.state.collectAsState()




    ConstraintLayout (
        modifier = modifier
            .padding(paddingValues)
            //.fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){
        var (bar, label, fName, lName, dob, gend, label2, label3, label4, label5, ro, phone, button) = createRefs()

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
        )*/
        /*Text(
            text = "Who are you?",
            fontSize = 24.sp,
            modifier = Modifier
                .constrainAs(label) {
                    //top.linkTo(bar.bottom)
                    top.linkTo(parent.top)
                    start.linkTo(startGuideline5)
                },
            fontWeight = Bold
        )*/
        NormalTextField(
            modifier = textFieldModifier
                .constrainAs(fName) {
                    //top.linkTo(label.bottom)
                    top.linkTo(parent.top)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                }
            ,
            label = "First name",
            onValueChange = {
                setupViewModel.onEvent(SetupEvent.FirstNameChanged(it))
            },
            value = state.firstName,
            singleLine = true,
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
            modifier = textFieldModifier
                .constrainAs(lName) {
                    top.linkTo(fName.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Last name",
            onValueChange = {
                setupViewModel.onEvent(SetupEvent.LastNameChanged(it))
            },
            value = state.lastName,
            singleLine = true,
            onDone = {},
            leadingIcon = null,
            trailingIcon = null,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
            ),
        )
        var testDOB by remember { mutableStateOf("") }
        NormalTextField(
            modifier = textFieldModifier
                .clickable(onClick = { isDateOfBirthVisible = true })
                .constrainAs(dob) {
                    top.linkTo(lName.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
            label = "Date of Birth",
            onValueChange = {
                if (it.length <= 8) testDOB = it
            },
            value = state.dateOfBirth?.toTriple().toString(),
            //value = testDOB,
            singleLine = true,
            enabled = false,
            readOnly = true,
            //visualTransformation = DateTransformation(),
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
        SingleChoiceSegmentedButtonRow (
            modifier = textFieldModifier
                .constrainAs(gend) {
                    top.linkTo(dob.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                }
        ) {
            SegmentedButton(
                selected = state.gender == 1,
                onClick = {
                    //gender = 1
                    setupViewModel.onEvent(SetupEvent.GenderChanged(1))
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
                selected = state.gender == 2,
                onClick = {
                    //gender = 2
                    setupViewModel.onEvent(SetupEvent.GenderChanged(2))
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
        val countryCode = extractRegionCode(state.phoneNumber)
        val flagEmoji = countryCode?.let { countryCodeToFlagEmoji(it) }
        NormalTextField(
            label = "Phone number",
            singleLine = true,
            value = state.phoneNumber,
            onValueChange = {
                setupViewModel.onEvent(SetupEvent.PhoneNumberChanged(it))
            },
            visualTransformation = PhoneNumberVisualTransformation(),
            modifier = textFieldModifier
                .constrainAs(phone) {
                    top.linkTo(gend.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                },
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
        Text(
            "Who are you?",
            fontSize = 24.sp,
            modifier = Modifier
                .padding(8.dp)
                .constrainAs(label2) {
                    top.linkTo(phone.bottom)
                    start.linkTo(parent.start, margin = 16.dp)
                },
            fontWeight = Bold
        )
        SingleChoiceSegmentedButtonRow (
            modifier = textFieldModifier
                .constrainAs(ro) {
                    top.linkTo(label2.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                }
        ) {
            SegmentedButton(
                selected = state.role == 1,
                onClick = {
                    //role = 1
                    setupViewModel.onEvent(SetupEvent.RoleChanged(1))
                },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 1),
                icon = {},
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = Color(0xFF00FF00)
                )
            ) {
                Text(text = "Trainer")
            }
            Spacer(modifier = Modifier.width(48.dp))
            SegmentedButton(
                selected = state.role == 2,
                onClick = {
                    //role = 2
                    setupViewModel.onEvent(SetupEvent.RoleChanged(2))
                },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 1),
                icon = {},
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = Color(0xFF00FF00)
                )
            ) {
                Text(text = "Trainee")
            }
        }
        if (isDateOfBirthVisible){
            DatePick(
                date = state.dateOfBirth?.toMillis() ?: System.currentTimeMillis(),
                selectRange = Timestamp.now().toMillis(),
                onDateSelected = {
                    setupViewModel.onEvent(SetupEvent.DateOfBirthChanged(it!!.toTimestamp()))
                },
            ) {
                isDateOfBirthVisible = false
            }
            /*Dialog(
                onDismissRequest = { isDateOfBirthVisible = false },
            ) {
                Column (
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    DateSelector(
                        width = 75.dp,
                        itemHeight = 50.dp,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        textColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        date = state.dateOfBirth?.toMillis() ?: System.currentTimeMillis()
                    ) { day, month, year ->
                        val timestamp = Triple(day, month, year).toTimestamp()
                        setupViewModel.onEvent(SetupEvent.DateOfBirthChanged(timestamp))
                    }
                }
            }*/
        }
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

        val (v1, v2) = createRefs()
        VerticalDivider(
            modifier = Modifier
                .constrainAs(v1) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(startGuideline5)
                },
            color = Color.Gray,
            thickness = 2.dp
        )
        VerticalDivider(
            modifier = Modifier
                .constrainAs(v2) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(endGuideline95)
                },
            color = Color.Gray,
            thickness = 2.dp
        )

    }

    /*Column (
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ProgressBar(
            progress = 0.12f
        )
        Text(
            text = "Who are you?",
            fontSize = 24.sp,
            modifier = Modifier.padding(8.dp),
            fontWeight = Bold
        )
        NormalTextField(
            modifier = textFieldModifier,
            label = "First name",
            onValueChange = {},
            value = "",
            singleLine = true,
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
            modifier = textFieldModifier,
            label = "Last name",
            onValueChange = {},
            value = "",
            singleLine = true,
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
            modifier = textFieldModifier.clickable(onClick = { isDateOfBirthVisible = true }),
            label = "Date of Birth",
            onValueChange = {},
            value = dateOfBirth,
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
        SingleChoiceSegmentedButtonRow (
            modifier = textFieldModifier
        ) {
            SegmentedButton(
                selected = gender == 1,
                onClick = {
                    gender = 1
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
                selected = gender == 2,
                onClick = {
                    gender = 2
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
        val countryCode = extractRegionCode(phoneNumber)
        val flagEmoji = countryCode?.let { countryCodeToFlagEmoji(it) }
        NormalTextField(
            label = "Phone number",
            singleLine = true,
            value = phoneNumber,
            onValueChange = {
                phoneNumber = it
            },
            visualTransformation = PhoneNumberVisualTransformation(),
            modifier = textFieldModifier,
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
        Text(
            "Who are you?",
            fontSize = 24.sp,
            modifier = Modifier.padding(8.dp),
            fontWeight = Bold
        )
        SingleChoiceSegmentedButtonRow (
            modifier = textFieldModifier
        ) {
            SegmentedButton(
                selected = role == 1,
                onClick = {
                    role = 1
                },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 1),
                icon = {},
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = Color(0xFF00FF00)
                )
            ) {
                Text(text = "Trainer")
            }
            Spacer(modifier = Modifier.width(48.dp))
            SegmentedButton(
                selected = role == 2,
                onClick = {
                    role = 2
                },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 1),
                icon = {},
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = Color(0xFF00FF00)
                )
            ) {
                Text(text = "Trainee")
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = {}
        ) {
            Text("Next")
        }

        if (isDateOfBirthVisible){
            Dialog(
                onDismissRequest = { isDateOfBirthVisible = false },
            ) {
                Column (
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    DateSelector(
                        width = 70.dp,
                        itemHeight = 50.dp,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        textColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        onDateSelected = { day, month, year ->
                            dateOfBirthDialog = "$day/$month/$year"
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(
                            onClick = {
                                isDateOfBirthVisible = false
                            }
                        ) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        TextButton(
                            onClick = {
                                dateOfBirth = dateOfBirthDialog
                                isDateOfBirthVisible = false
                            }
                        ) {
                            Text("Ok")
                        }
                    }
                }
            }
        }
    }*/
}

@Composable
@Preview
fun SetupScreenPreview(){
   /* SetupScreen(
        paddingValues = PaddingValues(0.dp),
        setupViewModel = SetupViewModel(

        )
    )*/
}

fun Long.toTimestamp(): Timestamp {
    val calendar = Calendar.getInstance().apply { timeInMillis = this@toTimestamp }
    return Timestamp(calendar.time)
}

fun Timestamp.toMillis() = this.toDate().time

fun Triple<Int, Int, Int>.toTimestamp(): Timestamp {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, third)
        set(Calendar.MONTH, second - 1)
        set(Calendar.DAY_OF_MONTH, first)
    }
    return Timestamp(calendar.time)
}

fun Timestamp.toTriple(): Triple<Int, Int, Int> {
    val calendar = Calendar.getInstance().apply { time = this@toTriple.toDate() }
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    return Triple(day, month, year)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePick(
    date: Long,
    selectRange: Long,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {

    val year = selectRange.toTimestamp().toTriple().third + 1


    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date,
        yearRange = (1900 until year),
        selectableDates = object : SelectableDates{
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= selectRange
            }
        }
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                }
            ) {
                Text("Ok")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }

    ) {
        DatePicker(
            state = datePickerState,
        )
    }
}



