package org.example.project.data

class ExpressShipment(
    id: String,
    type: String,
    expectedDeliveryDateTimestamp: Long,
    currentLocation: String,
    createdAtTimestamp: Long
) : Shipment(
    id = id,
    type = type,
    status = "created", // All shipments start with being created
    expectedDeliveryDateTimestamp = expectedDeliveryDateTimestamp,
    createdAtTimestamp = createdAtTimestamp,
    currentLocation = currentLocation
) {
    // Express Shipment's must be delivered within 3 days.
    override fun checkForViolations() {
        val timeDiff = expectedDeliveryDateTimestamp - createdAtTimestamp
        val maxAllowedMillis = 3 * 24 * 60 * 60 * 1000L // 3 days in ms

        if (timeDiff > maxAllowedMillis) {
            setViolation("Expected delivery date is more than 3 days after creation for an ExpressShipment.")
        } else {
            clearViolations()
        }
    }
}