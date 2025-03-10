package com.example.szakdogafitness.ui.common.lifestyle

import android.R.attr.top
import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.szakdogafitness.domain.model.DailyData
import com.example.szakdogafitness.domain.model.Nutrition
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter

@Composable
fun DailyCaloriesBlock(
    modifier: Modifier = Modifier,
    dailyData : DailyData
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = {2}
    )

    val totalCalories = dailyData.foodIntake.values.sumOf { it.sumOf { it.calories} }
    val targetCalories = dailyData.targetKcal

    Column (
        modifier = modifier
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        HorizontalPager(
            state = pagerState,
        ) { page ->
            when (page) {
                0 -> DailyCaloriesFirstPage(
                    totalCalories = totalCalories,
                    targetCalories = targetCalories
                )
                1 -> PieChartComp(
                    totalCalories = totalCalories,
                    targetCalories = targetCalories,
                    totalServings = dailyData.foodIntake.values.sumOf { it.sumOf {it.servingSize} },
                    totalProt = dailyData.foodIntake.values.sumOf { it.sumOf {it.protein} },
                    totalCarbs = dailyData.foodIntake.values.sumOf { it.sumOf {it.carbs} },
                    totalFat = dailyData.foodIntake.values.sumOf { it.sumOf {it.fat} }
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(2) { index ->
                Box(
                    modifier = Modifier
                        .padding(1.dp)
                        .size(10.dp)
                        .background(
                            if (pagerState.currentPage == index) Color.Cyan else Color.Gray,
                            shape = CircleShape
                        )

                )
            }
        }
    }
}

@Composable
@Preview
fun DailyCaloriesBlockPreview() {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DailyCaloriesBlock(
            dailyData = DailyData()
        )
    }
}

@Composable
fun DailyCaloriesFirstPage(
    totalCalories: Int,
    targetCalories: Int,
){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            //.padding(8.dp)
            .heightIn(290.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Text(
                text = "Daily Calories",
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp),
                fontWeight = FontWeight.Bold
            )
        }
        Row (modifier = Modifier.fillMaxWidth()) {
            Column (
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth()
                    .padding(8.dp)
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressBar(
                    percentage = totalCalories.toFloat() / targetCalories.toFloat(),
                    number = targetCalories,
                    fontSize = 30.sp,
                    radius = 100.dp,
                    color = Color.Green,
                    strokeWidth = 16.dp,
                    animDuration = 1000,
                    animDelay = 0
                )
            }
            Column (
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$totalCalories",
                        fontSize = 30.sp,
                        modifier = Modifier.padding(8.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Total kcal",
                        fontSize = 16.sp,
                    )
                }
                Column (
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$targetCalories",
                        fontSize = 30.sp,
                        modifier = Modifier.padding(8.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Goal kcal",
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun DailyCaloriesFirstPagePreview() {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        DailyCaloriesFirstPage(
            totalCalories = 1390,
            targetCalories = 2200
        )
    }
}
data class PieChartData(
    var browserName: String?,
    var value: Float?
)
val getPieChartData = listOf(
    PieChartData("Fat", 35F),
    PieChartData("Carb", 35F),
    PieChartData("Prot", 30F),
)
@Composable
fun PieChartComp(
    totalCalories: Int = 1390,
    targetCalories: Int = 2200,
    totalServings: Int = 0,
    totalProt: Int = 0,
    totalCarbs: Int = 0,
    totalFat: Int = 0
){

    val fat = totalFat.toFloat() / totalServings.toFloat() * 100
    val carbs = totalCarbs.toFloat() / totalServings.toFloat() * 100
    val prot = totalProt.toFloat() / totalServings.toFloat() * 100

    Log.d("PieChartComp", "fat: $fat, carbs: $carbs, prot: $prot")

    val pieChartData = listOf(
        PieChartData("Fat", fat),
        PieChartData("Carb", carbs),
        PieChartData("Prot", prot),
        PieChartData("Other", (100 - (fat + carbs + prot)) )
    )

    val context = LocalContext.current
    Column (
        modifier = Modifier
            .fillMaxWidth()
            //.padding(8.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
            .heightIn(290.dp)
            //.size(240.dp)
        ,

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start
        ){
            Text(
                text = "Macronutrient Breakdown",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Crossfade(
                modifier = Modifier
                    .weight(3f)
                    .size(240.dp),
                //targetState = getPieChartData
                targetState = pieChartData
            ) { pieChartData ->
                AndroidView(
                    factory = { context ->
                        PieChart(context).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                            )
                            this.description.isEnabled = false

                            this.isDrawHoleEnabled = false

                            this.legend.isEnabled = true

                            this.legend.textSize = 14F

                            //this.setUsePercentValues(true)


                            this.setDrawSliceText(false)


                            this.legend.horizontalAlignment =
                                Legend.LegendHorizontalAlignment.LEFT

                            ContextCompat.getColor(context, android.R.color.white)
                        }
                    },
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(5.dp), update = {
                            updatePieChartWithData(
                                context,
                                it,
                                pieChartData
                            )
                        })
            }
            Column (
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$totalCalories",
                        fontSize = 30.sp,
                        modifier = Modifier.padding(8.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Total kcal",
                        fontSize = 16.sp,
                    )
                }
                Column (
                    modifier = Modifier.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$targetCalories",
                        fontSize = 30.sp,
                        modifier = Modifier.padding(8.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Goal kcal",
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }

}

fun updatePieChartWithData(
    context: Context,
    chart: PieChart,
    data: List<PieChartData>
) {
    val entries = ArrayList<PieEntry>()

    for (i in data.indices) {
        val item = data[i]
        entries.add(PieEntry(item.value ?: 0.toFloat(), item.browserName ?: ""))
    }

    val ds = PieDataSet(entries, "")

    ds.colors = arrayListOf(
        ContextCompat.getColor(chart.context, android.R.color.holo_red_light),
        ContextCompat.getColor(chart.context, android.R.color.holo_blue_light),
        ContextCompat.getColor(chart.context, android.R.color.holo_green_light),
        ContextCompat.getColor(chart.context, android.R.color.holo_orange_light)
    )
    ds.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE

    ds.xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE

    ds.sliceSpace = 2f

    ContextCompat.getColor(context, android.R.color.white)

    ds.valueTextSize = 18f

    ds.valueTypeface = Typeface.DEFAULT_BOLD

    val d = PieData(ds)

    chart.data = d

    chart.invalidate()
}

@Composable
@Preview
fun CalorieTrackerScreenPreview() {
    PieChartComp()
}