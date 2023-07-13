package com.example.ahealthychallenge.presentation.screen.screens

import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.units.Energy
import androidx.health.connect.client.units.Length
import androidx.health.connect.client.units.Velocity
import com.example.ahealthychallenge.data.ExerciseSessionData
import com.example.ahealthychallenge.presentation.screen.exercisesessiondetail.ExerciseSessionDetailScreen
import com.example.ahealthychallenge.presentation.screen.exercisesessiondetail.ExerciseSessionDetailViewModel
import com.example.ahealthychallenge.presentation.theme.HealthConnectTheme
import org.junit.Rule
import org.junit.Test
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.random.Random

class ExerciseSessionDetailScreenTest {
    private fun generateSpeedData(): List<SpeedRecord> {
        val data = mutableListOf<SpeedRecord.Sample>()
        val end = ZonedDateTime.now()
        var time = ZonedDateTime.now()
        for (index in 1..10) {
            time = end.minusMinutes(index.toLong())
            data.add(
                SpeedRecord.Sample(
                    time = time.toInstant(),
                    speed = Velocity.metersPerSecond((Random.nextDouble(1.0, 5.0)))
                )
            )
        }
        return listOf(
            SpeedRecord(
                startTime = time.toInstant(),
                startZoneOffset = time.offset,
                endTime = end.toInstant(),
                endZoneOffset = end.offset,
                samples = data
            )
        )
    }

    private fun generateHeartRateSeries(): List<HeartRateRecord> {
        val data = mutableListOf<HeartRateRecord.Sample>()
        val end = ZonedDateTime.now()
        var time = ZonedDateTime.now()
        for (index in 1..10) {
            time = end.minusMinutes(index.toLong())
            data.add(
                HeartRateRecord.Sample(
                    time = time.toInstant(),
                    beatsPerMinute = Random.nextLong(55, 180)
                )
            )
        }
        return listOf(
            HeartRateRecord(
                startTime = time.toInstant(),
                startZoneOffset = time.offset,
                endTime = end.toInstant(),
                endZoneOffset = end.offset,
                samples = data
            )
        )
    }

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun totalDurationTimeIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                val uid = UUID.randomUUID().toString()
                val sessionMetrics = ExerciseSessionData(
                    uid = uid,
                    totalSteps = 5152,
                    totalDistance = Length.meters(11923.4),
                    totalEnergyBurned = Energy.calories(1131.2),
                    minHeartRate = 55,
                    maxHeartRate = 103,
                    avgHeartRate = 77,
                    heartRateSeries = generateHeartRateSeries(),
                    minSpeed = Velocity.metersPerSecond(2.5),
                    maxSpeed = Velocity.metersPerSecond(3.1),
                    avgSpeed = Velocity.metersPerSecond(2.8),
                    speedRecord = generateSpeedData(),
                )

