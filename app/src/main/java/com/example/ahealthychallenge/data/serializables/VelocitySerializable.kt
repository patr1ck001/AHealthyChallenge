package com.example.ahealthychallenge.data.serializables

import kotlinx.serialization.Serializable

@Serializable
data class VelocitySerializable(
    val value: Double,
    val type: String
)