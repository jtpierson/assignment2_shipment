package org.example.project.tracking

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import org.example.project.data.Shipment
import org.example.project.observer.Observer
import org.example.project.observer.Subject

class TrackerViewHelper : Observer<Shipment> {

    // Holds all currently tracked shipments and their UI states
    val trackedShipments = mutableStateListOf<TrackedShipment>()

    // When shipment is changed, notifyObservers() is called and this updates it for the UI
    override fun update(subject: Shipment) {
        // Find matching shipment
        val tracked = trackedShipments.find { candidate -> candidate.id == subject.id } ?: return

        tracked.status.value = subject.status
        tracked.location.value = subject.currentLocation
        tracked.expectedDelivery.value = formatTimestamp(subject.expectedDeliveryDateTimestamp)

        tracked.notes.clear()
        tracked.notes.addAll(subject.notes)

        tracked.updates.clear()
        tracked.updates.addAll(
            subject.updateHistory.map { trackedUpdate ->
                "Shipment went from ${trackedUpdate.previousStatus} to ${trackedUpdate.newStatus} on ${formatTimestamp(trackedUpdate.timestamp)}"
            }
        )
    }

    fun trackShipment(id: String) {
        val shipment = TrackingSimulator.findShipment(id)

        if (shipment != null) {
            if (trackedShipments.any {trackedShipment -> trackedShipment.id == shipment.id }) return // Already tracked

            shipment.registerObserver(this)

            val tracked = TrackedShipment(
                id = shipment.id,
                status = mutableStateOf(shipment.status),
                location = mutableStateOf(shipment.currentLocation),
                expectedDelivery = mutableStateOf(formatTimestamp(shipment.expectedDeliveryDateTimestamp)),
                notes = mutableStateListOf<String>().apply { addAll(shipment.notes) },
                updates = mutableStateListOf<String>().apply {
                    addAll(shipment.updateHistory.map {update ->
                        "Shipment went from ${update.previousStatus} to ${update.newStatus} on ${formatTimestamp(update.timestamp)}"
                    })
                }
            )

            trackedShipments += tracked
        }
    }

    fun stopTracking(id: String) {
        val tracked = trackedShipments.find {trackedShipment -> trackedShipment.id == id } ?: return
        TrackingSimulator.findShipment(id)?.removeObserver(this)
        trackedShipments.remove(tracked)
    }

    private fun formatTimestamp(timestamp: Long): String {
        val formatter = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return formatter.format(java.util.Date(timestamp))
    }
}