package org.example.project.update

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.project.data.Shipment

class DelayedStrategyTest : FunSpec({

    // *** apply sets shipment status to late and updates expected delivery
    test("DelayedStrategy should mark shipment as late and update expected delivery") {
        val shipment = Shipment("S7", "shipped", 0L, "New York")
        val result = DelayedStrategy().apply(
            shipment,
            listOf("delayed", "S7", "123456789", "777777777")
        )

        result shouldBe shipment
        shipment.status shouldBe "late"
        shipment.expectedDeliveryDateTimestamp shouldBe 777777777L
    }

    // *** apply returns null when shipment is null and data size >= 4
    test("DelayedStrategy should return null when shipment is null and data is valid length") {
        val result = DelayedStrategy().apply(
            null,
            listOf("delayed", "S8", "123456789", "888888888")
        )
        result shouldBe null
    }

    // *** apply returns null when shipment is not null and data is too short
    test("DelayedStrategy should return null when data list is too short and shipment is valid") {
        val shipment = Shipment("S9", "shipped", 0L, "Boston")
        val result = DelayedStrategy().apply(
            shipment,
            listOf("delayed", "S9", "123456789") // missing expected delivery
        )
        result shouldBe null
    }

    // *** apply returns null when shipment is null and data is too short
    test("DelayedStrategy should return null when shipment is null and data is too short") {
        val result = DelayedStrategy().apply(
            null,
            listOf("delayed", "S10") // size < 4
        )
        result shouldBe null
    }

    // *** apply returns null when timestamp is invalid
    test("DelayedStrategy should return null when timestamp is not a number") {
        val shipment = Shipment("S11", "shipped", 0L, "Chicago")
        val result = DelayedStrategy().apply(
            shipment,
            listOf("delayed", "S11", "not-a-timestamp", "777777777")
        )
        result shouldBe null
    }

    // *** apply returns null when expected delivery is invalid
    test("DelayedStrategy should return null when expected delivery is not a number") {
        val shipment = Shipment("S12", "shipped", 0L, "Austin")
        val result = DelayedStrategy().apply(
            shipment,
            listOf("delayed", "S12", "123456789", "not-a-number")
        )
        result shouldBe null
    }
})