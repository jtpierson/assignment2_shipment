package org.example.project.update

import org.example.project.data.Shipment

interface UpdateStrategy {
    fun apply(shipment : Shipment?, data : List<String>): Shipment?
}