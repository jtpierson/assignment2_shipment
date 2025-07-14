package org.example.project.update

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.project.data.Shipment

class ShippedStrategyTest : FunSpec({

    // *** apply sets shipment status to shipped and updates expected delivery timestamp
    test("ShippedStrategy should update status to shipped and set expected delivery") {
        val shipment = Shipment("s10000", "created", 0L, "Boston")
        val result = ShippedStrategy().apply(
            shipment,
            listOf("shipped", "s10000", "123456789", "987654321")
        )

        result shouldBe shipment
        shipment.status shouldBe "shipped"
        shipment.expectedDeliveryDateTimestamp shouldBe 987654321L
    }

    // *** apply returns null when shipment is null and data is valid
    test("ShippedStrategy should return null when shipment is null and data is valid") {
        val result = ShippedStrategy().apply(null, listOf("shipped", "s10000", "123", "456"))
        result shouldBe null
    }

    // *** apply returns null when expected delivery is missing
    test("ShippedStrategy should return null when expected delivery is missing") {
        val shipment = Shipment("s10001", "created", 0L, "Chicago")
        val result = ShippedStrategy().apply(
            shipment,
            listOf("shipped", "s10001", "123456789") // missing expected delivery
        )
        result shouldBe null
    }

    // *** apply returns null when expected delivery is not a valid Long
    test("ShippedStrategy should return null when expected delivery is not a number") {
        val shipment = Shipment("s10002", "created", 0L, "Dallas")
        val result = ShippedStrategy().apply(
            shipment,
            listOf("shipped", "s10002", "123456789", "not-a-number")
        )
        result shouldBe null
    }

    // *** apply returns null when shipment is null and data is too short
    test("ShippedStrategy should return null when shipment is null and data is too short") {
        val result = ShippedStrategy().apply(null, listOf("shipped", "s10003"))
        result shouldBe null
    }

    // *** apply returns null when timestamp is not a number
    test("ShippedStrategy should return null when timestamp is not a number") {
        val shipment = Shipment("s10004", "created", 0L, "Houston")
        val result = ShippedStrategy().apply(
            shipment,
            listOf("shipped", "s10004", "not-a-number", "999999999")
        )
        result shouldBe null
    }
})
