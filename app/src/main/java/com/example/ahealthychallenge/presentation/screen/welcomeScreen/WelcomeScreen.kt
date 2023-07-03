/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.ahealthychallenge.presentation.screen.welcomeScreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.data.HealthConnectAvailability
import com.example.ahealthychallenge.presentation.bottomBar.BottomNavItem
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ahealthychallenge.data.HealthConnectManager
import com.example.ahealthychallenge.presentation.SearchUserActivity
import com.example.ahealthychallenge.presentation.navigation.Screen
import com.example.ahealthychallenge.presentation.screen.welcomeScreen.friendsScreen.FriendsScreen
import com.example.ahealthychallenge.presentation.screen.welcomeScreen.homeScreen.HomeScreen
import com.example.ahealthychallenge.presentation.screen.welcomeScreen.homeScreen.HomeScreenViewModel
import com.example.ahealthychallenge.presentation.screen.welcomeScreen.homeScreen.HomeScreenViewModelFactory
import kotlinx.coroutines.CoroutineScope


/**
 * Welcome screen shown when the app is first launched.
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WelcomeScreen(
    healthConnectAvailability: HealthConnectAvailability,
    healthConnectManager: HealthConnectManager,
    drawerNavController: NavController,
    drawerScope: CoroutineScope,
    scaffoldState: ScaffoldState,
    onResumeAvailabilityCheck: () -> Unit,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val currentOnAvailabilityCheck by rememberUpdatedState(onResumeAvailabilityCheck)
    val context = LocalContext.current

    // Add a listener to re-check whether Health Connect has been installed each time the Welcome
    // screen is resumed: This ensures that if the user has been redirected to the Play store and
    // followed the onboarding flow, then when the app is resumed, instead of showing the message
    // to ask the user to install Health Connect, the app recognises that Health Connect is now
    // available and shows the appropriate welcome.
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                currentOnAvailabilityCheck()
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = listOf(
                    BottomNavItem(
                        name = "Home",
                        route = "home",
                        icon = ImageVector.vectorResource(id = R.drawable.ic_home)
                    ),
                    BottomNavItem(
                        name = "leaderBoard",
                        route = "leaderBoard",
                        icon = ImageVector.vectorResource(id = R.drawable.ic_ranking),
                        badgeCount = 214
                    ),
                    BottomNavItem(
                        name = "friends",
                        route = "friends",
                        icon = ImageVector.vectorResource(id = R.drawable.ic_friends),
                        badgeCount = 23
                    ),
                ),
                navController = navController,
                onItemClick = {
                    navController.navigate(it.route)
                    /*if (it.route == "friends") {
                        val intent = Intent(context, SearchUserActivity::class.java)
                        context.startActivity(intent)
                    } else {
                        navController.navigate(it.route)

                    }*/
                }
            )
        }
    ) {
        Navigation(
            navController = navController,
            healthConnectAvailability = healthConnectAvailability,
            healthConnectManager = healthConnectManager,
            drawerNavController = drawerNavController,
            drawerScope = drawerScope,
            scaffoldState = scaffoldState
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Navigation(
    navController: NavHostController,
    healthConnectAvailability: HealthConnectAvailability,
    scaffoldState: ScaffoldState,
    drawerNavController: NavController,
    drawerScope: CoroutineScope,
    healthConnectManager: HealthConnectManager
) {
    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            val viewModel: HomeScreenViewModel = viewModel(
                factory = HomeScreenViewModelFactory(
                    healthConnectManager = healthConnectManager
                )
            )
            val curveLineData by viewModel.curveLineData
            val refreshing by viewModel.refreshing
            val onRefresh = { viewModel.refreshing() }
            val pullRefreshState = rememberPullRefreshState(refreshing, { viewModel.refreshing() })
            HomeScreen(
                curveLineData = curveLineData,
                pullRefreshState = pullRefreshState,
                isRefreshing = refreshing,
                onRefresh = onRefresh,
                drawerNavController = drawerNavController,
                drawerScope = drawerScope,
                scaffoldState = scaffoldState
            )
        }
        composable("friends") {

            val context = LocalContext.current
            val onAddFriend = {
                val intent = Intent(context, SearchUserActivity::class.java)
                context.startActivity(intent)
            }
            FriendsScreen(
                onDetailsClick = { uid ->
                    navController.navigate("detailsFriends/$uid")
                },
                onAddFriend = onAddFriend
            )
        }
        composable("leaderBoard") {
            LeaderBoardScreen()
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(
        modifier = modifier,
        backgroundColor = MaterialTheme.colors.onPrimary,
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                selected = selected,
                onClick = { onItemClick(item) },
                selectedContentColor = MaterialTheme.colors.secondary,
                unselectedContentColor = MaterialTheme.colors.secondary,
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (item.badgeCount > 0) {
                            BadgedBox(
                                badge = { Badge { Text(text = item.badgeCount.toString()) } }
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.name,
                                    modifier = Modifier
                                        .height(30.dp)
                                        .width(30.dp)
                                )
                            }
                        } else {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.name,
                                modifier = Modifier
                                    .height(30.dp)
                                    .width(30.dp)
                            )
                        }
                        if (selected) {
                            Text(
                                text = item.name,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            )
        }
    }
}

/*@Composable
fun HomeScreen(
    healthConnectAvailability: HealthConnectAvailability,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(0.5f),
            painter = painterResource(id = R.drawable.ic_health_connect_logo),
            contentDescription = stringResource(id = R.string.health_connect_logo)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(id = R.string.welcome_message),
            color = MaterialTheme.colors.onBackground
        )
        Spacer(modifier = Modifier.height(32.dp))
        when (healthConnectAvailability) {
            HealthConnectAvailability.INSTALLED -> InstalledMessage()
            HealthConnectAvailability.NOT_INSTALLED -> NotInstalledMessage()
            HealthConnectAvailability.NOT_SUPPORTED -> NotSupportedMessage()
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}*/


@Composable
fun LeaderBoardScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "leaderBoard screen")
    }
}
