package org.example.project.data

class OvernightShipment(
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
    override fun checkForViolations() {
        if (expectedDeliveryDateTimestamp == null) {
            clearViolations()
            return
        }
        val timeDiff = expectedDeliveryDateTimestamp!! - createdAtTimestamp
        val oneDayMillis = 24 * 60 * 60 * 1000L

        // Allow small deviation window
        val tolerance = 10 * 1000L

        if (kotlin.math.abs(timeDiff - oneDayMillis) > tolerance && status != "late") {
            setViolation("Expected delivery for OvernightShipment must be exactly 1 day after creation.")
        } else {
            clearViolations()
        }
    }
}