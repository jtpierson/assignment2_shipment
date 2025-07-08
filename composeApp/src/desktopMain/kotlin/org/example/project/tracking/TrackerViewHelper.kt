package org.example.project.tracking

import androidx.compose.runtime.mutableStateOf
import org.example.project.data.Shipment
import org.example.project.observer.Observer
import org.example.project.observer.Subject

class TrackerViewHelper : Observer {

    var shipmentId = mutableStateOf("")
        private set

    var shipmentTotes = mutableStateOf<List<String>>(emptyList())
        private set

    var shipmentUpdateHistory = mutableStateOf<List<String>>(emptyList())
        private set

    var expectedShipmentDeliveryDate = mutableStateOf("")
        private set

    var shipmentStatus = mutableStateOf("")
        private set

    private val trackedShipments: MutableMap<String, Shipment> = mutableMapOf()

    override fun update(subject: Subject) {
        val shipment = subject as Shipment

        shipmentId.value = shipment.id
        shipmentTotes.value = shipment.notes
        shipmentStatus.value = shipment.status
        expectedShipmentDeliveryDate.value = formatTimestamp(shipment.expectedDeliveryDateTimestamp)

        shipmentUpdateHistory.value = shipment.updateHistory.map {
            "Shipment went from ${it.previousStatus} to ${it.newStatus} on ${formatTimestamp(it.timestamp)}"
        }
    }

    private fun formatTimestamp(timestamp: Long): String {
        // Format to readable string
        val formatter = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return formatter.format(java.util.Date(timestamp))
    }

    fun trackShipment(id : String) {
        val shipment = TrackingSimulator.findShipment(id)

        if (shipment != null) {
            shipment.registerObserver(this)
            trackedShipments[shipment.id] = shipment
            update(shipment)
        } else {
            shipmentId.value = ""
            shipmentStatus.value = "Shipment was not found"
            shipmentTotes.value = emptyList()
            expectedShipmentDeliveryDate.value = ""
            shipmentUpdateHistory.value = emptyList()
        }

    }

    fun stopTracking(id: String) {
        val shipment = trackedShipments[id] ?: return

        shipment.removeObserver(this)
        trackedShipments.remove(id)

        // Optional: if this is the currently shown shipment, clear UI state
        if (shipmentId.value == id) {
            shipmentId.value = ""
            shipmentStatus.value = ""
            shipmentTotes.value = emptyList()
            expectedShipmentDeliveryDate.value = ""
            shipmentUpdateHistory.value = emptyList()
        }
    }


}