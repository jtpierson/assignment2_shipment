package org.example.project.update

import org.example.project.data.Shipment

class CreatedStrategy : UpdateStrategy{
    override fun apply(shipment: Shipment?, data: List<String>): Shipment? {
        if (data.size < 3) return null
        val id = data[1]
        val timestamp = data[2].toLongOrNull() ?: return null

        return Shipment(
            id = id,
            status = "created",
            expectedDeliveryDateTimestamp = timestamp,
            currentLocation = "Origin"
        )
    }
}