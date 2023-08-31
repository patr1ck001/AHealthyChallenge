package com.example.ahealthychallenge.presentation.largeScreenLayout

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.window.layout.DisplayFeature
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.data.HealthConnectAvailability
import com.example.ahealthychallenge.data.HealthConnectManager
import com.example.ahealthychallenge.presentation.FriendsActivity
import com.example.ahealthychallenge.presentation.navigation.Drawer
import com.example.ahealthychallenge.presentation.navigation.HealthConnectNavigation
import com.example.ahealthychallenge.presentation.navigation.Screen
import com.example.ahealthychallenge.presentation.navigation.UID_NAV_ARGUMENT
import com.example.ahealthychallenge.presentation.screen.SettingsScreen
import com.example.ahealthychallenge.presentation.screen.changes.DifferentialChangesScreen
import com.example.ahealthychallenge.presentation.screen.changes.DifferentialChangesViewModel
import com.example.ahealthychallenge.presentation.screen.changes.DifferentialChangesViewModelFactory
import com.example.ahealthychallenge.presentation.screen.exercisesession.ExerciseSessionScreen
import com.example.ahealthychallenge.presentation.screen.exercisesession.ExerciseSessionViewModel
import com.example.ahealthychallenge.presentation.screen.exercisesession.ExerciseSessionViewModelFactory
import com.example.ahealthychallenge.presentation.screen.exercisesessiondetail.ExerciseSessionDetailScreen
import com.example.ahealthychallenge.presentation.screen.exercisesessiondetail.ExerciseSessionDetailViewModel
import com.example.ahealthychallenge.presentation.screen.exercisesessiondetail.ExerciseSessionDetailViewModelFactory
import com.example.ahealthychallenge.presentation.screen.inputreadings.InputReadingsScreen
import com.example.ahealthychallenge.presentation.screen.inputreadings.InputReadingsViewModel
import com.example.ahealthychallenge.presentation.screen.inputreadings.InputReadingsViewModelFactory
import com.example.ahealthychallenge.presentation.screen.points.PointStatScreen
import com.example.ahealthychallenge.presentation.screen.points.PointStatScreenViewModel
import com.example.ahealthychallenge.presentation.screen.points.PointStatScreenViewModelFactory
import com.example.ahealthychallenge.presentation.screen.privacypolicy.PrivacyPolicyScreen
import com.example.ahealthychallenge.presentation.screen.sleepsession.SleepSessionScreen
import com.example.ahealthychallenge.presentation.screen.sleepsession.SleepSessionViewModel
import com.example.ahealthychallenge.presentation.screen.sleepsession.SleepSessionViewModelFactory
import com.example.ahealthychallenge.presentation.screen.welcomeScreen.WelcomeScreen
import com.example.ahealthychallenge.presentation.utils.NavigationType
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LargeScreen(
    healthConnectManager: HealthConnectManager,
    scaffoldState: ScaffoldState,
    navController: NavHostController,
    currentRoute: String?,
    scope: CoroutineScope,
    displayFeatures: List<DisplayFeature>
) {
    val context = LocalContext.current
    val icon = R.drawable.group_svgrepo_com

    Row() {
        val paneStrategy = HorizontalTwoPaneStrategy(
            splitFraction = 1f / 4f,
        )
        Text(modifier = Modifier.weight(1f), text = "compose")
        TwoPane(
            first = {
                LargeScreenDrawer(
                    scope = scope,
                    navController = navController
                )
            },
            second = {
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
                    snackbarHost = {
                        SnackbarHost(it) { data -> Snackbar(snackbarData = data) }
                    }
                ) {
                     Nav(
                         scaffoldState = scaffoldState,
                         navController = navController,
                         healthConnectManager = healthConnectManager,
                         scope = scope
                     )
                }
            },
            strategy = paneStrategy,
            displayFeatures = displayFeatures
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Nav(
    scaffoldState: ScaffoldState,
    navController: NavHostController,
    healthConnectManager: HealthConnectManager,
    scope: CoroutineScope
){
    NavHost(navController = navController, startDestination = Screen.WelcomeScreen.route) {
        val availability by healthConnectManager.availability
        composable(Screen.WelcomeScreen.route) {
            WelcomeScreen(
                navigationType = NavigationType.PERMANENT_NAVIGATION_DRAWER,
                healthConnectAvailability = availability,
                healthConnectManager = healthConnectManager,
                drawerNavController = navController,
                drawerScope = scope,
                scaffoldState = scaffoldState,
                onResumeAvailabilityCheck = {
                    healthConnectManager.checkAvailability()
                }
            )
        }
        composable(
            route = Screen.PrivacyPolicy.route,
            deepLinks = listOf(
                navDeepLink {
                    action = "androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE"
                }
            )
        ) {
            PrivacyPolicyScreen()
        }
        composable(Screen.SettingsScreen.route){
            SettingsScreen { scope.launch { healthConnectManager.revokeAllPermissions() } }
        }
        composable(Screen.ExerciseSessions.route) {
            val viewModel: ExerciseSessionViewModel = viewModel(
                factory = ExerciseSessionViewModelFactory(
                    healthConnectManager = healthConnectManager
                )
            )
            val permissionsGranted by viewModel.permissionsGranted
            val sessionsList by viewModel.sessionsList
            val dailySessionsList by viewModel.dailySessionsList
            val allSessions by viewModel.allSessions
            val stepsList by viewModel.stepsList
            val permissions = viewModel.permissions
            val loading by viewModel.loading
            val onPermissionsResult = {viewModel.initialLoad()}
            val permissionsLauncher =
                rememberLauncherForActivityResult(viewModel.permissionsLauncher) {
                    onPermissionsResult()}

            val refreshing by viewModel.refreshing
            val pullRefreshState = rememberPullRefreshState(refreshing, { viewModel.initialLoad() })

            ExerciseSessionScreen(
                navigationType = NavigationType.PERMANENT_NAVIGATION_DRAWER,
                permissionsGranted = permissionsGranted,
                permissions = permissions,
                sessionsList = sessionsList,
                dailySessionsList = dailySessionsList,
                allSessions = allSessions,
                loading = loading,
                stepsList = stepsList,
                uiState = viewModel.uiState,
                onInsertClick = {
                    viewModel.insertExerciseSession()
                },
                onDetailsClick = { uid ->
                    navController.navigate(Screen.ExerciseSessionDetail.route + "/" + uid)
                },
                onDeleteClick = { uid ->
                    viewModel.deleteExerciseSession(uid)
                },
                onPermissionsResult = {
                    viewModel.initialLoad()
                },
                onPermissionsLaunch = { values ->
                    permissionsLauncher.launch(values)},
                pullRefreshState = pullRefreshState,
                refreshing = refreshing
            )

        }

        composable(Screen.PointScreen.route) {
            val viewModel: PointStatScreenViewModel = viewModel(
                factory = PointStatScreenViewModelFactory(
                    healthConnectManager = healthConnectManager
                )
            )

            val pieData by viewModel.pieData
            val pieDataMap by viewModel.pieDataMap
            val curveLineData by viewModel.curveLineData
            val walkingLineData by viewModel.walkingLineData
            val runningLineData by viewModel.runningLineData
            val bikingLineData by viewModel.bikingLineData
            val workoutLineData by viewModel.workoutLineData
            val pointStatScreenLoading by viewModel.pointStatScreenLoading

            PointStatScreen(
                navigationType = NavigationType.PERMANENT_NAVIGATION_DRAWER,
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

        composable(Screen.ExerciseSessionDetail.route + "/{$UID_NAV_ARGUMENT}") {
            val uid = it.arguments?.getString(UID_NAV_ARGUMENT)!!
            val viewModel: ExerciseSessionDetailViewModel = viewModel(
                factory = ExerciseSessionDetailViewModelFactory(
                    uid = uid,
                    healthConnectManager = healthConnectManager
                )
            )
            val permissionsGranted by viewModel.permissionsGranted
            val sessionMetrics by viewModel.sessionMetrics
            val permissions = viewModel.permissions
            val onPermissionsResult = {viewModel.initialLoad()}
            val permissionsLauncher =
                rememberLauncherForActivityResult(viewModel.permissionsLauncher) {
                    onPermissionsResult()}
            ExerciseSessionDetailScreen(
                permissions = permissions,
                permissionsGranted = permissionsGranted,
                sessionMetrics = sessionMetrics,
                uiState = viewModel.uiState,
                onPermissionsResult = {
                    viewModel.initialLoad()
                },
                onPermissionsLaunch = { values ->
                    permissionsLauncher.launch(values)}
            )
        }
        composable(Screen.SleepSessions.route) {
            val viewModel: SleepSessionViewModel = viewModel(
                factory = SleepSessionViewModelFactory(
                    healthConnectManager = healthConnectManager
                )
            )
            val permissionsGranted by viewModel.permissionsGranted
            val sessionsList by viewModel.sessionsList
            val permissions = viewModel.permissions
            val onPermissionsResult = {viewModel.initialLoad()}
            val permissionsLauncher =
                rememberLauncherForActivityResult(viewModel.permissionsLauncher) {
                    onPermissionsResult()}
            SleepSessionScreen(
                permissionsGranted = permissionsGranted,
                permissions = permissions,
                sessionsList = sessionsList,
                uiState = viewModel.uiState,
                onInsertClick = {
                    viewModel.generateSleepData()
                },
                onPermissionsResult = {
                    viewModel.initialLoad()
                },
                onPermissionsLaunch = { values ->
                    permissionsLauncher.launch(values)}
            )
        }
        composable(Screen.InputReadings.route) {
            val viewModel: InputReadingsViewModel = viewModel(
                factory = InputReadingsViewModelFactory(
                    healthConnectManager = healthConnectManager
                )
            )
            val permissionsGranted by viewModel.permissionsGranted
            val readingsList by viewModel.readingsList
            val permissions = viewModel.permissions
            val weeklyAvg by viewModel.weeklyAvg
            val onPermissionsResult = {viewModel.initialLoad()}
            val permissionsLauncher =
                rememberLauncherForActivityResult(viewModel.permissionsLauncher) {
                    onPermissionsResult()}
            InputReadingsScreen(
                permissionsGranted = permissionsGranted,
                permissions = permissions,

                uiState = viewModel.uiState,
                onInsertClick = { weightInput ->
                    viewModel.inputReadings(weightInput)
                },
                weeklyAvg = weeklyAvg,
                onDeleteClick = { uid ->
                    viewModel.deleteWeightInput(uid)
                },
                readingsList = readingsList,
                onPermissionsResult = {
                    viewModel.initialLoad()
                },
                onPermissionsLaunch = { values ->
                    permissionsLauncher.launch(values)}
            )
        }
        composable(Screen.DifferentialChanges.route) {
            val viewModel: DifferentialChangesViewModel = viewModel(
                factory = DifferentialChangesViewModelFactory(
                    healthConnectManager = healthConnectManager
                )
            )
            val changesToken by viewModel.changesToken
            val permissionsGranted by viewModel.permissionsGranted
            val permissions = viewModel.permissions
            val onPermissionsResult = {viewModel.initialLoad()}
            val permissionsLauncher =
                rememberLauncherForActivityResult(viewModel.permissionsLauncher) {
                    onPermissionsResult()}
            DifferentialChangesScreen(
                permissionsGranted = permissionsGranted,
                permissions = permissions,
                changesEnabled = changesToken != null,
                onChangesEnable = { enabled ->
                    viewModel.enableOrDisableChanges(enabled)
                },
                changes = viewModel.changes,
                changesToken = changesToken,
                onGetChanges = {
                    viewModel.getChanges()
                },
                uiState = viewModel.uiState,
                onPermissionsResult = {
                    viewModel.initialLoad()
                }
            ) { values ->
                permissionsLauncher.launch(values)
            }
        }

    }
}