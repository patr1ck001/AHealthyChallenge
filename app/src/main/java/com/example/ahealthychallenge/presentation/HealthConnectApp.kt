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
package com.example.ahealthychallenge.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.data.HealthConnectAvailability
import com.example.ahealthychallenge.data.HealthConnectManager
import com.example.ahealthychallenge.presentation.largeScreenLayout.LargeScreen
import com.example.ahealthychallenge.presentation.navigation.Drawer
import com.example.ahealthychallenge.presentation.navigation.HealthConnectNavigation
import com.example.ahealthychallenge.presentation.navigation.Screen
import com.example.ahealthychallenge.presentation.theme.HealthConnectTheme
import com.example.ahealthychallenge.presentation.utils.ContentType
import com.example.ahealthychallenge.presentation.utils.FirebaseUtils
import com.example.ahealthychallenge.presentation.utils.NavigationType
import com.google.accompanist.adaptive.calculateDisplayFeatures
import kotlinx.coroutines.launch

const val TAG = "Health Connect sample"

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HealthConnectApp(
    healthConnectManager: HealthConnectManager,
    activity: Activity,
    hasFriendRequest: Boolean
) {
    val windowSize = calculateWindowSizeClass(activity).widthSizeClass
    val navigationType: NavigationType
    var drawerShape = NavShape(0.dp, 1f)

    val contentType: ContentType

    Log.d("navigo", "this is the windows size: $windowSize")
    when (windowSize) {
        WindowWidthSizeClass.Compact -> {
            navigationType = NavigationType.BOTTOM_NAVIGATION
            contentType = ContentType.LIST_ONLY
        }

        WindowWidthSizeClass.Medium -> {
            navigationType = NavigationType.NAVIGATION_RAIL
            contentType = ContentType.LIST_ONLY
            drawerShape = NavShape(0.dp, 0.5f)
        }

        WindowWidthSizeClass.Expanded -> {
            navigationType = NavigationType.PERMANENT_NAVIGATION_DRAWER
            contentType = ContentType.LIST_AND_DETAIL
        }

        else -> {
            navigationType = NavigationType.BOTTOM_NAVIGATION
            contentType = ContentType.LIST_ONLY
        }
    }

    HealthConnectTheme {
        val scaffoldState = rememberScaffoldState()
        val navController = rememberNavController()
        val scope = rememberCoroutineScope()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        var icon = R.drawable.group_svgrepo_com

        val availability by healthConnectManager.availability
        var mDisplayMenu by remember { mutableStateOf(false) }
        val context = LocalContext.current

        if(hasFriendRequest){
            icon = R.drawable.group_outline_badged_svgrepo_com
        }

        if (navigationType != NavigationType.PERMANENT_NAVIGATION_DRAWER) {
            Scaffold(
                scaffoldState = scaffoldState,
                modifier = Modifier.statusBarsPadding(),
                topBar = {
                    TopAppBar(
                        title = {
                            val titleId = when (currentRoute) {
                                Screen.ExerciseSessions.route -> Screen.ExerciseSessions.titleId
                                Screen.SleepSessions.route -> Screen.SleepSessions.titleId
                                Screen.InputReadings.route -> Screen.InputReadings.titleId
                                Screen.DifferentialChanges.route -> Screen.DifferentialChanges.titleId
                                else -> R.string.app_name
                            }
                            Text(stringResource(titleId))
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    if (availability == HealthConnectAvailability.INSTALLED) {
                                        scope.launch {
                                            scaffoldState.drawerState.open()
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Menu,
                                    stringResource(id = R.string.menu)
                                )
                            }
                        },

                        actions = {
                            IconButton(onClick = {
                                val intent = Intent(context, FriendsActivity::class.java)
                                context.startActivity(intent)
                            }
                            ) {
                                Icon(
                                    painter = painterResource(id = icon),
                                    stringResource(id = R.string.add_friend),
                                    modifier = Modifier.height(45.dp),
                                    tint = Color.White
                                )
                            }
                        }
                    )
                },
                drawerContent = {
                    if (availability == HealthConnectAvailability.INSTALLED) {
                        Drawer(
                            scope = scope,
                            scaffoldState = scaffoldState,
                            navController = navController
                        )
                    }
                },
                snackbarHost = {
                    SnackbarHost(it) { data -> Snackbar(snackbarData = data) }
                }
            ) {//TODO: add the padding for the backdrop scaffold
                HealthConnectNavigation(
                    navigationType = navigationType,
                    drawerScope = scope,
                    healthConnectManager = healthConnectManager,
                    navController = navController,
                    scaffoldState = scaffoldState
                )
            }
        } else {
            LargeScreen(
                healthConnectManager = healthConnectManager,
                scaffoldState = scaffoldState,
                scope = scope,
                navController = navController,
                currentRoute = currentRoute,
                displayFeatures =  calculateDisplayFeatures(activity)
            )
        }
    }
}

class NavShape(
    private val widthOffset: Dp,
    private val scale: Float
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            Rect(
                Offset.Zero,
                Offset(
                    size.width * scale + with(density) { widthOffset.toPx() },
                    size.height
                )
            )
        )
    }
}
