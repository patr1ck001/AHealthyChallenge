package com.example.ahealthychallenge.presentation.screen.InputReadings

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
import androidx.health.connect.client.units.Mass
import com.example.ahealthychallenge.data.WeightData
import com.example.ahealthychallenge.presentation.screen.inputreadings.InputReadingsScreen
import com.example.ahealthychallenge.presentation.screen.inputreadings.InputReadingsViewModel
import org.junit.Rule
import org.junit.Test
import java.time.ZonedDateTime

class InputReadingsTest {
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


    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun recordTest() {

        composeTestRule.setContent {
            InputReadingsScreen(
                permissions = permissions,
                permissionsGranted = true,
                readingsList = listOf(
                    WeightData(Mass.kilograms(70.0), "id", ZonedDateTime.of(2023,8,9,15,24,0,0,ZonedDateTime.now().zone), null)
                ),
                uiState = InputReadingsViewModel.UiState.Done,
                weeklyAvg = Mass.kilograms(70.0)
            )
        }

        composeTestRule.onNode(hasText("New Record (Kg)")).assertExists()
        composeTestRule.onNode(hasText("Add")).assertExists()
        composeTestRule.onNode(hasText("Weight must be between 0 and 1000")).assertExists()
        composeTestRule.onNode(hasText("Previous Measurements")).assertExists()
        composeTestRule.onNode(hasText("Aug 9, 2023, 3:24:00 PM")).assertExists()
        composeTestRule.onNode(hasContentDescription("Delete")).assertExists()
        composeTestRule.onRoot().printToLog("currentLabelExists")
    }

}