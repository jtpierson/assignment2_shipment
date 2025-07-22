package org.example.project.factory

import org.example.project.data.ExpressShipment
import org.example.project.data.Shipment

class ExpressCreator : ShipmentCreator{
    override fun create(
        id: String,
        expected: Long,
        location: String,
        createdAt: Long
    ): Shipment {
        return ExpressShipment(id = id, expectedDeliveryDateTimestamp = expected, currentLocation = location, createdAtTimestamp = createdAt)
    }
}