                ExerciseSessionDetailScreen(
                    permissions = setOf(),
                    permissionsGranted = true,
                    sessionMetrics = sessionMetrics,
                    uiState = ExerciseSessionDetailViewModel.UiState.Done
                )
            }
        }
        composeTestRule.onNode(hasText("00:00:00")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }


    @Test
    fun totalStepsIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                val uid = UUID.randomUUID().toString()
                val sessionMetrics = ExerciseSessionData(
                    uid = uid,
                    totalSteps = 5152,
                    totalDistance = Length.meters(11923.4),
                    totalEnergyBurned = Energy.calories(1131.2),
                    minHeartRate = 55,
                    maxHeartRate = 103,
                    avgHeartRate = 77,
                    heartRateSeries = generateHeartRateSeries(),
                    minSpeed = Velocity.metersPerSecond(2.5),
                    maxSpeed = Velocity.metersPerSecond(3.1),
                    avgSpeed = Velocity.metersPerSecond(2.8),
                    speedRecord = generateSpeedData(),
                )

                ExerciseSessionDetailScreen(
                    permissions = setOf(),
                    permissionsGranted = true,
                    sessionMetrics = sessionMetrics,
                    uiState = ExerciseSessionDetailViewModel.UiState.Done
                )
            }
        }
        composeTestRule.onNode(hasText("Total steps")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun totalStepsNumberIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                val uid = UUID.randomUUID().toString()
                val sessionMetrics = ExerciseSessionData(
                    uid = uid,
                    totalSteps = 5152,
                    totalDistance = Length.meters(11923.4),
                    totalEnergyBurned = Energy.calories(1131.2),
                    minHeartRate = 55,
                    maxHeartRate = 103,
                    avgHeartRate = 77,
                    heartRateSeries = generateHeartRateSeries(),
                    minSpeed = Velocity.metersPerSecond(2.5),
                    maxSpeed = Velocity.metersPerSecond(3.1),
                    avgSpeed = Velocity.metersPerSecond(2.8),
                    speedRecord = generateSpeedData(),
                )

                ExerciseSessionDetailScreen(
                    permissions = setOf(),
                    permissionsGranted = true,
                    sessionMetrics = sessionMetrics,
                    uiState = ExerciseSessionDetailViewModel.UiState.Done
                )
            }
        }
        composeTestRule.onNode(hasText("5152")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun totalDistanceIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                val uid = UUID.randomUUID().toString()
                val sessionMetrics = ExerciseSessionData(
                    uid = uid,
                    totalSteps = 5152,
                    totalDistance = Length.meters(11923.4),
                    totalEnergyBurned = Energy.calories(1131.2),
                    minHeartRate = 55,
                    maxHeartRate = 103,
                    avgHeartRate = 77,
                    heartRateSeries = generateHeartRateSeries(),
                    minSpeed = Velocity.metersPerSecond(2.5),
                    maxSpeed = Velocity.metersPerSecond(3.1),
                    avgSpeed = Velocity.metersPerSecond(2.8),
                    speedRecord = generateSpeedData(),
                )

                ExerciseSessionDetailScreen(
                    permissions = setOf(),
                    permissionsGranted = true,
                    sessionMetrics = sessionMetrics,
                    uiState = ExerciseSessionDetailViewModel.UiState.Done
                )
            }
        }
        composeTestRule.onNode(hasText("Total distance")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun totalDistanceAmountIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                val uid = UUID.randomUUID().toString()
                val sessionMetrics = ExerciseSessionData(
                    uid = uid,
                    totalSteps = 5152,
                    totalDistance = Length.meters(11923.4),
                    totalEnergyBurned = Energy.calories(1131.2),
                    minHeartRate = 55,
                    maxHeartRate = 103,
                    avgHeartRate = 77,
                    heartRateSeries = generateHeartRateSeries(),
                    minSpeed = Velocity.metersPerSecond(2.5),
                    maxSpeed = Velocity.metersPerSecond(3.1),
                    avgSpeed = Velocity.metersPerSecond(2.8),
                    speedRecord = generateSpeedData(),
                )

                ExerciseSessionDetailScreen(
                    permissions = setOf(),
                    permissionsGranted = true,
                    sessionMetrics = sessionMetrics,
                    uiState = ExerciseSessionDetailViewModel.UiState.Done
                )
            }
        }
        composeTestRule.onNode(hasText("11923.4 meters")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun totalCalories() {
        composeTestRule.setContent {
            HealthConnectTheme {
                val uid = UUID.randomUUID().toString()
                val sessionMetrics = ExerciseSessionData(
                    uid = uid,
                    totalSteps = 5152,
                    totalDistance = Length.meters(11923.4),
                    totalEnergyBurned = Energy.calories(1131.2),
                    minHeartRate = 55,
                    maxHeartRate = 103,
                    avgHeartRate = 77,
                    heartRateSeries = generateHeartRateSeries(),
                    minSpeed = Velocity.metersPerSecond(2.5),
                    maxSpeed = Velocity.metersPerSecond(3.1),
                    avgSpeed = Velocity.metersPerSecond(2.8),
                    speedRecord = generateSpeedData(),
                )

                ExerciseSessionDetailScreen(
                    permissions = setOf(),
                    permissionsGranted = true,
                    sessionMetrics = sessionMetrics,
                    uiState = ExerciseSessionDetailViewModel.UiState.Done
                )
            }
        }
        composeTestRule.onNode(hasText("Total calories")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun totalCaloriesAmount() {
        composeTestRule.setContent {
            HealthConnectTheme {
                val uid = UUID.randomUUID().toString()
                val sessionMetrics = ExerciseSessionData(
                    uid = uid,
                    totalSteps = 5152,
                    totalDistance = Length.meters(11923.4),
                    totalEnergyBurned = Energy.calories(1131.2),
                    minHeartRate = 55,
                    maxHeartRate = 103,
                    avgHeartRate = 77,
                    heartRateSeries = generateHeartRateSeries(),
                    minSpeed = Velocity.metersPerSecond(2.5),
                    maxSpeed = Velocity.metersPerSecond(3.1),
                    avgSpeed = Velocity.metersPerSecond(2.8),
                    speedRecord = generateSpeedData(),
                )

                ExerciseSessionDetailScreen(
                    permissions = setOf(),
                    permissionsGranted = true,
                    sessionMetrics = sessionMetrics,
                    uiState = ExerciseSessionDetailViewModel.UiState.Done
                )
            }
        }
        composeTestRule.onNode(hasText("1131.2")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }
    @Test
    fun speedStatIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                val uid = UUID.randomUUID().toString()
                val sessionMetrics = ExerciseSessionData(
                    uid = uid,
                    totalSteps = 5152,
                    totalDistance = Length.meters(11923.4),
                    totalEnergyBurned = Energy.calories(1131.2),
                    minHeartRate = 55,
                    maxHeartRate = 103,
                    avgHeartRate = 77,
                    heartRateSeries = generateHeartRateSeries(),
                    minSpeed = Velocity.metersPerSecond(2.5),
                    maxSpeed = Velocity.metersPerSecond(3.1),
                    avgSpeed = Velocity.metersPerSecond(2.8),
                    speedRecord = generateSpeedData(),
                )

                ExerciseSessionDetailScreen(
                    permissions = setOf(),
                    permissionsGranted = true,
                    sessionMetrics = sessionMetrics,
                    uiState = ExerciseSessionDetailViewModel.UiState.Done
                )
            }
        }
        composeTestRule.onNode(hasText("Speed stats (m/s)")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun speedStatMinIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                val uid = UUID.randomUUID().toString()
                val sessionMetrics = ExerciseSessionData(
                    uid = uid,
                    totalSteps = 5152,
                    totalDistance = Length.meters(11923.4),
                    totalEnergyBurned = Energy.calories(1131.2),
                    minHeartRate = 55,
                    maxHeartRate = 103,
                    avgHeartRate = 77,
                    heartRateSeries = generateHeartRateSeries(),
                    minSpeed = Velocity.metersPerSecond(2.5),
                    maxSpeed = Velocity.metersPerSecond(3.1),
                    avgSpeed = Velocity.metersPerSecond(2.8),
                    speedRecord = generateSpeedData(),
                )

                ExerciseSessionDetailScreen(
                    permissions = setOf(),
                    permissionsGranted = true,
                    sessionMetrics = sessionMetrics,
                    uiState = ExerciseSessionDetailViewModel.UiState.Done
                )
            }
        }
        composeTestRule.onNode(hasText("Min: 2.5")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun speedStatMaxIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                val uid = UUID.randomUUID().toString()
                val sessionMetrics = ExerciseSessionData(
                    uid = uid,
                    totalSteps = 5152,
                    totalDistance = Length.meters(11923.4),
                    totalEnergyBurned = Energy.calories(1131.2),
                    minHeartRate = 55,
                    maxHeartRate = 103,
                    avgHeartRate = 77,
                    heartRateSeries = generateHeartRateSeries(),
                    minSpeed = Velocity.metersPerSecond(2.5),
                    maxSpeed = Velocity.metersPerSecond(3.1),
                    avgSpeed = Velocity.metersPerSecond(2.8),
                    speedRecord = generateSpeedData(),
                )

                ExerciseSessionDetailScreen(
                    permissions = setOf(),
                    permissionsGranted = true,
                    sessionMetrics = sessionMetrics,
                    uiState = ExerciseSessionDetailViewModel.UiState.Done
                )
            }
        }
        composeTestRule.onNode(hasText("Max: 3.1")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }
    @Test
    fun speedStatAvgIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                val uid = UUID.randomUUID().toString()
                val sessionMetrics = ExerciseSessionData(
                    uid = uid,
                    totalSteps = 5152,
                    totalDistance = Length.meters(11923.4),
                    totalEnergyBurned = Energy.calories(1131.2),
                    minHeartRate = 55,
                    maxHeartRate = 103,
                    avgHeartRate = 77,
                    heartRateSeries = generateHeartRateSeries(),
                    minSpeed = Velocity.metersPerSecond(2.5),
                    maxSpeed = Velocity.metersPerSecond(3.1),
                    avgSpeed = Velocity.metersPerSecond(2.8),
                    speedRecord = generateSpeedData(),
                )

                ExerciseSessionDetailScreen(
                    permissions = setOf(),
                    permissionsGranted = true,
                    sessionMetrics = sessionMetrics,
                    uiState = ExerciseSessionDetailViewModel.UiState.Done
                )
            }
        }
        composeTestRule.onNode(hasText("Avg: 2.8")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

    @Test
    fun speedSerieIsDisplayed() {
        composeTestRule.setContent {
            HealthConnectTheme {
                val uid = UUID.randomUUID().toString()
                val sessionMetrics = ExerciseSessionData(
                    uid = uid,
                    totalSteps = 5152,
                    totalDistance = Length.meters(11923.4),
                    totalEnergyBurned = Energy.calories(1131.2),
                    minHeartRate = 55,
                    maxHeartRate = 103,
                    avgHeartRate = 77,
                    heartRateSeries = generateHeartRateSeries(),
                    minSpeed = Velocity.metersPerSecond(2.5),
                    maxSpeed = Velocity.metersPerSecond(3.1),
                    avgSpeed = Velocity.metersPerSecond(2.8),
                    speedRecord = generateSpeedData(),
                )

                ExerciseSessionDetailScreen(
                    permissions = setOf(),
                    permissionsGranted = true,
                    sessionMetrics = sessionMetrics,
                    uiState = ExerciseSessionDetailViewModel.UiState.Done
                )
            }
        }
        composeTestRule.onNode(hasText("Speed series (m/s)")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

}