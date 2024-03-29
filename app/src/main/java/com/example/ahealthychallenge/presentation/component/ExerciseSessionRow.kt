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
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.units.Length
import com.example.ahealthychallenge.R
import com.example.ahealthychallenge.presentation.theme.HealthConnectTheme
import java.time.ZonedDateTime
import java.util.UUID
import java.time.Duration

/**
 * Creates a row to represent an [ExerciseSessionRecord]
 */
@Composable
fun ExerciseSessionRow(
    exerciseType: Int,
    start: ZonedDateTime,
    end: ZonedDateTime,
    duration: Duration?,
    points: Int,
    uid: String,
    name: String,
    steps: String,
    sourceAppName: String,
    sourceAppIcon: Drawable?,
    onDeleteClick: (String) -> Unit = {},
    onDetailsClick: (String) -> Unit = {},
    distance: Length?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp)
        ) {
                Icon(
                    painter = painterResource(id = getIconId(exerciseType)),
                    modifier = Modifier.size(size = 40.dp),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary
                )
        }
        ExerciseSessionInfoColumn(
            modifier = Modifier.weight(5f),
            exerciseType = exerciseType,
            start = start,
            end = end,
            duration = duration,
            distance = distance,
            points = points,
            uid = uid,
            name = name,
            steps = steps,
            sourceAppName = sourceAppName,
            sourceAppIcon = sourceAppIcon,
            onClick = onDetailsClick
        )
    }
}

fun getIconId(recordType: Int): Int {
    val exerciseType = when (recordType) {
        5 -> R.drawable.ic_basketball
        8 -> R.drawable.ic_biking
        29 -> R.drawable.ic_football
        37 -> R.drawable.ic_hiking
        56 -> R.drawable.ic_runner
        61 -> R.drawable.ic_skiing
        62 -> R.drawable.ic_snowboarding
        73 -> R.drawable.ic_swimming_pool
        74 -> R.drawable.ic_swimming_pool
        75 -> R.drawable.ic_ping_pong
        76 -> R.drawable.ic_tennis
        78 -> R.drawable.ic_volleyball
        79 -> R.drawable.ic_step
        else -> R.drawable.ic_workout
    }
    return exerciseType
}


@Preview
@Composable
fun ExerciseSessionRowPreview() {
    val context = LocalContext.current
    HealthConnectTheme {
        ExerciseSessionRow(
            1,
            ZonedDateTime.now().minusMinutes(30),
            ZonedDateTime.now(),
            Duration.ZERO,
            points = 2,
            UUID.randomUUID().toString(),
            "Running",
            "0",
            sourceAppName = "My Fitness app",
            sourceAppIcon = context.getDrawable(R.drawable.ic_launcher_foreground),
            distance = Length.meters(100.0)
        )
    }
}
