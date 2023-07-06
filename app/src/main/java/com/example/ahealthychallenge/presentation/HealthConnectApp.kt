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
import android.widget.Toast
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.data.HealthConnectAvailability
import com.example.ahealthychallenge.data.HealthConnectManager
import com.example.ahealthychallenge.presentation.component.getIconId
import com.example.ahealthychallenge.presentation.navigation.Drawer
import com.example.ahealthychallenge.presentation.navigation.HealthConnectNavigation
import com.example.ahealthychallenge.presentation.navigation.Screen
import com.example.ahealthychallenge.presentation.theme.HealthConnectTheme
import com.example.ahealthychallenge.presentation.utils.ContentType
import com.example.ahealthychallenge.presentation.utils.FirebaseUtils
import com.example.ahealthychallenge.presentation.utils.NavigationType
import com.google.android.gms.common.internal.StringResourceValueReader
import kotlinx.coroutines.launch

const val TAG = "Health Connect sample"

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HealthConnectApp(
    healthConnectManager: HealthConnectManager,
    activity: Activity
) {
    val windowSize = calculateWindowSizeClass(activity).widthSizeClass
    val navigationType: NavigationType
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

        val availability by healthConnectManager.availability
        var mDisplayMenu by remember { mutableStateOf(false) }
        val context = LocalContext.current
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
                        IconButton(onClick = { mDisplayMenu = !mDisplayMenu }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_menu_vertical),
                                stringResource(id = R.string.menu),
                                modifier = Modifier.height(30.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = mDisplayMenu,
                            onDismissRequest = { mDisplayMenu = false }
                        ) {
                            // Creating dropdown menu item, on click
                            // would create a Toast message
                            DropdownMenuItem(onClick = {
                                FirebaseUtils.firebaseAuth.signOut()
                                val intent = Intent(context, SignInActivity::class.java)
                                context.startActivity(intent)
                            }) {
                                Text(text = stringResource(id = R.string.sign_out))
                            }
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
    }
}
