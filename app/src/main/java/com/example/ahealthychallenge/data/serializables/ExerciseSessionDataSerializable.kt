package com.example.ahealthychallenge.data.serializables


import kotlinx.serialization.Serializable


@Serializable
data class ExerciseSessionDataSerializable(
    val uid: String,
    val totalActiveTime: DurationSerializable? = null,
    val totalSteps: Long? = null,
    val totalDistance: LengthSerializable? = null,
    val totalEnergyBurned: EnergySerializable? = null,
    val minHeartRate: Long? = null,
    val maxHeartRate: Long? = null,
    val avgHeartRate: Long? = null,
    val heartRateSeries: List<HeartRateRecordSerializable> = listOf(),
    val minSpeed: VelocitySerializable? = null,
    val maxSpeed: VelocitySerializable? = null,
    val avgSpeed: VelocitySerializable? = null,
    val speedRecord: List<SpeedRecordSerializable> = listOf()

    )