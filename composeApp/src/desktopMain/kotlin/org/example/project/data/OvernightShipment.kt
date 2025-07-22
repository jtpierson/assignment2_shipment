package org.example.project.data

class OvernightShipment(
    id: String,
    expectedDeliveryDateTimestamp: Long,
    currentLocation: String,
    createdAtTimestamp: Long
) : Shipment(
    id = id,
    status = "created",
    expectedDeliveryDateTimestamp = expectedDeliveryDateTimestamp,
    createdAtTimestamp = createdAtTimestamp,
    currentLocation = currentLocation
) {
    override fun checkForViolations() {
        val timeDiff = expectedDeliveryDateTimestamp - createdAtTimestamp
        val oneDayMillis = 24 * 60 * 60 * 1000L

        // Allow small deviation window (e.g. 10 seconds) for timestamp precision issues
        val tolerance = 10 * 1000L

        if (kotlin.math.abs(timeDiff - oneDayMillis) > tolerance && status != "late") {
            setViolation("Expected delivery for OvernightShipment must be exactly 1 day after creation.")
        } else {
            clearViolations()
        }
    }
}