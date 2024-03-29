package com.example.ahealthychallenge.presentation.screen.welcomeScreen.homeScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.presentation.component.CircularProgressBar
import com.example.ahealthychallenge.presentation.navigation.Screen
import com.example.ahealthychallenge.presentation.utils.NavigationType
import com.himanshoe.charty.common.axis.AxisConfig
import com.himanshoe.charty.line.LineChart
import com.himanshoe.charty.line.model.LineData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    homeScreenLoading: Boolean,
    navigationType: NavigationType,
    lineData: List<LineData>,
    drawerNavController: NavController,
    drawerScope: CoroutineScope,
    scaffoldState: ScaffoldState,
    positionInLeaderboard: Int,
    pointTHisMonth: Int
) {
    if (navigationType == NavigationType.BOTTOM_NAVIGATION) {
        CompactHomeScreen(
            homeScreenLoading = homeScreenLoading,
            lineData = lineData,
            drawerNavController = drawerNavController,
            drawerScope = drawerScope,
            scaffoldState = scaffoldState,
            positionInLeaderboard = positionInLeaderboard,
            pointTHisMonth = pointTHisMonth
        )
    } else {
        ExpendedAndMediumHomeScreen(
            homeScreenLoading = homeScreenLoading,
            lineData = lineData,
            drawerNavController = drawerNavController,
            drawerScope = drawerScope,
            scaffoldState = scaffoldState,
            positionInLeaderboard = positionInLeaderboard,
            pointTHisMonth = pointTHisMonth
        )
    }
}

@Composable
fun CompactHomeScreen(
    homeScreenLoading: Boolean,
    lineData: List<LineData>,
    drawerNavController: NavController,
    drawerScope: CoroutineScope,
    scaffoldState: ScaffoldState,
    positionInLeaderboard: Int,
    pointTHisMonth: Int
) {

    val context = LocalContext.current
    if (!homeScreenLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (lineData.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(7.dp)
                            .testTag("clickableCard")
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
                                text = stringResource(R.string.points_this_month)
                            )
                            Box(modifier = Modifier.height(30.dp))
                            LineChart(
                                modifier = Modifier
                                    .fillMaxSize(0.85f)
                                    .align(Alignment.CenterHorizontally)
                                    .padding(bottom = 50.dp)
                                    .weight(1f),
                                color = MaterialTheme.colors.primary,
                                lineData = lineData,
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
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {

                    Text(
                        text = "Workout to add some statistics!",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = MaterialTheme.colors.primary
                    )
                    LineChart(
                        modifier = Modifier
                            .fillMaxSize(0.85f)
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 50.dp)
                            .weight(1f),
                        color = MaterialTheme.colors.primary,
                        lineData = listOf(LineData(0, 0F)),
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
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 7.dp)
                        .weight(1f),
                    elevation = 10.dp,
                    contentColor = MaterialTheme.colors.onPrimary,
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Position",
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "$positionInLeaderboard",
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                }
                Spacer(modifier = Modifier.width(5.dp))
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 7.dp)
                        .weight(1f),
                    elevation = 10.dp,
                    contentColor = MaterialTheme.colors.onPrimary,
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Points this month",
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "$pointTHisMonth",
                            fontSize = 30.sp,
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

@Composable
fun ExpendedAndMediumHomeScreen(
    homeScreenLoading: Boolean,
    lineData: List<LineData>,
    drawerNavController: NavController,
    drawerScope: CoroutineScope,
    scaffoldState: ScaffoldState,
    positionInLeaderboard: Int,
    pointTHisMonth: Int
) {
    val context = LocalContext.current

    if (!homeScreenLoading) {
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (lineData.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(7.dp)
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
                        shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)

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
                            Box(modifier = Modifier.height(30.dp))
                            LineChart(
                                modifier = Modifier
                                    .fillMaxSize(0.85f)
                                    .align(Alignment.CenterHorizontally)
                                    .padding(bottom = 50.dp)
                                    .weight(1f),
                                color = MaterialTheme.colors.primary,
                                lineData = lineData,
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
                        }
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(7.dp),
                        elevation = 10.dp,
                        shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)

                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Workout to add some statistics!",
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                color = MaterialTheme.colors.primary
                            )
                            LineChart(
                                modifier = Modifier
                                    .fillMaxSize(0.85f)
                                    .align(Alignment.CenterHorizontally)
                                    .padding(bottom = 50.dp)
                                    .weight(1f),
                                color = MaterialTheme.colors.primary,
                                lineData = listOf(LineData(0, 0F)),
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
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(0.2f)
                    .fillMaxHeight()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 7.dp)
                        .weight(1f),
                    elevation = 10.dp,
                    contentColor = MaterialTheme.colors.onPrimary,
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Position",
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text ="$positionInLeaderboard",
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                }
                Spacer(modifier = Modifier.height(5.dp))
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 7.dp)
                        .weight(1f),
                    elevation = 10.dp,
                    contentColor = MaterialTheme.colors.onPrimary,
                    backgroundColor = MaterialTheme.colors.primary
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Points this month",
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "$pointTHisMonth",
                            fontSize = 30.sp,
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

@Composable
fun ExpendedHomeScreen(
    lineData: List<LineData>,
    drawerNavController: NavController,
    drawerScope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (lineData.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(7.dp)
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
                        Box(modifier = Modifier.height(30.dp))
                        LineChart(
                            modifier = Modifier
                                .fillMaxSize(0.85f)
                                .align(Alignment.CenterHorizontally)
                                .padding(bottom = 50.dp)
                                .weight(1f),
                            color = MaterialTheme.colors.primary,
                            lineData = lineData,
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
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(0.2f)
                    .fillMaxHeight()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 7.dp)
                        .weight(1f)
                        .clickable {
                            drawerNavController.navigate(Screen.ExerciseSessions.route) {
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
                    contentColor = MaterialTheme.colors.onPrimary,
                    backgroundColor = MaterialTheme.colors.primary
                ) {
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
                Spacer(modifier = Modifier.height(5.dp))
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 7.dp)
                        .weight(1f),
                    elevation = 10.dp,
                    contentColor = MaterialTheme.colors.onPrimary,
                    backgroundColor = MaterialTheme.colors.primary
                ) {
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