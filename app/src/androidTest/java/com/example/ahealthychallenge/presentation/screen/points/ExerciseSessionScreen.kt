package com.example.ahealthychallenge.presentation.screen.points

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.navigation.compose.rememberNavController
import com.example.ahealthychallenge.data.DailySessionsList
import com.example.ahealthychallenge.data.DailySessionsSummary
import com.example.ahealthychallenge.data.ExerciseSession
import com.example.ahealthychallenge.data.ExerciseSessionData
import com.example.ahealthychallenge.data.HealthConnectManager
import com.example.ahealthychallenge.presentation.screen.exercisesession.ExerciseSessionScreen
import com.example.ahealthychallenge.presentation.screen.exercisesession.ExerciseSessionViewModel
import com.example.ahealthychallenge.presentation.screen.welcomeScreen.WelcomeScreen
import com.example.ahealthychallenge.presentation.utils.NavigationType
import org.junit.Rule
import org.junit.Test
import java.time.Duration
import java.time.ZonedDateTime

class ExerciseSessionScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()


    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun exerciseSessionTest() {

        val permissions = setOf(
            HealthPermission.getWritePermission(ExerciseSessionRecord::class),
            HealthPermission.getReadPermission(ExerciseSessionRecord::class),
            HealthPermission.getWritePermission(StepsRecord::class),
            HealthPermission.getWritePermission(SpeedRecord::class),
            HealthPermission.getWritePermission(DistanceRecord::class),
            HealthPermission.getWritePermission(TotalCaloriesBurnedRecord::class),
            HealthPermission.getWritePermission(HeartRateRecord::class),
            HealthPermission.getReadPermission(DistanceRecord::class),
            HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class),
            HealthPermission.getReadPermission(HeartRateRecord::class),
            HealthPermission.getReadPermission(SpeedRecord::class),
            HealthPermission.getReadPermission(StepsRecord::class),
        )

        val exerciseSessionData = ExerciseSessionData("session")
        val zoneDateTime = ZonedDateTime.now()
        val sessionList = listOf(
            ExerciseSession(
                sessionData = exerciseSessionData,
                exerciseType = 5,
                startTime = zoneDateTime,
                endTime = zoneDateTime,
                id = "id",
                title = null,
                sourceAppInfo = null,
                points = 2
            ),
            ExerciseSession(
                sessionData = exerciseSessionData,
                exerciseType = 5,
                startTime = zoneDateTime,
                endTime = zoneDateTime,
                id = "id",
                title = null,
                sourceAppInfo = null,
                points = 2
            )
        )
        val dailySessionsList = DailySessionsList(
            DailySessionsSummary(
                totalActiveTime = Duration.ofHours(2)
            ),
        )

        composeTestRule.setContent {
            ExerciseSessionScreen(
                navigationType = NavigationType.BOTTOM_NAVIGATION,
                permissions = permissions,
                permissionsGranted = true,
                sessionsList = sessionList,
                dailySessionsList = dailySessionsList,
                loading = false,
                allSessions = listOf(dailySessionsList),
                stepsList = listOf(),
                uiState = ExerciseSessionViewModel.UiState.Done,
                pullRefreshState = rememberPullRefreshState(false, {}),
                refreshing = false
            )
        }

        composeTestRule.onNode(hasText("Today")).assertExists()
        composeTestRule.onNode(hasText("0 points")).assertExists()
        composeTestRule.onNode(hasText("02:00:00")).assertExists()
        composeTestRule.onNode(hasContentDescription("Refreshing")).assertExists()

    }
}
