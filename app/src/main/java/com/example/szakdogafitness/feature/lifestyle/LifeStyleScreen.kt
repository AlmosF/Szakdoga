package com.example.szakdogafitness.feature.lifestyle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.szakdogafitness.ui.common.lifestyle.DailyCaloriesBlock
import com.example.szakdogafitness.ui.common.lifestyle.StepsBlock
import com.example.szakdogafitness.ui.common.lifestyle.WaterIntakeBlock
import com.example.szakdogafitness.ui.common.lifestyle.WeightControlBlock

@Composable
fun LifeStyleScreen(
    modifier: Modifier = Modifier,
    //VMs
){
    ConstraintLayout (
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF2F7FB))
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

@Composable
@Preview
fun LifeStyleScreenPreview(){
    LifeStyleScreen()
}
