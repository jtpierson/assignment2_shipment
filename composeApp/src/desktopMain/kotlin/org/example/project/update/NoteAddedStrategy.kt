package org.example.project.update

import org.example.project.data.Shipment

class NoteAddedStrategy : UpdateStrategy {
    override fun apply(shipment: Shipment?, data: List<String>): Shipment? {
        if (shipment == null || data.size < 4) return null
        val note = data[3]
        shipment.addNote(note)
        return shipment

        }

}