package org.example.project.data

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class OvernightShipmentTest : FunSpec({

    val createdAt = 1650000000000L
    val oneDayMillis = 24 * 60 * 60 * 1000L
    val tolerance = 10 * 1000L

    test("No violations when expectedDelivery is exactly 24 hours after creation") {
        val shipment = OvernightShipment(
            id = "s1",
            type = "overnight",
            expectedDeliveryDateTimestamp = createdAt + oneDayMillis,
            currentLocation = "Denver",
            createdAtTimestamp = createdAt
        )

        shipment.checkForViolations()
        shipment.violations.shouldBeEmpty()
    }

    test("No violations when within tolerance window") {
        val shipment = OvernightShipment(
            id = "s2",
            type = "overnight",
            expectedDeliveryDateTimestamp = createdAt + oneDayMillis + tolerance - 1,
            currentLocation = "New York",
            createdAtTimestamp = createdAt
        )

        shipment.checkForViolations()
        shipment.violations.shouldBeEmpty()
    }

    test("Violation when delivery is too early (beyond tolerance)") {
        val shipment = OvernightShipment(
            id = "s3",
            type = "overnight",
            expectedDeliveryDateTimestamp = createdAt + oneDayMillis - tolerance - 1000,
            currentLocation = "Los Angeles",
            createdAtTimestamp = createdAt
        )

        shipment.checkForViolations()
        shipment.violations.shouldHaveSize(1)
        shipment.violations.first() shouldBe
                "Expected delivery for OvernightShipment must be exactly 1 day after creation."
    }

    test("Violation when delivery is too late (beyond tolerance)") {
        val shipment = OvernightShipment(
            id = "s4",
            type = "overnight",
            expectedDeliveryDateTimestamp = createdAt + oneDayMillis + tolerance + 1000,
            currentLocation = "Chicago",
            createdAtTimestamp = createdAt
        )

        shipment.checkForViolations()
        shipment.violations.shouldHaveSize(1)
        shipment.violations.first() shouldBe
                "Expected delivery for OvernightShipment must be exactly 1 day after creation."
    }

    test("No violation if status is 'late' and outside tolerance") {
        val shipment = OvernightShipment(
            id = "s5",
            type = "overnight",
            expectedDeliveryDateTimestamp = createdAt, // temporary placeholder
            currentLocation = "Boston",
            createdAtTimestamp = createdAt
        )

        val delayedDelivery = createdAt + oneDayMillis + tolerance + 5000
        shipment.markDelayed(delayedDelivery, timestamp = createdAt + 5000)

        shipment.violations.shouldBeEmpty()
    }

    test("No violation if expectedDeliveryDate is null") {
        val shipment = OvernightShipment(
            id = "s6",
            type = "overnight",
            expectedDeliveryDateTimestamp = null,
            currentLocation = "Seattle",
            createdAtTimestamp = createdAt
        )

        shipment.checkForViolations()
        shipment.violations.shouldBeEmpty()
    }
})