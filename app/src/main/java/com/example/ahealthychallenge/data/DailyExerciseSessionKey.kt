package com.example.ahealthychallenge.data

import java.time.ZonedDateTime

data class DailyExerciseSessionKey(
    val date: ZonedDateTime = ZonedDateTime.now(),
    val key: String = "no_key"
)