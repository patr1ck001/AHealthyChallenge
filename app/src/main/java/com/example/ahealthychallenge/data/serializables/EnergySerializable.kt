package com.example.ahealthychallenge.data.serializables

import kotlinx.serialization.Serializable

@Serializable
data class EnergySerializable(
    val value: Double,
    val type: String
)