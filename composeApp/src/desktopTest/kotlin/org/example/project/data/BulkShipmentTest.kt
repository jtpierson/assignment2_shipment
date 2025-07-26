package org.example.project.data

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class BulkShipmentTest : FunSpec({

    test("BulkShipment with null expectedDeliveryDate should have no violations") {
        val shipment = BulkShipment(
            id = "b001",
            type = "bulk",
            expectedDeliveryDateTimestamp = null,
            currentLocation = "Warehouse A",
            createdAtTimestamp = 1650000000000
        )

        shipment.checkForViolations()

        shipment.violations.isEmpty() shouldBe true
    }

    test("BulkShipment with delivery more than 3 days later should have no violations") {
        val createdAt = 1650000000000
        val threeDaysAndOneMinuteLater = createdAt + (3 * 24 * 60 * 60 * 1000L) + 60_000L

        val shipment = BulkShipment(
            id = "b002",
            type = "bulk",
            expectedDeliveryDateTimestamp = threeDaysAndOneMinuteLater,
            currentLocation = "Warehouse B",
            createdAtTimestamp = createdAt
        )

        shipment.checkForViolations()

        shipment.violations.isEmpty() shouldBe true
    }

    test("BulkShipment with delivery less than 3 days later should have a violation") {
        val createdAt = 1650000000000
        val twoDaysLater = createdAt + (2 * 24 * 60 * 60 * 1000L)

        val shipment = BulkShipment(
            id = "b003",
            type = "bulk",
            expectedDeliveryDateTimestamp = twoDaysLater,
            currentLocation = "Warehouse C",
            createdAtTimestamp = createdAt
        )

        shipment.checkForViolations()

        shipment.violations.size shouldBe 1
        shipment.violations.first() shouldBe "Expected delivery date is sooner than 3 days after creation for a BulkShipment."
    }
})