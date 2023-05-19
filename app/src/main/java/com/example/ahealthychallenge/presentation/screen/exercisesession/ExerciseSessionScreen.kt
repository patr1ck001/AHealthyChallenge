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
package com.example.ahealthychallenge.presentation.screen.exercisesession

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.TypedArrayUtils.getText
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.data.*
import com.example.ahealthychallenge.presentation.component.CircularProgressBar
import com.example.ahealthychallenge.presentation.component.ExerciseSessionRow
import com.example.ahealthychallenge.presentation.component.StepSessionRow
import com.example.ahealthychallenge.presentation.theme.HealthConnectTheme
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import java.time.Duration
import java.time.ZonedDateTime
import java.util.UUID

/**
 * Shows a list of [ExerciseSessionRecord]s from today.
 */
const val TAG = "ExerciseSessionScreen"

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExerciseSessionScreen(
    permissions: Set<String>,
    permissionsGranted: Boolean,
    sessionsList: List<ExerciseSession>,
    dailySessionsList: DailySessionsList,
    loading: Boolean,
    allSessions: List<DailySessionsList>,
    stepsList: List<StepSession>,
    uiState: ExerciseSessionViewModel.UiState,
    onInsertClick: () -> Unit = {},
    onDetailsClick: (String) -> Unit = {},
    onDeleteClick: (String) -> Unit = {},
    onError: (Throwable?) -> Unit = {},
    onPermissionsResult: () -> Unit = {},
    onPermissionsLaunch: (Set<String>) -> Unit = {},
    pullRefreshState: PullRefreshState,
    refreshing: Boolean
) {

    // Remember the last error ID, such that it is possible to avoid re-launching the error
    // notification for the same error when the screen is recomposed, or configuration changes etc.
    val errorId = rememberSaveable { mutableStateOf(UUID.randomUUID()) }
    LaunchedEffect(uiState) {
        // If the initial data load has not taken place, attempt to load the data.
        if (uiState is ExerciseSessionViewModel.UiState.Uninitialized) {
            onPermissionsResult()
        }

        // The [ExerciseSessionViewModel.UiState] provides details of whether the last action was a
        // success or resulted in an error. Where an error occurred, for example in reading and
        // writing to Health Connect, the user is notified, and where the error is one that can be
        // recovered from, an attempt to do so is made.
        if (uiState is ExerciseSessionViewModel.UiState.Error && errorId.value != uiState.uuid) {
            onError(uiState.exception)
            errorId.value = uiState.uuid
        }
    }

    if (uiState != ExerciseSessionViewModel.UiState.Uninitialized) {

        Box(
            modifier = Modifier.fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!permissionsGranted) {
                    item {
                        Button(onClick = {
                            onPermissionsLaunch(permissions)
                        }) {
                            Text(text = stringResource(R.string.permissions_button_label))
                        }
                    }
                } else if (!loading && allSessions.isNotEmpty()) { // } else if (dailySessionsList.exerciseSessions.isNotEmpty()) { // } else if (sessionsList.isNotEmpty()) {
                    item {
                        Button(modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .padding(4.dp),
                            onClick = {
                                onInsertClick()
                            }) {
                            Text(stringResource(id = R.string.insert_exercise_session))
                        }
                    }
                    /*items(sessionsList) { session ->
                        Log.d(TAG, "${session.sessionData.totalActiveTime?.formatTime()}")
                        val appInfo = session.sourceAppInfo
                        ExerciseSessionRow(
                            exerciseType = session.exerciseType,
                            start = session.startTime,
                            end = session.endTime,
                            duration = session.sessionData.totalActiveTime,
                            distance = session.sessionData.totalDistance,
                            uid = session.id,
                            name = session.title ?: stringResource(R.string.no_title),
                            steps = "0",
                            sourceAppName = appInfo?.appLabel
                                ?: stringResource(R.string.unknown_app),
                            sourceAppIcon = getIcon(appInfo?.packageName, LocalContext.current),
                            onDeleteClick = { uid ->
                                onDeleteClick(uid)
                            },
                            onDetailsClick = { uid ->
                                onDetailsClick(uid)
                            }
                        )
                    }*/
                    /*items(dailySessionsList.exerciseSessions) { session ->
                        Log.d(TAG, "${session.sessionData.totalActiveTime?.formatTime()}")
                        val appInfo = session.sourceAppInfo
                        ExerciseSessionRow(
                            exerciseType = session.exerciseType,
                            start = session.startTime,
                            end = session.endTime,
                            duration = session.sessionData.totalActiveTime,
                            distance = session.sessionData.totalDistance,
                            uid = session.id,
                            name = session.title ?: stringResource(R.string.no_title),
                            steps = "0",
                            sourceAppName = appInfo?.appLabel
                                ?: stringResource(R.string.unknown_app),
                            sourceAppIcon = getIcon(appInfo?.packageName, LocalContext.current),
                            onDeleteClick = { uid ->
                                onDeleteClick(uid)
                            },
                            onDetailsClick = { uid ->
                                onDetailsClick(uid)
                            }
                        )
                    }*/
                    allSessions.forEach { dailySessionsList ->
                        if (dailySessionsList.dailySessionsSummary.totalActiveTime != Duration.ZERO && dailySessionsList.dailySessionsSummary.totalActiveTime != null) {
                            Log.d(
                                TAG,
                                "${dailySessionsList.dailySessionsSummary.date}: ${dailySessionsList.dailySessionsSummary.totalActiveTime}"
                            )
                            item {
                                ExerciseSessionSeparator(
                                    dailySessionsSummary = dailySessionsList.dailySessionsSummary,
                                    points = dailySessionsList.dailySessionsSummary.totalPoints,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 20.dp)
                                        .padding(end = 20.dp)
                                )
                            }
                            items(dailySessionsList.exerciseSessions) { session ->
                                //Log.d(TAG, "${session.startTime}: ${session.sessionData.totalActiveTime?.formatTime()}")
                                val appInfo = session.sourceAppInfo
                                ExerciseSessionRow(exerciseType = session.exerciseType,
                                    start = session.startTime,
                                    end = session.endTime,
                                    duration = session.sessionData.totalActiveTime,
                                    distance = session.sessionData.totalDistance,
                                    points = session.points,
                                    uid = session.id,
                                    name = session.title ?: stringResource(R.string.no_title),
                                    steps = "0",
                                    sourceAppName = appInfo?.appLabel
                                        ?: stringResource(R.string.unknown_app),
                                    sourceAppIcon = getIcon(
                                        appInfo?.packageName, LocalContext.current
                                    ),
                                    onDeleteClick = { uid ->
                                        onDeleteClick(uid)
                                    },
                                    onDetailsClick = { uid ->
                                        onDetailsClick(uid)
                                    })
                            }
                        }
                    }
                }
            }
            CircularProgressBar(
                isDisplayed = loading, Modifier.size(60.dp)
            )
            PullRefreshIndicator(
                refreshing = refreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

fun getIcon(packageName: String?, context: Context): Drawable? {

    return when (packageName) {
        "com.sec.android.app.shealth" -> AppCompatResources.getDrawable(
            context, R.drawable.ic_samsung_health_logo
        )
        "com.google.android.apps.fitness" -> AppCompatResources.getDrawable(
            context, R.drawable.ic_google_fit_logo
        )
        else -> AppCompatResources.getDrawable(context, R.drawable.ic_samsung_health_logo)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun ExerciseSessionScreenPreview() {
    val context = LocalContext.current
    HealthConnectTheme {
        val runningStartTime = ZonedDateTime.now()
        val runningEndTime = runningStartTime.plusMinutes(30)
        val walkingStartTime = ZonedDateTime.now().minusMinutes(120)
        val walkingEndTime = walkingStartTime.plusMinutes(30)

        val appInfo = HealthConnectAppInfo(
            packageName = "com.example.myfitnessapp",
            appLabel = "My Fitness App",
            icon = context.getDrawable(R.drawable.ic_launcher_foreground)!!
        )

        val refreshing = false
        ExerciseSessionScreen(
            permissions = setOf(),
            permissionsGranted = true,
            sessionsList = listOf(
//                ExerciseSession(
//                    title = "Running",
//                    startTime = runningStartTime,
//                    endTime = runningEndTime,
//                    id = UUID.randomUUID().toString(),
//                    sourceAppInfo = appInfo
//                ),
//                ExerciseSession(
//                    title = "Running",
//                    startTime = walkingStartTime,
//                    endTime = walkingEndTime,
//                    id = UUID.randomUUID().toString(),
//                    sourceAppInfo = appInfo
//                )
            ),
            dailySessionsList = DailySessionsList(),
            allSessions = listOf(DailySessionsList()),
            loading = false,
            uiState = ExerciseSessionViewModel.UiState.Done,
            stepsList = listOf(
                StepSession(
                    title = "Walking",
                    startTime = runningStartTime,
                    endTime = runningEndTime,
                    id = UUID.randomUUID().toString(),
                    count = "3000",
                    sourceAppInfo = appInfo
                ), StepSession(
                    title = "Walking",
                    startTime = walkingStartTime,
                    endTime = walkingEndTime,
                    id = UUID.randomUUID().toString(),
                    count = "5000",
                    sourceAppInfo = appInfo
                )
            ),
            pullRefreshState = rememberPullRefreshState(refreshing, {}) ,
            refreshing = refreshing
        )
    }
}
