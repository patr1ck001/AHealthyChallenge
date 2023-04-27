package com.example.ahealthychallenge.data.serializables


import kotlinx.serialization.Serializable
import java.time.Instant
import androidx.health.connect.client.units.Velocity

@Serializable
class SRSampleSerializable(
    val time: InstantSerializable = SerializableFactory.getInstantSerializable(Instant.now()),
    val speed: VelocitySerializable = SerializableFactory.getVelocitySerializable(Velocity.kilometersPerHour(0.0)),
)