package org.example.project.update

import org.example.project.data.Shipment

class ShippedStrategy : UpdateStrategy{
    override fun apply(shipment: Shipment?, data: List<String>): Shipment? {
        if (shipment == null || data.size < 4) return null

        val updatedTimestamp = data[2].toLongOrNull() ?: return null
        val expectedDelivery = data[3].toLongOrNull() ?: return null

        shipment.markShipped(expectedDelivery, updatedTimestamp)

        return shipment
    }

}