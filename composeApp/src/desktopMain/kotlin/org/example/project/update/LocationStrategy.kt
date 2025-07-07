package org.example.project.update

import org.example.project.data.Shipment

class LocationStrategy : UpdateStrategy {
    override fun apply(shipment: Shipment?, data: List<String>): Shipment? {
        if (shipment == null || data.size < 4) return null

        val timestamp = data[2].toLongOrNull() ?: return null
        val newLocation = data[3]

        shipment.markRelocated(newLocation, timestamp)

        return shipment
    }

}