package org.example.project.data

class BulkShipment(
    id: String,
    type: String,
    expectedDeliveryDateTimestamp: Long?,
    currentLocation: String,
    createdAtTimestamp: Long
) : Shipment(
    id = id,
    type = type,
    status = "created",
    expectedDeliveryDateTimestamp = expectedDeliveryDateTimestamp,
    createdAtTimestamp = createdAtTimestamp,
    currentLocation = currentLocation
) {
    // Bulk Shipment's must be delivered after 3 days.
    override fun checkForViolations() {
        if (expectedDeliveryDateTimestamp == null) {
            clearViolations()
            return
        }

        val timeDiff = expectedDeliveryDateTimestamp!! - createdAtTimestamp
        val minRequiredMillis = 3 * 24 * 60 * 60 * 1000L // 3 days

        if (timeDiff < minRequiredMillis) {
            setViolation("Expected delivery date is sooner than 3 days after creation for a BulkShipment.")
        } else {
            clearViolations()
        }
    }
}