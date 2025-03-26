package com.example.szakdogafitness.feature.trainee

import android.R.attr.text
import android.R.attr.top
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.example.szakdogafitness.R
import com.example.szakdogafitness.domain.model.User
import com.example.szakdogafitness.feature.setup.toTriple
import com.example.szakdogafitness.viewmodel.calculateAge
import com.google.firebase.Timestamp

@Composable
fun TraineeBioScreen(
    modifier: Modifier = Modifier,
    data: User
) {

    val painter = rememberAsyncImagePainter(data.profileImageUrl)
    val painterState by painter.state.collectAsState()
    val context = LocalContext.current

    Column (
        modifier = modifier
            .padding(top = 8.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth(0.95f),
            horizontalArrangement = Arrangement.SpaceAround
        ){
            Column(
                modifier = Modifier
                    .padding(8.dp)
                ,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (painterState) {
                    AsyncImagePainter.State.Empty -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(150.dp)
                        )
                    }
                    is AsyncImagePainter.State.Error -> {
                        Image(
                            painter = painterResource(id = android.R.drawable.ic_menu_report_image),
                            contentDescription = "Error",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(shape = RoundedCornerShape(16.dp))
                        )
                    }
                    is AsyncImagePainter.State.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(150.dp)
                        )
                    }
                    is AsyncImagePainter.State.Success -> {
                        Image(
                            painter = painter,
                            contentDescription = "Trainer",
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(16.dp))
                                .size(150.dp)
                            ,
                            contentScale = ContentScale.Crop
                        )
                    }
                } // FIXME: ERROR IMAGE REPLACE
                val tripleDOB = data.dateOfBirth?.toTriple()
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "${tripleDOB?.third}. ${tripleDOB?.second}. ${tripleDOB?.first}",
                    fontSize = 20.sp,
                )
            }
            Column (
                modifier = Modifier
                    .padding(8.dp)
                    .heightIn(150.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                val fitnessGoalsList = context.resources.getStringArray(R.array.fitness_goal).toList()
                Text(
                    text = data.trainee.fitnessGoals.joinToString(", ") { fitnessGoalsList[it] },
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                    fontSize = 16.sp
                )
                val fitnessLevelList = context.resources.getStringArray(R.array.current_fitness_level).toList()
                Text(
                    text = data.trainee.currentFitnessLevel.toString().let { fitnessLevelList[it.toInt()] },
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                    fontSize = 16.sp
                )
                Text(
                    text = data.trainee.trainingPreference.toString(),
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                    fontSize = 16.sp
                )
                val availableEquipmentList = context.resources.getStringArray(R.array.equipment).toList()
                Text(
                    text = data.trainee.availableEquipment.joinToString(", ") { availableEquipmentList[it] },
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                    fontSize = 16.sp
                )
            }
        }
        Spacer(modifier = Modifier.size(32.dp))
        Column (
            modifier = Modifier
                .fillMaxWidth(0.95f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = data.bio,
                fontSize = 16.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }


}