package org.example.project.tracking

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

    suspend fun runSimulation(file: File) {
        for (line in file.readLines()) {
            processLine(line)
            println("Processed update: $line")
            delay(1000) // ✅ Now allowed: you're in a suspendable loop
        }
    }



    private fun processLine(line: String) {
        val data = line.split(",")
        if (data.size < 2) return

        val updateType = data[0].lowercase()
        val shipmentId = data[1]

        val strategy = UpdateStrategySelector.getStrategy(updateType)
        if (strategy == null) {
            println("No strategy for update type: $updateType")
            return
        }

        val shipment = findShipment(shipmentId)
        val result = strategy.apply(shipment, data)

        if (updateType == "created" && result != null) {
            addShipment(result)
        }
    }
}