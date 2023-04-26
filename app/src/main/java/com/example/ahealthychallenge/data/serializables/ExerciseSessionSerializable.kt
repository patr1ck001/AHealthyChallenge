package com.example.ahealthychallenge.data.serializables

import com.example.ahealthychallenge.data.HealthConnectAppInfo
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseSessionSerializable (
    val sessionData: ExerciseSessionDataSerializable,
    val exerciseType: Int,
    val startTime: ZoneDataTimeSerializable,
    val endTime: ZoneDataTimeSerializable,
    val id: String,
    val title: String?,
    val sourceAppInfo: HealthConnectAppInfo?
)