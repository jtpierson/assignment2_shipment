package org.example.project.factory

import org.example.project.data.Shipment
import org.example.project.data.StandardShipment

class StandardCreator : ShipmentCreator {
    override fun create(
        id: String,
        expected: Long,
        location: String,
        createdAt: Long
    ): Shipment {
        return StandardShipment(id = id, expectedDeliveryDateTimestamp = expected, currentLocation = location, createdAtTimestamp = createdAt)
    }
}