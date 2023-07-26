package com.example.ahealthychallenge.presentation.screen.points

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.presentation.theme.HealthConnectBlue
import com.example.ahealthychallenge.presentation.theme.HealthConnectGreen
import com.example.ahealthychallenge.presentation.utils.NavigationType
import com.himanshoe.charty.common.axis.AxisConfig
import com.himanshoe.charty.line.CurveLineChart
import com.himanshoe.charty.line.LineChart
import com.himanshoe.charty.line.config.CurveLineConfig
import com.himanshoe.charty.line.model.LineData
import com.himanshoe.charty.pie.PieChart
import com.himanshoe.charty.pie.config.PieConfig
import com.himanshoe.charty.pie.config.PieData
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun PointStatScreen(
    navigationType: NavigationType,
    pieData: List<PieData>,
    curveLineData: List<LineData>,
    walkingLineData: List<LineData>,
    runningLineData: List<LineData>,
    bikingLineData: List<LineData>,
    workoutLineData: List<LineData>
) {
    val lineChartData = listOf(
        LineData(10F, 35F),
        LineData(20F, 25F),
        LineData(10F, 50F),
        LineData(80F, 10F),
        LineData(10F, 15F),
        LineData(50F, 100F),
        LineData(20F, 25F),
    )
//TODO: manage the case in which there is no exercise session yet

    if (navigationType == NavigationType.BOTTOM_NAVIGATION) {
        CompactPointStatScreen(
            pieData = pieData,
            curveLineData = curveLineData,
            walkingLineData = walkingLineData,
            runningLineData = runningLineData,
            bikingLineData = bikingLineData,
            workoutLineData = workoutLineData
        )
    } else {
        ExpendedPointStatScreen(
            navigationType = navigationType,
            pieData = pieData,
            curveLineData = curveLineData,
            walkingLineData = walkingLineData,
            runningLineData = runningLineData,
            bikingLineData = bikingLineData,
            workoutLineData = workoutLineData
        )
    }


}

