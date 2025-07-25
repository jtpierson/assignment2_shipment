package org.example.project.network
import kotlinx.serialization.Serializable

@Serializable
data class ShipmentDto(
    val id : String,
    val type : String,
    val status : String,
    val location : String,
    val expectedDelivery : String,
    val notes : List<String>,
    val updates : List<String>,
    val violations : List<String>
)