package org.example.project.tracking

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.example.project.data.Shipment
import java.io.File

class TrackingSimulatorTest : FunSpec({

    // Ensure a clean environment before each test
    beforeTest {
        clearShipments()
    }

    // *** addShipment and findShipment work correctly
    test("addShipment adds and findShipment retrieves the shipment") {
        val shipment = Shipment("s10000", "created", 123456L, "Boston")
        TrackingSimulator.addShipment(shipment)

        val found = TrackingSimulator.findShipment("s10000")
        found shouldBe shipment
    }

    // *** runSimulation processes created shipment line and adds it to the map
    test("runSimulation adds created shipment from file") {
        val tempFile = File.createTempFile("simulation", ".txt")
        tempFile.writeText("created,s10001,1652712855468")

        runBlocking {
            TrackingSimulator.runSimulation(tempFile)
        }

        val shipment = TrackingSimulator.findShipment("s10001")
        shipment?.id shouldBe "s10001"
        shipment?.status shouldBe "created"
        shipment?.expectedDeliveryDateTimestamp shouldBe 1652712855468L

        tempFile.delete()
    }

    // *** runSimulation skips malformed lines
    test("runSimulation skips malformed lines") {
        val tempFile = File.createTempFile("badlines", ".txt")
        tempFile.writeText(
            """
            created,s10002,1652712855468
            malformed,line
            shipped,s10002,1652712855468,1652812855468
            """.trimIndent()
        )

        runBlocking {
            TrackingSimulator.runSimulation(tempFile)
        }

        val shipment = TrackingSimulator.findShipment("s10002")
        shipment?.status shouldBe "shipped"
        tempFile.delete()
    }

    // *** runSimulation does not crash with unknown strategy
    test("runSimulation skips unknown strategy lines") {
        val tempFile = File.createTempFile("unknown", ".txt")
        tempFile.writeText("unknown,s10003,123456789")

        runBlocking {
            TrackingSimulator.runSimulation(tempFile)
        }

        val shipment = TrackingSimulator.findShipment("s10003")
        shipment shouldBe null

        tempFile.delete()
    }

    // *** created strategy returns null if timestamp is invalid → result == null
    test("runSimulation with invalid created timestamp does not add shipment") {
        val tempFile = File.createTempFile("badcreated", ".txt")
        tempFile.writeText("created,s10004,not_a_timestamp")

        runBlocking {
            TrackingSimulator.runSimulation(tempFile)
        }

        val shipment = TrackingSimulator.findShipment("s10004")
        shipment shouldBe null

        tempFile.delete()
    }

    // *** processLine skips if data has fewer than 2 items
    test("runSimulation skips lines with fewer than two fields") {
        val tempFile = File.createTempFile("toolittle", ".txt")
        tempFile.writeText("onlyonefield")

        runBlocking {
            TrackingSimulator.runSimulation(tempFile)
        }

        // No shipment ID could be formed, just ensure no crash and no shipment added
        TrackingSimulator.findShipment("onlyonefield") shouldBe null

        tempFile.delete()
    }
})

private fun clearShipments() {
    val shipmentsField = TrackingSimulator::class.java.getDeclaredField("shipments")
    shipmentsField.isAccessible = true
    val shipmentsMap = shipmentsField.get(null) as MutableMap<*, *>
    shipmentsMap.clear()
}
