package org.example.project.server

import org.example.project.data.Shipment
import org.example.project.factory.ShipmentCreatorSelector
import org.example.project.update.UpdateStrategySelector

object TrackingServer {
    private val shipments = mutableMapOf<String, Shipment>()

    fun getShipment(id: String): Shipment? = shipments[id]

    fun handleUpdate(update: String): Shipment? {
        val parts = update.split(",").map { it.trim() }

        val keyword = parts.getOrNull(0) ?: return null
        val id = parts.getOrNull(1) ?: return null

        val strategy = UpdateStrategySelector.getStrategy(keyword) ?: return null
        val updated = strategy.apply(shipments[id], parts)

        // 🔑 If createdStrategy produced a new shipment, we store it
        if (keyword == "created" && updated != null) {
            updated.registerObserver(ServerLogger())
            shipments[id] = updated
        } else if (updated != null) {
            shipments[id] = updated
        }

        return updated
    }
}