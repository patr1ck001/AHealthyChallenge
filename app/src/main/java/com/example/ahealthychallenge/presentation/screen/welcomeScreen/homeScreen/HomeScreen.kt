package com.example.ahealthychallenge.presentation.screen.welcomeScreen.homeScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.presentation.navigation.Screen
import com.example.ahealthychallenge.presentation.theme.HealthConnectBlue
import com.example.ahealthychallenge.presentation.theme.HealthConnectGreen
import com.himanshoe.charty.common.axis.AxisConfig
import com.himanshoe.charty.line.LineChart
import com.himanshoe.charty.line.model.LineData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    curveLineData: List<LineData>,
    pullRefreshState: PullRefreshState,
    drawerNavController: NavController,
    drawerScope: CoroutineScope,
    scaffoldState: ScaffoldState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (curveLineData.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clickable { }
                            .height(500.dp)
                            .clickable {
                                drawerNavController.navigate(Screen.PointScreen.route) {
                                    drawerNavController.graph.startDestinationRoute?.let { route ->
                                        popUpTo(route) {
                                            saveState = true
                                        }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                drawerScope.launch {
                                    scaffoldState.drawerState.close()
                                }
                            },
                        elevation = 10.dp,

                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally),
                                text = "Point This month"
                            )

                            LineChart(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp),
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
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    ) {
                        Card(modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 10.dp)
                            .weight(1f),
                            elevation = 10.dp,
                            contentColor = MaterialTheme.colors.onPrimary,
                            backgroundColor = HealthConnectBlue) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "1 st",
                                    textAlign = TextAlign.Center
                                )
                            }

                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Card(modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 10.dp)
                            .weight(1f),
                            elevation = 10.dp,
                            contentColor = MaterialTheme.colors.onPrimary,
                            backgroundColor = HealthConnectBlue) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    text = "10 points",
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

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