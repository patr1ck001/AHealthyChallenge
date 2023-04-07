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
import android.icu.text.ListFormatter
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.units.Length
import com.example.healthconnectsample.R
import com.example.healthconnectsample.presentation.theme.HealthConnectTheme
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
                painter = painterResource(id = R.drawable.ic_running),
                contentDescription = null,
            )
        }
        ExerciseSessionInfoColumn(
            modifier = Modifier.weight(5f),
            exerciseType = exerciseType,
            start = start,
            end = end,
            duration = duration,
            distance = distance,
            uid = uid,
            name = name,
            steps = steps,
            sourceAppName = sourceAppName,
            sourceAppIcon = sourceAppIcon,
            onClick = onDetailsClick
        )
    }
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
            UUID.randomUUID().toString(),
            "Running",
            "0",
            sourceAppName = "My Fitness app",
            sourceAppIcon = context.getDrawable(R.drawable.ic_launcher_foreground),
            distance = Length.meters(100.0)
        )
    }
}
