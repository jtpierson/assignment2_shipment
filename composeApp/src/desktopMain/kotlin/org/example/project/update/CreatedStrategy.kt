package org.example.project.update

import org.example.project.data.Shipment
import org.example.project.factory.ShipmentCreatorSelector

class CreatedStrategy : UpdateStrategy{
    override fun apply(shipment: Shipment?, data: List<String>): Shipment? {
        if (data.size < 6) return null

        val id = data[1]
        val createdAt = data[2].toLongOrNull() ?: return null
        val expectedDelivery = data[3].toLongOrNull() ?: return null
        val location = data[4]
        val type = data[5].lowercase()

        val creator = ShipmentCreatorSelector.getCreator(type) ?: return null
        return creator.create(
            id = id,
            expected = expectedDelivery,
            location = location,
            createdAt = createdAt
        )
    }
}