@Composable
fun CompactPointStatScreen(
    pieData: List<PieData>,
    curveLineData: List<LineData>,
    walkingLineData: List<LineData>,
    runningLineData: List<LineData>,
    bikingLineData: List<LineData>,
    workoutLineData: List<LineData>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        if (pieData.isNotEmpty()) {
            item(key = 1) {
                PieChart(
                    modifier = Modifier
                        .fillMaxSize(),
                    pieData = pieData,
                    config = PieConfig(isDonut = true, expandDonutOnClick = true),
                    onSectionClicked = { percent, value ->
                        Log.d("point", "value: $value")
                        Log.d("point", "percent: $percent")
                    }
                )
            }
        } else {
            item(key = 1) {
                PieChart(
                    modifier = Modifier
                        .fillMaxSize(),
                    pieData = listOf(PieData(1F)),
                    config = PieConfig(isDonut = true, expandDonutOnClick = true),
                )
            }
        }
        item(key = 2) {
            Text(
                text = stringResource(R.string.points_per_exercise_type),
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                textAlign = TextAlign.Center
            )
        }

        if (curveLineData.isNotEmpty()) {
            item(key = 3) {
                LineChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    color = HealthConnectBlue,
                    lineData = curveLineData,
                    axisConfig = AxisConfig(
                        showAxis = true,
                        showUnitLabels = true,
                        isAxisDashed = true,
                        showXLabels = true,
                        textColor = HealthConnectBlue,
                        xAxisColor = HealthConnectBlue,
                        yAxisColor = HealthConnectGreen
                    )
                )
            }
        } else {
            item(key = 3) {
                LineChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    color = HealthConnectBlue,
                    lineData = listOf(LineData(0, 0F)),
                    axisConfig = AxisConfig(
                        showAxis = true,
                        showUnitLabels = true,
                        isAxisDashed = true,
                        showXLabels = true,
                        textColor = HealthConnectBlue,
                        xAxisColor = HealthConnectBlue,
                        yAxisColor = HealthConnectGreen
                    )
                )
            }
        }
        item(key = 4) {
            Text(
                text = stringResource(R.string.points_this_month),
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                textAlign = TextAlign.Center
            )
        }

        /*walking*/
        if (walkingLineData.isNotEmpty()) {
            item(key = 5) {
                LineChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    color = HealthConnectGreen,
                    lineData = walkingLineData,
                    axisConfig = AxisConfig(
                        showAxis = true,
                        showUnitLabels = true,
                        isAxisDashed = true,
                        showXLabels = true,
                        textColor = HealthConnectBlue,
                        xAxisColor = HealthConnectBlue,
                        yAxisColor = HealthConnectGreen
                    )
                )
            }
            item(key = 6) {
                Text(
                    text = stringResource(R.string.points_walking),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        /*running*/
        if (runningLineData.isNotEmpty()) {
            item(key = 7) {
                LineChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    color = HealthConnectGreen,
                    lineData = runningLineData,
                    axisConfig = AxisConfig(
                        showAxis = true,
                        showUnitLabels = true,
                        isAxisDashed = true,
                        showXLabels = true,
                        textColor = HealthConnectBlue,
                        xAxisColor = HealthConnectBlue,
                        yAxisColor = HealthConnectGreen
                    )
                )
            }
            item(key = 8) {
                Text(
                    text = stringResource(R.string.points_running),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        /*biking*/
        if (bikingLineData.isNotEmpty()) {

            item(key = 9) {
                LineChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    color = HealthConnectGreen,
                    lineData = bikingLineData,
                    axisConfig = AxisConfig(
                        showAxis = true,
                        showUnitLabels = true,
                        isAxisDashed = true,
                        showXLabels = true,
                        textColor = HealthConnectBlue,
                        xAxisColor = HealthConnectBlue,
                        yAxisColor = HealthConnectGreen
                    )
                )
            }
            item(key = 10) {
                Text(
                    text = stringResource(R.string.points_biking),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        /*working out*/
        if (workoutLineData.isNotEmpty()) {
            item(key = 11) {
                LineChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    color = HealthConnectGreen,
                    lineData = workoutLineData,
                    axisConfig = AxisConfig(
                        showAxis = true,
                        showUnitLabels = true,
                        isAxisDashed = true,
                        showXLabels = true,
                        textColor = HealthConnectBlue,
                        xAxisColor = HealthConnectBlue,
                        yAxisColor = HealthConnectGreen
                    )
                )
            }
            item(key = 12) {
                Text(
                    text = stringResource(R.string.points_working_out),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Composable
fun ExpendedPointStatScreen(
    navigationType: NavigationType,
    pieData: List<PieData>,
    curveLineData: List<LineData>,
    walkingLineData: List<LineData>,
    runningLineData: List<LineData>,
    bikingLineData: List<LineData>,
    workoutLineData: List<LineData>
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(30.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(50.dp),
    ) {
        if (pieData.isNotEmpty()) {
            item {
                Column {
                    PieChart(
                        modifier = Modifier
                            .fillMaxSize(),
                        pieData = pieData,
                        config = PieConfig(isDonut = true, expandDonutOnClick = true),
                        onSectionClicked = { percent, value ->
                            Log.d("point", "value: $value")
                            Log.d("point", "percent: $percent")
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Points per exercise type",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

        } else {
            item {
                Column {
                    PieChart(
                        modifier = Modifier
                            .fillMaxSize(),
                        pieData = listOf(PieData(1F)),
                        config = PieConfig(isDonut = true, expandDonutOnClick = true),
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Points per exercise type",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if (curveLineData.isNotEmpty()) {
            item {
                Column() {
                    LineChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        color = HealthConnectBlue,
                        lineData = curveLineData,
                        axisConfig = AxisConfig(
                            showAxis = true,
                            showUnitLabels = true,
                            isAxisDashed = true,
                            showXLabels = true,
                            textColor = HealthConnectBlue,
                            xAxisColor = HealthConnectBlue,
                            yAxisColor = HealthConnectGreen
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Points per exercise type",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            item {
                Column() {
                    LineChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        color = HealthConnectBlue,
                        lineData = listOf(LineData(0, 0F)),
                        axisConfig = AxisConfig(
                            showAxis = true,
                            showUnitLabels = true,
                            isAxisDashed = true,
                            showXLabels = true,
                            textColor = HealthConnectBlue,
                            xAxisColor = HealthConnectBlue,
                            yAxisColor = HealthConnectGreen
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Points per exercise type",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if (walkingLineData.isNotEmpty()) {
            item {
                Column() {
                    LineChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        color = HealthConnectGreen,
                        lineData = walkingLineData,
                        axisConfig = AxisConfig(
                            showAxis = true,
                            showUnitLabels = true,
                            isAxisDashed = true,
                            showXLabels = true,
                            textColor = HealthConnectBlue,
                            xAxisColor = HealthConnectBlue,
                            yAxisColor = HealthConnectGreen
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Points walking",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if (runningLineData.isNotEmpty()) {
            item {
                Column() {
                    LineChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        color = HealthConnectGreen,
                        lineData = runningLineData,
                        axisConfig = AxisConfig(
                            showAxis = true,
                            showUnitLabels = true,
                            isAxisDashed = true,
                            showXLabels = true,
                            textColor = HealthConnectBlue,
                            xAxisColor = HealthConnectBlue,
                            yAxisColor = HealthConnectGreen
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Points running",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if (bikingLineData.isNotEmpty()) {
            item {
                Column() {
                    LineChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        color = HealthConnectGreen,
                        lineData = bikingLineData,
                        axisConfig = AxisConfig(
                            showAxis = true,
                            showUnitLabels = true,
                            isAxisDashed = true,
                            showXLabels = true,
                            textColor = HealthConnectBlue,
                            xAxisColor = HealthConnectBlue,
                            yAxisColor = HealthConnectGreen
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Points biking",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if (workoutLineData.isNotEmpty()) {
            item {
                Column() {
                    LineChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        color = HealthConnectGreen,
                        lineData = workoutLineData,
                        axisConfig = AxisConfig(
                            showAxis = true,
                            showUnitLabels = true,
                            isAxisDashed = true,
                            showXLabels = true,
                            textColor = HealthConnectBlue,
                            xAxisColor = HealthConnectBlue,
                            yAxisColor = HealthConnectGreen
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Points working out",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}