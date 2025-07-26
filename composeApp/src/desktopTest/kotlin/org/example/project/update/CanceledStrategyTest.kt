package org.example.project.update

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.project.data.StandardShipment

class CanceledStrategyTest : FunSpec({

    fun makeShipment(
        id: String = "s1",
        type: String = "standard",
        expected: Long? = null,
        location: String = "Anywhere",
        createdAt: Long = 0L
    ) = StandardShipment(id, type, expected, location, createdAt)

    test("CanceledStrategy should mark shipment as canceled") {
        val shipment = makeShipment()
        val result = CanceledStrategy().apply(
            shipment,
            listOf("canceled", shipment.id, "123456789")
        )

        result shouldBe shipment
        shipment.status shouldBe "canceled"
        shipment.updateHistory.last().newStatus shouldBe "canceled"
        shipment.updateHistory.last().timestamp shouldBe 123456789L
    }

    test("CanceledStrategy should return null when shipment is null and data is valid") {
        val result = CanceledStrategy().apply(null, listOf("canceled", "s10001", "123456789"))
        result shouldBe null
    }

    test("CanceledStrategy should return null when data is too short") {
        val shipment = makeShipment()
        val result = CanceledStrategy().apply(shipment, listOf("canceled", shipment.id))
        result shouldBe null
    }

    test("CanceledStrategy should return null when timestamp is invalid") {
        val shipment = makeShipment()
        val result = CanceledStrategy().apply(
            shipment,
            listOf("canceled", shipment.id, "not-a-timestamp")
        )
        result shouldBe null
    }

    test("CanceledStrategy should return null when shipment is null and data is too short") {
        val result = CanceledStrategy().apply(null, listOf("canceled", "s10004"))
        result shouldBe null
    }

    test("CanceledStrategy should return null when shipment is null and timestamp is invalid") {
        val result = CanceledStrategy().apply(null, listOf("canceled", "s10005", "invalid"))
        result shouldBe null
    }
})