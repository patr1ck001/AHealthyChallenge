package com.example.ahealthychallenge.data.serializables


import kotlinx.serialization.Serializable

@Serializable
class SRSampleSerializable(
    val time: InstantSerializable,
    val speed: VelocitySerializable,
)