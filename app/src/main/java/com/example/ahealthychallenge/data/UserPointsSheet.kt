package com.example.ahealthychallenge.data


data class UserPointsSheet(
    var username: String? = null,
    var uid: String? = null,
    val pointsWalking: Int = 0,
    val pointRunning: Int = 0,
    val pointsCycling: Int = 0,
    val pointsWorkout: Int = 0,
    val totalPoints: Int = 0,
)



