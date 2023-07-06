package com.example.ahealthychallenge.presentation.screen.points

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PointStatScreen(
    navigationType: NavigationType,
    pieData: List<PieData>,
    curveLineData: List<LineData>,
    walkingLineData: List<LineData>,
    runningLineData: List<LineData>,
    bikingLineData: List<LineData>,
    workoutLineData: List<LineData>,
    pullRefreshState: PullRefreshState,
    isRefreshing: Boolean

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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            if (pieData.isNotEmpty()) {
                item {
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
                item {
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

            if (curveLineData.isNotEmpty()) {
                item {
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

                item {
                    Text(
                        text = "Points this month",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            /*walking*/
            if (walkingLineData.isNotEmpty()) {
                item {
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

                item {
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

            /*running*/
            if (runningLineData.isNotEmpty()) {
                item {
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

                item {
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

            /*biking*/
            if (bikingLineData.isNotEmpty()) {

                item {
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

                item {
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

            /*working out*/
            if (workoutLineData.isNotEmpty()) {
                item {
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

                item {
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

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }

}