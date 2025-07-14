package org.example.project.update

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.project.data.Shipment

class DeliveredStrategyTest : FunSpec({

    // *** apply marks shipment as delivered
    test("DeliveredStrategy should update status to delivered") {
        val shipment = Shipment("s10000", "shipped", 0L, "Chicago")
        val result = DeliveredStrategy().apply(
            shipment,
            listOf("delivered", "s10000", "123456789")
        )

        result shouldBe shipment
        shipment.status shouldBe "delivered"
    }

    // *** apply returns null when shipment is null
    test("DeliveredStrategy should return null when shipment is null") {
        val result = DeliveredStrategy().apply(null, listOf("delivered", "s10001", "123456789"))
        result shouldBe null
    }

    // *** apply returns null when data is too short
    test("DeliveredStrategy should return null when data list is too short") {
        val shipment = Shipment("s10002", "shipped", 0L, "Boston")
        val result = DeliveredStrategy().apply(
            shipment,
            listOf("delivered", "s10002") // missing timestamp
        )
        result shouldBe null
    }

    // *** apply returns null when timestamp is not a number
    test("DeliveredStrategy should return null when timestamp is invalid") {
        val shipment = Shipment("s10003", "shipped", 0L, "Austin")
        val result = DeliveredStrategy().apply(
            shipment,
            listOf("delivered", "s10003", "not-a-number")
        )
        result shouldBe null
    }
})