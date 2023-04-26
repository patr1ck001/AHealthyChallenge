package com.example.ahealthychallenge.data.serializables

import androidx.health.connect.client.units.Length
import kotlinx.serialization.Serializable

@Serializable
data class LengthSerializable(
    val value: Double,
    val type: String
)