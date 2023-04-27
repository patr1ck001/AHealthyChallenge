package com.example.ahealthychallenge.data.serializables

import kotlinx.serialization.Serializable

@Serializable
data class VelocitySerializable(
    val value: Double = 0.0,
    val type: String = "KILOMETERS_PER_HOUR"
)