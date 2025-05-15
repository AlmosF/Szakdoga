package com.example.szakdogafitness.feature.setup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.szakdogafitness.ui.common.NormalTextField
import com.example.szakdogafitness.ui.components.PhoneNumberVisualTransformation
import com.example.szakdogafitness.ui.components.countryCodeToFlagEmoji
import com.example.szakdogafitness.ui.components.extractRegionCode
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

    var isDateOfBirthVisible by remember { mutableStateOf(false) }

    val state by setupViewModel.state.collectAsState()




    ConstraintLayout (
        modifier = modifier
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
    ){
        var (bar, label, fName, lName, dob, gend, label2, label3, label4, label5, ro, phone, button) = createRefs()

        val startGuideline5 = createGuidelineFromStart(0.05f)
        val startGuideline25 = createGuidelineFromStart(0.25f)
        val endGuideline75 = createGuidelineFromStart(0.75f)
        val endGuideline95 = createGuidelineFromStart(0.95f)

        NormalTextField(
            modifier = textFieldModifier
                .constrainAs(fName) {
                    top.linkTo(parent.top)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                }
            ,
            label = "First name",
            onValueChange = {
                setupViewModel.onEvent(SetupEvent.FirstNameChanged(it))
            },
            value = state.user.firstName,
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
            value = state.user.lastName,
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
            value = state.user.dateOfBirth?.toTriple()?.let { (first, second, third) ->
                "$first / $second / $third"
            } ?: "",
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
                .constrainAs(gend) {
                    top.linkTo(dob.bottom)
                    start.linkTo(startGuideline5)
                    end.linkTo(endGuideline95)
                }
        ) {
            SegmentedButton(
                selected = state.user.gender == 1,
                onClick = {
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
                selected = state.user.gender == 2,
                onClick = {
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
        val countryCode = extractRegionCode(state.user.phoneNumber)
        val flagEmoji = countryCode?.let { countryCodeToFlagEmoji(it) }
        NormalTextField(
            label = "Phone number",
            singleLine = true,
            value = state.user.phoneNumber,
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
                selected = state.user.role == 1,
                onClick = {
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
                selected = state.user.role == 2,
                onClick = {
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
                date = state.user.dateOfBirth?.toMillis() ?: System.currentTimeMillis(),
                selectRange = Timestamp.now().toMillis(),
                onDateSelected = {
                    setupViewModel.onEvent(SetupEvent.DateOfBirthChanged(it!!.toTimestamp()))
                },
            ) {
                isDateOfBirthVisible = false
            }
        }

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



