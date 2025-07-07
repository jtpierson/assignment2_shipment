package org.example.project.update

interface UpdateStrategy {
    fun apply(shipment : Shipment?, data : List<String>): Shipment?
}