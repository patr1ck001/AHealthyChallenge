package com.example.ahealthychallenge.presentation.screen.sleepSessions

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.health.connect.client.records.SleepStageRecord
import com.example.ahealthychallenge.data.SleepSessionData
import com.example.ahealthychallenge.presentation.screen.sleepsession.SleepSessionScreen
import com.example.ahealthychallenge.presentation.screen.sleepsession.SleepSessionViewModel
import com.example.ahealthychallenge.presentation.theme.HealthConnectTheme
import org.junit.Rule
import org.junit.Test
import java.time.Duration
import java.time.ZonedDateTime

class SleepSessionScreenTest {


    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun pointExerciseTextTest() {
        composeTestRule.setContent {
            HealthConnectTheme {
                val end2 = ZonedDateTime.now()
                val start2 = end2.minusHours(5)
                val end1 = end2.minusDays(1)
                val start1 = end1.minusHours(5)
                SleepSessionScreen(
                    permissions = setOf(),
                    permissionsGranted = true,
                    sessionsList = listOf(
                        SleepSessionData(
                            uid = "123",
                            title = "My sleep",
                            notes = "Slept well",
                            startTime = start1.toInstant(),
                            startZoneOffset = start1.offset,
                            endTime = end1.toInstant(),
                            endZoneOffset = end1.offset,
                            duration = Duration.between(start1, end1),
                            stages = listOf(
                                SleepStageRecord(
                                    stage = SleepStageRecord.STAGE_TYPE_DEEP,
                                    startTime = start1.toInstant(),
                                    startZoneOffset = start1.offset,
                                    endTime = end1.toInstant(),
                                    endZoneOffset = end1.offset
                                )
                            )
                        ),
                        SleepSessionData(
                            uid = "123",
                            title = "My sleep",
                            notes = "Slept well",
                            startTime = start2.toInstant(),
                            startZoneOffset = start2.offset,
                            endTime = end2.toInstant(),
                            endZoneOffset = end2.offset,
                            duration = Duration.between(start2, end2),
                            stages = listOf(
                                SleepStageRecord(
                                    stage = SleepStageRecord.STAGE_TYPE_DEEP,
                                    startTime = start2.toInstant(),
                                    startZoneOffset = start2.offset,
                                    endTime = end2.toInstant(),
                                    endZoneOffset = end2.offset
                                )
                            )
                        )
                    ),
                    uiState = SleepSessionViewModel.UiState.Done
                )
            }
        }
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

}