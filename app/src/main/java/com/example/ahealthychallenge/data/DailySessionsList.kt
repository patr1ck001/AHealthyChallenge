package com.example.ahealthychallenge.data

data class DailySessionsList(
    val dailySessionsSummary: DailySessionsSummary = DailySessionsSummary(),
    val exerciseSessions: List<ExerciseSession> = listOf()
)