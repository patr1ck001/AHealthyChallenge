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
package com.example.healthconnectsample.presentation.component

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.units.Length
import com.example.healthconnectsample.R
import com.example.healthconnectsample.data.formatTime
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import java.math.RoundingMode
import java.time.Duration
import java.time.ZonedDateTime

/**
 * Displays summary information about the [ExerciseSessionRecord]
 */
const val TAG = "ExerciseSessionInfoColumn"

@Composable
fun ExerciseSessionInfoColumn(
    modifier: Modifier,
    exerciseType: Int,
    start: ZonedDateTime,
    end: ZonedDateTime,
    duration: Duration?,
    uid: String,
    name: String,
    steps: String,
    sourceAppName: String,
    sourceAppIcon: Drawable?,
    onClick: (String) -> Unit = {},
    distance: Length?
) {
    Column(
        modifier = modifier.clickable {
            onClick(uid)
        }
    ) {
        Text(getExerciseType(exerciseType))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .padding(4.dp, 2.dp)
                    .height(16.dp)
                    .width(16.dp),
                painter = rememberDrawablePainter(drawable = sourceAppIcon),
                contentDescription = "App Icon"
            )
            Text(
                text = sourceAppName,
                fontStyle = FontStyle.Italic
            )
        }
        // TODO: add unity of measurement
        // TODO: add string resources for unity of measurement
        // TODO: when distance = 0, it display null
        // TODO: add separator element in the session summary component
        // TODO: fix the clickable surface
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            SessionSummary(labelId = R.string.duration, value = duration?.formatTime().toString())
            SessionSummary(
                labelId = R.string.distance,
                value = distance?.inKilometers?.toBigDecimal()?.setScale(2, RoundingMode.UP)
                    .toString()
            )
            SessionSummary(labelId = R.string.points, value = "2")
        }
    }
}

fun getExerciseType(recordType: Int): String {
    val exerciseType = when (recordType) {
//TODO: display the correct exercise session
        56 -> "Running"
        79 -> "Walking"
        else -> "Cycling"
    }
    return exerciseType
}


/*@Preview
@Composable
fun ExerciseSessionInfoColumnPreview() {
    HealthConnectTheme {
        ExerciseSessionInfoColumn(
            modifier = Modifier.weight(weight = 1f),
            ExerciseSessionRecord.EXERCISE_TYPE_RUNNING,
            ZonedDateTime.now().minusMinutes(30),
            ZonedDateTime.now(),
            UUID.randomUUID().toString(),
            "Running",
            "0",
            "My Fitness App",
            null
        )
    }
}*/


