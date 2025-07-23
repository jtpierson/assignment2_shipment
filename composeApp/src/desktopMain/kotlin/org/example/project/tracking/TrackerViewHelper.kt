package org.example.project.tracking

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.project.data.Shipment
import org.example.project.network.ShipmentDto
import org.example.project.network.TrackingClient


class TrackerViewHelper {

    val trackedShipments = mutableStateListOf<TrackedShipment>()
    val trackingError = mutableStateOf<String?>(null)

    suspend fun trackShipment(id: String) {
        try {
            val dto: ShipmentDto = withContext(Dispatchers.IO) {
                TrackingClient.getShipment(id)
            }

            if (trackedShipments.any { it.id == dto.id }) return

            val tracked = TrackedShipment(
                id = dto.id,
                status = mutableStateOf(dto.status),
                location = mutableStateOf(dto.location),
                expectedDelivery = mutableStateOf(dto.expectedDelivery),
                notes = mutableStateListOf<String>().apply { addAll(dto.notes) },
                updates = mutableStateListOf<String>().apply { addAll(dto.updates) }
            )

            trackedShipments += tracked
            trackingError.value = null
        } catch (e: Exception) {
            trackingError.value = "Shipment '${id}' not found."
        }
    }

    fun stopTracking(id: String) {
        val tracked = trackedShipments.find { it.id == id } ?: return
        trackedShipments.remove(tracked)
    }
}