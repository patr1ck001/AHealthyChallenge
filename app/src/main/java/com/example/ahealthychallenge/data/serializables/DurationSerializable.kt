package com.example.ahealthychallenge.data.serializables
import kotlinx.serialization.Serializable

@Serializable
data class DurationSerializable (
    val durationInSeconds: Long = 0
    )
