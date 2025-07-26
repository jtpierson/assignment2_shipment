package org.example.project.update

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.project.data.StandardShipment

class ShippedStrategyTest : FunSpec({

    fun makeShipment(
        id: String,
        type: String = "standard",
        expected: Long? = null,
        location: String = "Boston",
        createdAt: Long = 1000L
    ) = StandardShipment(id, type, expected, location, createdAt)

    // *** apply sets shipment status to shipped and updates expected delivery timestamp
    test("ShippedStrategy should update status to shipped and set expected delivery") {
        val shipment = makeShipment("s10000")
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
        val shipment = makeShipment("s10001", location = "Chicago")
        val result = ShippedStrategy().apply(
            shipment,
            listOf("shipped", "s10001", "123456789") // missing expected delivery
        )
        result shouldBe null
    }

    // *** apply returns null when expected delivery is not a valid Long
    test("ShippedStrategy should return null when expected delivery is not a number") {
        val shipment = makeShipment("s10002", location = "Dallas")
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
        val shipment = makeShipment("s10004", location = "Houston")
        val result = ShippedStrategy().apply(
            shipment,
            listOf("shipped", "s10004", "not-a-number", "999999999")
        )
        result shouldBe null
    }
})