package com.example.ahealthychallenge.presentation.screen.points

import android.app.slice.Slice
import android.content.res.Resources.Theme
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ahealthychallenge.presentation.theme.HealthConnectBlue
import com.example.ahealthychallenge.presentation.theme.HealthConnectGreen
import com.himanshoe.charty.circle.CircleChart
import com.himanshoe.charty.circle.model.CircleData
import com.himanshoe.charty.line.CurveLineChart
import com.himanshoe.charty.line.LineChart
import com.himanshoe.charty.line.config.CurveLineConfig
import com.himanshoe.charty.line.model.LineData
import com.himanshoe.charty.pie.PieChart
import com.himanshoe.charty.pie.config.PieConfig
import com.himanshoe.charty.pie.config.PieData

@Composable
fun PointStatScreen(
) {
    val pieData = listOf(
        PieData(3F),
        PieData(1F),
        PieData(1F),
    )
    val lineChartData = listOf(
        LineData(10F, 35F),
        LineData(20F, 25F),
        LineData(10F, 50F),
        LineData(80F, 10F),
        LineData(10F, 15F),
        LineData(50F, 100F),
        LineData(20F, 25F),
    )

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

}