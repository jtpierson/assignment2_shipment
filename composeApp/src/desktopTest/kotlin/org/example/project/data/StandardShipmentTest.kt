package org.example.project.data

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class StandardShipmentTest : FunSpec({

    val createdAt = 1650000000000L

    test("StandardShipment has no violations by default") {
        val shipment = StandardShipment(
            id = "s1",
            type = "standard",
            expectedDeliveryDateTimestamp = createdAt + 5 * 24 * 60 * 60 * 1000L,
            currentLocation = "Dallas",
            createdAtTimestamp = createdAt
        )

        shipment.checkForViolations()
        shipment.violations.shouldBeEmpty()
    }

    test("StandardShipment clears existing violations") {
        val shipment = StandardShipment(
            id = "s2",
            type = "standard",
            expectedDeliveryDateTimestamp = createdAt + 2 * 24 * 60 * 60 * 1000L,
            currentLocation = "Miami",
            createdAtTimestamp = createdAt
        )

        shipment.setViolation("Fake violation for testing")
        shipment.violations.shouldHaveSize(1)

        shipment.checkForViolations()
        shipment.violations.shouldBeEmpty()
    }
})