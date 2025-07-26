package org.example.project.update

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.project.data.StandardShipment

class DeliveredStrategyTest : FunSpec({

    fun makeShipment(
        id: String = "s10000",
        type: String = "standard",
        expected: Long? = null,
        location: String = "TestCity",
        createdAt: Long = 1000L
    ) = StandardShipment(id, type, expected, location, createdAt)

    // *** apply marks shipment as delivered
    test("DeliveredStrategy should update status to delivered") {
        val shipment = makeShipment("s10000")
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
        val shipment = makeShipment("s10002")
        val result = DeliveredStrategy().apply(
            shipment,
            listOf("delivered", "s10002") // missing timestamp
        )
        result shouldBe null
    }

    // *** apply returns null when timestamp is not a number
    test("DeliveredStrategy should return null when timestamp is invalid") {
        val shipment = makeShipment("s10003")
        val result = DeliveredStrategy().apply(
            shipment,
            listOf("delivered", "s10003", "not-a-number")
        )
        result shouldBe null
    }
})