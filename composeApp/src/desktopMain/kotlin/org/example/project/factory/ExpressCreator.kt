package org.example.project.factory

import org.example.project.data.ExpressShipment
import org.example.project.data.Shipment

class ExpressCreator : ShipmentCreator{
    override fun create(
        id: String,
        type: String,
        expected: Long,
        location: String,
        createdAt: Long
    ): Shipment {
        return ExpressShipment(id = id,type = type, expectedDeliveryDateTimestamp = expected, currentLocation = location, createdAtTimestamp = createdAt)
    }
}