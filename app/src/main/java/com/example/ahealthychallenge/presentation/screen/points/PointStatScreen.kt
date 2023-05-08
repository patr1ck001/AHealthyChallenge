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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ahealthychallenge.presentation.theme.HealthConnectBlue
import com.example.ahealthychallenge.presentation.theme.HealthConnectGreen
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
    pieData: List<PieData>,
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

    Box(
        modifier = Modifier.fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
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
                    text = "Pie chart",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    textAlign = TextAlign.Center
                )
            }

            item {
                CurveLineChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 300.dp),
                    chartColors = listOf(HealthConnectBlue, HealthConnectGreen),
                    lineColors = listOf(HealthConnectBlue, HealthConnectGreen),
                    lineData = lineChartData,
                    curveLineConfig = CurveLineConfig(false)
                )
            }

            item {
                Text(
                    text = "curve chart",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    textAlign = TextAlign.Center
                )
            }

            item {
                LineChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    color = HealthConnectBlue,
                    lineData = lineChartData
                )
            }

            item {
                Text(
                    text = "Line chart",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }

}