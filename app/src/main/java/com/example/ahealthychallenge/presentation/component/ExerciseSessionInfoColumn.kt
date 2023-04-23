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
package com.example.ahealthychallenge.presentation.component

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.units.Length
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.data.formatTime
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
    Log.d(TAG, "the exercise type number: $exerciseType")
    Column(
        modifier = modifier.clickable {
            onClick(uid)
        },
        verticalArrangement = Arrangement.Center
    ) {
        // TODO: set the name of the app as last element and define the priority in the UI with contentAlpha
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


        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            SessionSummary(labelId = R.string.nothing, value = duration?.formatTime().toString())
            // TODO: change hardcoded color to theme changing color
            Icon(
                painter = painterResource(id = R.drawable.ic_vertical_line),
                tint = Color.LightGray,
                contentDescription = null,
            )
            SessionSummary(
                value = distance?.inKilometers?.toBigDecimal()?.setScale(2, RoundingMode.UP)
                    .toString(),
                labelId = R.string.Kilometers
            )
            // TODO: change hardcoded color to theme changing color
            Icon(
                painter = painterResource(id = R.drawable.ic_vertical_line),
                tint = Color.LightGray,
                contentDescription = null,
            )
            SessionSummary(labelId = R.string.points, value = "2")

        }

        // TODO: figure out the theme situation
        // TODO: when distance = 0, it display null
        // TODO: fix the clickable surface
        // TODO: add loading animation for the exercise session
        // TODO: sleep session hade data for more time. check how and apply it to esercise sessions
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


