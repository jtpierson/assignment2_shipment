package org.example.project.data

class StandardShipment(
    id: String,
    expectedDeliveryDateTimestamp: Long,
    currentLocation: String,
    createdAtTimestamp: Long
) : Shipment(
    id = id,
    status = "created", // All shipments start with being created
    expectedDeliveryDateTimestamp = expectedDeliveryDateTimestamp,
    createdAtTimestamp = createdAtTimestamp,
    currentLocation = currentLocation
) {
    // Standard Shipment has no special conditions so we just clear the violations
    override fun checkForViolations() {
        clearViolations()
    }
}