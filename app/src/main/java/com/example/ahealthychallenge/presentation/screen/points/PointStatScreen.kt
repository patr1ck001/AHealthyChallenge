package com.example.ahealthychallenge.presentation.screen.points

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.presentation.component.CircularProgressBar
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
import kotlin.math.roundToInt

@Composable
fun PointStatScreen(
    pointStatScreenLoading: Boolean,
    navigationType: NavigationType,
    pieData: List<PieData>,
    pieDataMap: Map<String, Int>,
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
            pieDataMap = pieDataMap,
            curveLineData = curveLineData,
            walkingLineData = walkingLineData,
            runningLineData = runningLineData,
            bikingLineData = bikingLineData,
            workoutLineData = workoutLineData,
            pointStatScreenLoading = pointStatScreenLoading
        )
    } else {
        ExpendedPointStatScreen(
            navigationType = navigationType,
            pieData = pieData,
            pieDataMap = pieDataMap,
            curveLineData = curveLineData,
            walkingLineData = walkingLineData,
            runningLineData = runningLineData,
            bikingLineData = bikingLineData,
            workoutLineData = workoutLineData,
            pointStatScreenLoading = pointStatScreenLoading

        )
    }


}

@SuppressLint("UnrememberedMutableState")
@Composable
fun CompactPointStatScreen(
    pointStatScreenLoading: Boolean,
    pieData: List<PieData>,
    pieDataMap: Map<String, Int>,
    curveLineData: List<LineData>,
    walkingLineData: List<LineData>,
    runningLineData: List<LineData>,
    bikingLineData: List<LineData>,
    workoutLineData: List<LineData>
) {
    val isActivityDisplayed = remember { mutableStateOf(false) }
    val valueDisplayed = remember { mutableFloatStateOf(0F) }

    if (!pointStatScreenLoading) {
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
                        onSectionClicked = { _, value ->
                            isActivityDisplayed.value = true
                            valueDisplayed.floatValue = value
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

            if (pieDataMap["walking"] != null && isActivityDisplayed.value) {
                if (pieDataMap["walking"]!!.toFloat() == valueDisplayed.floatValue) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Walking",
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary
                            )
                        }
                    }
                }
            }

            if (pieDataMap["running"] != null && isActivityDisplayed.value) {
                if (pieDataMap["running"]!!.toFloat() == valueDisplayed.floatValue) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Running",
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary
                            )
                        }
                    }
                }
            }
            if (pieDataMap["cycling"] != null && isActivityDisplayed.value) {
                if (pieDataMap["cycling"]!!.toFloat() == valueDisplayed.floatValue) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Biking",
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary
                            )
                        }
                    }
                }
            }
            if (pieDataMap["workout"] != null && isActivityDisplayed.value) {
                if (pieDataMap["workout"]!!.toFloat() == valueDisplayed.floatValue) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Workout",
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colors.primary
                            )
                        }
                    }
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
                            textColor = MaterialTheme.colors.primary,
                            xAxisColor = MaterialTheme.colors.primary,
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
                            textColor = MaterialTheme.colors.primary,
                            xAxisColor = MaterialTheme.colors.primary,
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
                            textColor = MaterialTheme.colors.primary,
                            xAxisColor = MaterialTheme.colors.primary,
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
                            textColor = MaterialTheme.colors.primary,
                            xAxisColor = MaterialTheme.colors.primary,
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
    } else {
        CircularProgressBar(
            isDisplayed = true, Modifier.size(60.dp)
        )
    }
}


@Composable
fun ExpendedPointStatScreen(
    pointStatScreenLoading: Boolean,
    navigationType: NavigationType,
    pieDataMap: Map<String, Int>,
    pieData: List<PieData>,
    curveLineData: List<LineData>,
    walkingLineData: List<LineData>,
    runningLineData: List<LineData>,
    bikingLineData: List<LineData>,
    workoutLineData: List<LineData>
) {
    val isActivityDisplayed = remember { mutableStateOf(false) }
    val valueDisplayed = remember { mutableFloatStateOf(0F) }

    if (!pointStatScreenLoading) {
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
                            onSectionClicked = { _, value ->
                                isActivityDisplayed.value = true
                                valueDisplayed.floatValue = value
                            }
                        )
                        if (pieDataMap["walking"] != null && isActivityDisplayed.value) {
                            if (pieDataMap["walking"]!!.toFloat() == valueDisplayed.floatValue) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Walking",
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colors.primary
                                    )
                                }

                            }
                        }


                        if (pieDataMap["running"] != null && isActivityDisplayed.value) {
                            if (pieDataMap["running"]!!.toFloat() == valueDisplayed.floatValue) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Running",
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colors.primary
                                    )
                                }
                            }
                        }

                        if (pieDataMap["cycling"] != null && isActivityDisplayed.value) {
                            if (pieDataMap["cycling"]!!.toFloat() == valueDisplayed.floatValue) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Biking",
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colors.primary
                                    )
                                }
                            }
                        }
                        if (pieDataMap["workout"] != null && isActivityDisplayed.value) {
                            if (pieDataMap["workout"]!!.toFloat() == valueDisplayed.floatValue) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Workout",
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colors.primary
                                    )
                                }
                            }
                        }
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
                            text = stringResource(R.string.points_this_month),
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
                                textColor = MaterialTheme.colors.primary,
                                xAxisColor = MaterialTheme.colors.primary,
                                yAxisColor = MaterialTheme.colors.primary
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
                                textColor = MaterialTheme.colors.primary,
                                xAxisColor = MaterialTheme.colors.primary,
                                yAxisColor = MaterialTheme.colors.primary
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
                                textColor = MaterialTheme.colors.primary,
                                xAxisColor = MaterialTheme.colors.primary,
                                yAxisColor = MaterialTheme.colors.primary
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
                                textColor = MaterialTheme.colors.primary,
                                xAxisColor = MaterialTheme.colors.primary,
                                yAxisColor = MaterialTheme.colors.primary
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
    } else {
        CircularProgressBar(
            isDisplayed = true, Modifier.size(60.dp)
        )
    }
}