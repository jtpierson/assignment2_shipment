package org.example.project.update

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.project.data.Shipment

class CanceledStrategyTest : FunSpec({

    fun createShipment(
        id: String = "s-cancel",
        type: String = "standard",
        status: String = "created",
        expected: Long = 0L,
        createdAt: Long = 0L,
        location: String = "Anywhere"
    ): Shipment = FakeShipment(id, type, status, expected, createdAt, location)

    // *** apply marks shipment as canceled
    test("CanceledStrategy should mark shipment as canceled") {
        val shipment = createShipment()
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
        val shipment = createShipment()
        val result = CanceledStrategy().apply(shipment, listOf("canceled", shipment.id))
        result shouldBe null
    }

    test("CanceledStrategy should return null when timestamp is invalid") {
        val shipment = createShipment()
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

// Local dummy subclass to enable testing abstract Shipment
private class FakeShipment(
    id: String,
    type: String,
    status: String,
    expected: Long,
    createdAt: Long,
    location: String
) : Shipment(id, type, status, expected, createdAt, location) {
    override fun checkForViolations() {
        // no-op for unit test
    }
}