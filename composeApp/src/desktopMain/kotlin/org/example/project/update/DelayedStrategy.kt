package org.example.project.update

import org.example.project.data.Shipment

class DelayedStrategy : UpdateStrategy {
    override fun apply(shipment: Shipment?, data: List<String>): Shipment? {
        if (shipment == null || data.size < 4) return null

        val timestamp = data[2].toLongOrNull() ?: return null
        val newExpectedDelivery = data[3].toLongOrNull() ?: return null

        shipment.markDelayed(newExpectedDelivery, timestamp)

        return shipment
    }
}