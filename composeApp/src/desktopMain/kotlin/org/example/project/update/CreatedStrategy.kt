package org.example.project.update

import org.example.project.data.Shipment
import org.example.project.factory.ShipmentCreatorSelector

class CreatedStrategy : UpdateStrategy {
    override fun apply(shipment: Shipment?, data: List<String>): Shipment? {
        if (data.size < 4) return null

        val id = data[1]
        val type = data[2].lowercase()

        val createdAt = data[3].toLongOrNull() ?: return null
        val expectedDelivery: Long? = null
        val location = "Unknown"

        val creator = ShipmentCreatorSelector.getCreator(type) ?: return null
        return creator.create(id= id,type=type, expected = expectedDelivery, location = location, createdAt = createdAt)
    }
}