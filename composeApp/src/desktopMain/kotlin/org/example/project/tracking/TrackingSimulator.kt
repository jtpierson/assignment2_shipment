package org.example.project.tracking

import kotlinx.coroutines.runBlocking
import org.example.project.data.Shipment
import org.example.project.update.UpdateStrategySelector
import java.io.File

object TrackingSimulator {
    private val shipments = mutableMapOf<String, Shipment>()

    fun findShipment(id : String): Shipment? {
        return shipments[id]
    }

    fun addShipment(shipment : Shipment) {
        shipments[shipment.id] = shipment
    }

    fun runSimulation(file : File) = runBlocking {
        file.forEachLine { line ->
            processLine(line)

        }
    }

    private fun processLine(line : String) {
        val data = line.split(",")
        if (data.size < 2) return

        val updateType = data[0].lowercase()
        val shipmentId = data[1]

        val strategy = UpdateStrategySelector.getStrategy(updateType)
        val shipment = findShipment(shipmentId)

        val result = strategy?.apply(shipment, data)

        if (updateType == "created" && result != null) {
            addShipment(result)
        }
    }
}