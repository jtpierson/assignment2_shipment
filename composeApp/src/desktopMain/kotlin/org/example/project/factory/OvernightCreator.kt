package org.example.project.factory

import org.example.project.data.OvernightShipment
import org.example.project.data.Shipment

class OvernightCreator : ShipmentCreator {
    override fun create(
        id: String,
        expected: Long,
        location: String,
        createdAt: Long
    ): Shipment {
        return OvernightShipment(id = id, expectedDeliveryDateTimestamp = expected, currentLocation = location, createdAtTimestamp = createdAt)
    }
}