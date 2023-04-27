package com.example.ahealthychallenge.data.serializables

import com.example.ahealthychallenge.data.ExerciseSession
import com.example.ahealthychallenge.data.ExerciseSessionData
import com.example.ahealthychallenge.data.HealthConnectAppInfo
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
data class ExerciseSessionSerializable (
    val sessionData: ExerciseSessionDataSerializable = SerializableFactory.getExerciseSessionDataSerializable(ExerciseSessionData("null_uid")),
    val exerciseType: Int = 79,
    val startTime: ZoneDataTimeSerializable = SerializableFactory.getZoneDataTimeSerializable(ZonedDateTime.now()),
    val endTime: ZoneDataTimeSerializable = SerializableFactory.getZoneDataTimeSerializable(ZonedDateTime.now()),
    val id: String = "null_id",
    val title: String? = null,
    val sourceAppInfo: HealthConnectAppInfo? = null
)