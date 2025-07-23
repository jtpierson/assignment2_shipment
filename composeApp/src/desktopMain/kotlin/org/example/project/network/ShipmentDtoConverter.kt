package org.example.project.network

import org.example.project.data.Shipment
import org.example.project.util.formatTimestamp

// Converts Shipment to ShipmentDto
class ShipmentDtoConverter {
    companion object {
        fun fromShipment(shipment: Shipment): ShipmentDto {
            return ShipmentDto(
                id = shipment.id,
                status = shipment.status,
                location = shipment.currentLocation,
                expectedDelivery = formatTimestamp(shipment.expectedDeliveryDateTimestamp),
                notes = shipment.notes,
                updates = shipment.updateHistory.map {
                    "Shipment went from ${it.previousStatus} to ${it.newStatus} on ${formatTimestamp(it.timestamp)}"
                },
                violations = shipment.violations
            )
        }
    }
}