package org.example.project.factory

import org.example.project.data.Shipment

interface ShipmentCreator {
    fun create(id : String,type : String, expected : Long?, location : String, createdAt : Long) : Shipment
}