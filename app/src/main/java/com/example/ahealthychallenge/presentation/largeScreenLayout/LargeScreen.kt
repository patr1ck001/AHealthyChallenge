package com.example.ahealthychallenge.presentation.largeScreenLayout

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Row
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.window.layout.DisplayFeature
import com.example.ahealthychallenge.data.HealthConnectManager
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
import com.example.ahealthychallenge.presentation.screen.screens.PointStatScreen
import com.example.ahealthychallenge.presentation.screen.screens.PointStatScreenViewModel
import com.example.ahealthychallenge.presentation.screen.screens.PointStatScreenViewModelFactory
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

@Composable
fun LargeScreen(
    healthConnectManager: HealthConnectManager,
    scaffoldState: ScaffoldState,
    navController: NavHostController,
    scope: CoroutineScope,
    displayFeatures: List<DisplayFeature>
) {
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
                     Nav(
                         scaffoldState = scaffoldState,
                         navController = navController,
                         healthConnectManager = healthConnectManager,
                         scope = scope
                     )
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
            val curveLineData by viewModel.curveLineData
            val walkingLineData by viewModel.walkingLineData
            val runningLineData by viewModel.runningLineData
            val bikingLineData by viewModel.bikingLineData
            val workoutLineData by viewModel.workoutLineData
            PointStatScreen(
                navigationType = NavigationType.PERMANENT_NAVIGATION_DRAWER,
                pieData = pieData,
                curveLineData = curveLineData,
                walkingLineData = walkingLineData,
                runningLineData = runningLineData,
                bikingLineData = bikingLineData,
                workoutLineData = workoutLineData
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