package com.example.ahealthychallenge.data.serializables

import androidx.health.connect.client.units.Length
import kotlinx.serialization.Serializable

@Serializable
data class LengthSerializable(
    val value: Double = 0.0,
    val type: String = "KILOMETERS"
)