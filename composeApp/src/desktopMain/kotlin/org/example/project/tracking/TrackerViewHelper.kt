package org.example.project.tracking

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.*
import org.example.project.network.ShipmentDto
import org.example.project.network.TrackingClient

class TrackerViewHelper {

    val trackedShipments = mutableStateListOf<TrackedShipment>()
    val trackingError = mutableStateOf<String?>(null)

    private val pollingScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    suspend fun trackShipment(id: String) {
        try {
            val dto: ShipmentDto = withContext(Dispatchers.IO) {
                TrackingClient.getShipment(id)
            }
            if (trackedShipments.any { it.id == dto.id }) return

            val tracked = TrackedShipment(
                id = dto.id,
                type = dto.type,
                status = mutableStateOf(dto.status),
                location = mutableStateOf(dto.location),
                expectedDelivery = mutableStateOf(dto.expectedDelivery),
                notes = mutableStateListOf<String>().apply { addAll(dto.notes) },
                updates = mutableStateListOf<String>().apply { addAll(dto.updates) }
            )

            trackedShipments += tracked
            trackingError.value = null

            startPollingShipment(tracked.id)

        } catch (e: Exception) {
            trackingError.value = "Shipment '${id}' not found."
        }
    }

    fun stopTracking(id: String) {
        val tracked = trackedShipments.find { it.id == id } ?: return
        trackedShipments.remove(tracked)
    }

    private fun startPollingShipment(id: String) {
        pollingScope.launch {
            while (trackedShipments.any { it.id == id }) {
                delay(1000) // every second

                try {
                    val dto = TrackingClient.getShipment(id)
                    val tracked = trackedShipments.find { it.id == id } ?: continue

                    tracked.status.value = dto.status
                    tracked.location.value = dto.location
                    tracked.expectedDelivery.value = dto.expectedDelivery

                    tracked.notes.clear()
                    tracked.notes.addAll(dto.notes)

                    tracked.updates.clear()
                    tracked.updates.addAll(dto.updates)
                } catch (e: Exception) {
                    println("Polling failed for shipment $id: ${e.message}")
                }
            }
        }
    }
}