package org.example.project.update

import org.example.project.data.Shipment

class LostStrategy : UpdateStrategy {
    override fun apply(shipment: Shipment?, data: List<String>): Shipment? {
        if (shipment == null || data.size < 3) return null

        val timestamp = data[2].toLongOrNull() ?: return null

        shipment.markLost(timestamp)

        return shipment
    }
}