package org.example.project.data

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class ExpressShipmentTest : FunSpec({

    val createdAt = 1650000000000L
    val threeDaysMillis = 3 * 24 * 60 * 60 * 1000L

    test("should have no violations when expectedDeliveryDate is null") {
        val shipment = ExpressShipment(
            id = "express1",
            type = "express",
            expectedDeliveryDateTimestamp = null,
            currentLocation = "New York",
            createdAtTimestamp = createdAt
        )

        shipment.checkForViolations()

        shipment.violations.shouldBeEmpty()
    }

    test("should have no violations when delivery is exactly at 3 days") {
        val shipment = ExpressShipment(
            id = "express2",
            type = "express",
            expectedDeliveryDateTimestamp = createdAt + threeDaysMillis,
            currentLocation = "Chicago",
            createdAtTimestamp = createdAt
        )

        shipment.checkForViolations()

        shipment.violations.shouldBeEmpty()
    }

    test("should have no violations when delivery is within 3 days") {
        val shipment = ExpressShipment(
            id = "express3",
            type = "express",
            expectedDeliveryDateTimestamp = createdAt + threeDaysMillis - 1,
            currentLocation = "Dallas",
            createdAtTimestamp = createdAt
        )

        shipment.checkForViolations()

        shipment.violations.shouldBeEmpty()
    }

    test("should have a violation when delivery is after 3 days") {
        val shipment = ExpressShipment(
            id = "express4",
            type = "express",
            expectedDeliveryDateTimestamp = createdAt + threeDaysMillis + 1,
            currentLocation = "Denver",
            createdAtTimestamp = createdAt
        )

        shipment.checkForViolations()

        shipment.violations.shouldHaveSize(1)
        shipment.violations.first() shouldBe
                "Expected delivery date is more than 3 days after creation for an ExpressShipment."
    }
})