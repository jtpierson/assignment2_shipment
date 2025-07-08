package org.example.project.tracking

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import org.example.project.data.Shipment
import org.example.project.observer.Observer
import org.example.project.observer.Subject

class TrackerViewHelper : Observer<Shipment> {

    // Holds all currently tracked shipments and their UI states
    val trackedShipments = mutableStateListOf<TrackedShipment>()

    override fun update(subject: Shipment) {
        val tracked = trackedShipments.find { it.id == subject.id } ?: return

        tracked.status.value = subject.status
        tracked.location.value = subject.currentLocation
        tracked.expectedDelivery.value = formatTimestamp(subject.expectedDeliveryDateTimestamp)

        tracked.notes.clear()
        tracked.notes.addAll(subject.notes)

        tracked.updates.clear()
        tracked.updates.addAll(
            subject.updateHistory.map {
                "Shipment went from ${it.previousStatus} to ${it.newStatus} on ${formatTimestamp(it.timestamp)}"
            }
        )
    }

    fun trackShipment(id: String) {
        val shipment = TrackingSimulator.findShipment(id)

        if (shipment != null) {
            if (trackedShipments.any { it.id == shipment.id }) return // Already tracked

            shipment.registerObserver(this)

            val tracked = TrackedShipment(
                id = shipment.id,
                status = mutableStateOf(shipment.status),
                location = mutableStateOf(shipment.currentLocation),
                expectedDelivery = mutableStateOf(formatTimestamp(shipment.expectedDeliveryDateTimestamp)),
                notes = mutableStateListOf<String>().apply { addAll(shipment.notes) },
                updates = mutableStateListOf<String>().apply {
                    addAll(shipment.updateHistory.map {
                        "Shipment went from ${it.previousStatus} to ${it.newStatus} on ${formatTimestamp(it.timestamp)}"
                    })
                }
            )

            trackedShipments += tracked
        }
    }

    fun stopTracking(id: String) {
        val tracked = trackedShipments.find { it.id == id } ?: return
        TrackingSimulator.findShipment(id)?.removeObserver(this)
        trackedShipments.remove(tracked)
    }

    private fun formatTimestamp(timestamp: Long): String {
        val formatter = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return formatter.format(java.util.Date(timestamp))
    }
}