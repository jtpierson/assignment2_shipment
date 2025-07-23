package org.example.project.network

data class ShipmentDto(
    val id : String,
    val status : String,
    val location : String,
    val expectedDelivery : String,
    val notes : List<String>,
    val updates : List<String>,
    val violations : List<String>
)