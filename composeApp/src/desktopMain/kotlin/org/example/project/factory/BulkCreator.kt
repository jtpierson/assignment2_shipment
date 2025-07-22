package org.example.project.factory

import org.example.project.data.BulkShipment
import org.example.project.data.Shipment

class BulkCreator : ShipmentCreator {
    override fun create(
        id: String,
        expected: Long,
        location: String,
        createdAt: Long
    ): Shipment {
        return BulkShipment(id = id, expectedDeliveryDateTimestamp = expected, currentLocation = location, createdAtTimestamp = createdAt)
    }
}