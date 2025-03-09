package com.example.szakdogafitness.feature.lifestyle

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.szakdogafitness.presentation.navigation.root.RootScreenRoute
import com.example.szakdogafitness.ui.common.appbars.LifeStyleAction
import com.example.szakdogafitness.ui.common.appbars.LifeStyleTitle
import com.example.szakdogafitness.ui.common.appbars.TopBar
import com.example.szakdogafitness.ui.common.lifestyle.DailyCaloriesBlock
import com.example.szakdogafitness.ui.common.lifestyle.StepsBlock
import com.example.szakdogafitness.ui.common.lifestyle.WaterIntakeBlock
import com.example.szakdogafitness.ui.common.lifestyle.WeightControlBlock
import com.example.szakdogafitness.viewmodel.AuthenticationEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LifeStyleScreen(
    modifier: Modifier = Modifier,
    //VMs
){
    Scaffold (
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = {
                    LifeStyleTitle(
                        title = "DAYA",
                        onPreviousDayClick = {
                            Log.d("RootNavigation", "onPreviousDayClick")
                        }
                    ) {

                    }

                },
                navigationIcon = {
                    LifeStyleAction {  }
                },
            )
        }
    ){ innerPadding ->
        ConstraintLayout (
            modifier = modifier.padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ){
            val (caloryBlock, waterBlock, weightBlock, stepsBlock) = createRefs()

            DailyCaloriesBlock(
                modifier = Modifier
                    .constrainAs(caloryBlock){
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth(0.9f)
            )
            WaterIntakeBlock(
                modifier = Modifier
                    .constrainAs(waterBlock){
                        top.linkTo(caloryBlock.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth(0.9f),
                consumedWater = 0.0,
                goalWater = 2.5
            )
            WeightControlBlock(
                modifier = Modifier
                    .constrainAs(weightBlock){
                        top.linkTo(waterBlock.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth(0.9f)
            )
            StepsBlock(
                modifier = Modifier
                    .constrainAs(stepsBlock){
                        top.linkTo(weightBlock.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth(0.9f),
                steps = 1234,
                goalSteps = 6000
            )


        }
    }
}

@Composable
@Preview
fun LifeStyleScreenPreview(){
    LifeStyleScreen()
}
