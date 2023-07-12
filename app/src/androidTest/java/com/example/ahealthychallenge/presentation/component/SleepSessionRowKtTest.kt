package com.example.ahealthychallenge.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.health.connect.client.records.SleepStageRecord
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.data.SleepSessionData
import com.example.ahealthychallenge.presentation.theme.HealthConnectTheme
import org.junit.Rule
import org.junit.Test
import java.time.Duration
import java.time.ZonedDateTime

class SleepSessionRowKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun dateTextIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                val end = ZonedDateTime.now()
                val start = end.minusHours(1)
                Column {
                    SleepSessionRow(
                        SleepSessionData(
                            uid = "123",
                            title = "My sleep",
                            notes = "Slept well",
                            startTime = start.toInstant(),
                            startZoneOffset = start.offset,
                            endTime = end.toInstant(),
                            endZoneOffset = end.offset,
                            duration = Duration.between(start, end),
                            stages = listOf(
                                SleepStageRecord(
                                    stage = SleepStageRecord.STAGE_TYPE_DEEP,
                                    startTime = start.toInstant(),
                                    startZoneOffset = start.offset,
                                    endTime = end.toInstant(),
                                    endZoneOffset = end.offset
                                )
                            )
                        ),
                        startExpanded = true
                    )
                }
            }
        }

        composeTestRule.onNode(hasText("Wed, 12 Jul")).assertExists().assertIsDisplayed()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun deleteButtonIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                val end = ZonedDateTime.now()
                val start = end.minusHours(1)
                Column {
                    SleepSessionRow(
                        SleepSessionData(
                            uid = "123",
                            title = "My sleep",
                            notes = "Slept well",
                            startTime = start.toInstant(),
                            startZoneOffset = start.offset,
                            endTime = end.toInstant(),
                            endZoneOffset = end.offset,
                            duration = Duration.between(start, end),
                            stages = listOf(
                                SleepStageRecord(
                                    stage = SleepStageRecord.STAGE_TYPE_DEEP,
                                    startTime = start.toInstant(),
                                    startZoneOffset = start.offset,
                                    endTime = end.toInstant(),
                                    endZoneOffset = end.offset
                                )
                            )
                        ),
                        startExpanded = true
                    )
                }
            }
        }
        composeTestRule.onNode(hasContentDescription("Delete")).assertExists().assertIsDisplayed()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun timeTextIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                val end = ZonedDateTime.now()
                val start = end.minusHours(1)
                Column {
                    SleepSessionRow(
                        SleepSessionData(
                            uid = "123",
                            title = "My sleep",
                            notes = "Slept well",
                            startTime = start.toInstant(),
                            startZoneOffset = start.offset,
                            endTime = end.toInstant(),
                            endZoneOffset = end.offset,
                            duration = Duration.between(start, end),
                            stages = listOf(
                                SleepStageRecord(
                                    stage = SleepStageRecord.STAGE_TYPE_DEEP,
                                    startTime = start.toInstant(),
                                    startZoneOffset = start.offset,
                                    endTime = end.toInstant(),
                                    endZoneOffset = end.offset
                                )
                            )
                        ),
                        startExpanded = true
                    )
                }
            }
        }
        composeTestRule.onNode(hasText("Time:")).assertExists().assertIsDisplayed()
    }


    @Test
    fun durationTextIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                val end = ZonedDateTime.now()
                val start = end.minusHours(1)
                Column {
                    SleepSessionRow(
                        SleepSessionData(
                            uid = "123",
                            title = "My sleep",
                            notes = "Slept well",
                            startTime = start.toInstant(),
                            startZoneOffset = start.offset,
                            endTime = end.toInstant(),
                            endZoneOffset = end.offset,
                            duration = Duration.between(start, end),
                            stages = listOf(
                                SleepStageRecord(
                                    stage = SleepStageRecord.STAGE_TYPE_DEEP,
                                    startTime = start.toInstant(),
                                    startZoneOffset = start.offset,
                                    endTime = end.toInstant(),
                                    endZoneOffset = end.offset
                                )
                            )
                        ),
                        startExpanded = true
                    )
                }
            }
        }
        composeTestRule.onNode(hasText("Duration:")).assertExists().assertIsDisplayed()
    }

    @Test
    fun durationValueTextIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                val end = ZonedDateTime.now()
                val start = end.minusHours(1)
                Column {
                    SleepSessionRow(
                        SleepSessionData(
                            uid = "123",
                            title = "My sleep",
                            notes = "Slept well",
                            startTime = start.toInstant(),
                            startZoneOffset = start.offset,
                            endTime = end.toInstant(),
                            endZoneOffset = end.offset,
                            duration = Duration.between(start, end),
                            stages = listOf(
                                SleepStageRecord(
                                    stage = SleepStageRecord.STAGE_TYPE_DEEP,
                                    startTime = start.toInstant(),
                                    startZoneOffset = start.offset,
                                    endTime = end.toInstant(),
                                    endZoneOffset = end.offset
                                )
                            )
                        ),
                        startExpanded = true
                    )
                }
            }
        }
        composeTestRule.onNode(hasText("1h00m")).assertExists().assertIsDisplayed()
    }

    @Test
    fun noteTextIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                val end = ZonedDateTime.now()
                val start = end.minusHours(1)
                Column {
                    SleepSessionRow(
                        SleepSessionData(
                            uid = "123",
                            title = "My sleep",
                            notes = "Slept well",
                            startTime = start.toInstant(),
                            startZoneOffset = start.offset,
                            endTime = end.toInstant(),
                            endZoneOffset = end.offset,
                            duration = Duration.between(start, end),
                            stages = listOf(
                                SleepStageRecord(
                                    stage = SleepStageRecord.STAGE_TYPE_DEEP,
                                    startTime = start.toInstant(),
                                    startZoneOffset = start.offset,
                                    endTime = end.toInstant(),
                                    endZoneOffset = end.offset
                                )
                            )
                        ),
                        startExpanded = true
                    )
                }
            }
        }
        composeTestRule.onNode(hasText("Notes:")).assertExists().assertIsDisplayed()
    }

    @Test
    fun noteValueTextIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                val end = ZonedDateTime.now()
                val start = end.minusHours(1)
                Column {
                    SleepSessionRow(
                        SleepSessionData(
                            uid = "123",
                            title = "My sleep",
                            notes = "Slept well",
                            startTime = start.toInstant(),
                            startZoneOffset = start.offset,
                            endTime = end.toInstant(),
                            endZoneOffset = end.offset,
                            duration = Duration.between(start, end),
                            stages = listOf(
                                SleepStageRecord(
                                    stage = SleepStageRecord.STAGE_TYPE_DEEP,
                                    startTime = start.toInstant(),
                                    startZoneOffset = start.offset,
                                    endTime = end.toInstant(),
                                    endZoneOffset = end.offset
                                )
                            )
                        ),
                        startExpanded = true
                    )
                }
            }
        }
        composeTestRule.onNode(hasText("Slept well")).assertExists().assertIsDisplayed()
    }

    @Test
    fun sleepStageTextIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                val end = ZonedDateTime.now()
                val start = end.minusHours(1)
                Column {
                    SleepSessionRow(
                        SleepSessionData(
                            uid = "123",
                            title = "My sleep",
                            notes = "Slept well",
                            startTime = start.toInstant(),
                            startZoneOffset = start.offset,
                            endTime = end.toInstant(),
                            endZoneOffset = end.offset,
                            duration = Duration.between(start, end),
                            stages = listOf(
                                SleepStageRecord(
                                    stage = SleepStageRecord.STAGE_TYPE_DEEP,
                                    startTime = start.toInstant(),
                                    startZoneOffset = start.offset,
                                    endTime = end.toInstant(),
                                    endZoneOffset = end.offset
                                )
                            )
                        ),
                        startExpanded = true
                    )
                }
            }
        }
        composeTestRule.onNode(hasText("Sleep stages:")).assertExists().assertIsDisplayed()
    }


    @Test
    fun noteSleepStageTextIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                val end = ZonedDateTime.now()
                val start = end.minusHours(1)
                Column {
                    SleepSessionRow(
                        SleepSessionData(
                            uid = "123",
                            title = "My sleep",
                            notes = "Slept well",
                            startTime = start.toInstant(),
                            startZoneOffset = start.offset,
                            endTime = end.toInstant(),
                            endZoneOffset = end.offset,
                            duration = Duration.between(start, end),
                            stages = listOf(
                                SleepStageRecord(
                                    stage = SleepStageRecord.STAGE_TYPE_DEEP,
                                    startTime = start.toInstant(),
                                    startZoneOffset = start.offset,
                                    endTime = end.toInstant(),
                                    endZoneOffset = end.offset
                                )
                            )
                        ),
                        startExpanded = true
                    )
                }
            }
        }
        composeTestRule.onNode(hasText("deep")).assertExists().assertIsDisplayed()
    }
}