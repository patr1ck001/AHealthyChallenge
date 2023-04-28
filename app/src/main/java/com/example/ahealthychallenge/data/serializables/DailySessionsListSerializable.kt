package com.example.ahealthychallenge.data.serializables

import kotlinx.serialization.Serializable

@Serializable
data class DailySessionsListSerializable(
    val dailySessionsSummary: DailySessionSummarySerializable = DailySessionSummarySerializable(),
    val exerciseSessions: List<ExerciseSessionSerializable> = listOf()
)