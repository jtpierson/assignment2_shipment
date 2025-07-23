package org.example.project.server

import org.example.project.data.Shipment
import org.example.project.factory.ShipmentCreatorSelector
import org.example.project.update.UpdateStrategySelector

object TrackingServer {
    private val shipments = mutableMapOf<String, Shipment>()

    fun getShipment(id: String): Shipment? = shipments[id]

    fun createShipment(
        id: String,
        type: String,
        expected: Long,
        location: String,
        createdAt: Long
    ): Shipment? {
        val creator = ShipmentCreatorSelector.getCreator(type) ?: return null
        val shipment = creator.create(id, expected, location, createdAt)

        // ✅ Attach server-side observer
        shipment.registerObserver(ServerLogger())

        shipments[id] = shipment
        return shipment
    }

    fun handleUpdate(update: String): Shipment? {
        val parts = update.split(",").map { it.trim() }

        val keyword = parts.getOrNull(0) ?: return null
        val id = parts.getOrNull(1) ?: return null

        val shipment = shipments[id]

        val strategy = UpdateStrategySelector.getStrategy(keyword) ?: return null
        val updated = strategy.apply(shipment, parts)
        updated?.let { shipments[it.id] = it }
        return updated
    }
}