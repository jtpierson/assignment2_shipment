package org.example.project.update

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.project.data.StandardShipment

class DelayedStrategyTest : FunSpec({

    fun makeShipment(
        id: String = "s10000",
        type: String = "standard",
        expected: Long? = null,
        location: String = "TestCity",
        createdAt: Long = 1000L
    ) = StandardShipment(id, type, expected, location, createdAt)

    test("DelayedStrategy should mark shipment as late and update expected delivery") {
        val shipment = makeShipment()
        val result = DelayedStrategy().apply(
            shipment,
            listOf("delayed", shipment.id, "123456789", "777777777")
        )

        result shouldBe shipment
        shipment.status shouldBe "late"
        shipment.expectedDeliveryDateTimestamp shouldBe 777777777L
    }

    test("DelayedStrategy should return null when shipment is null and data is valid length") {
        val result = DelayedStrategy().apply(
            null,
            listOf("delayed", "s10001", "123456789", "888888888")
        )
        result shouldBe null
    }

    test("DelayedStrategy should return null when data list is too short and shipment is valid") {
        val shipment = makeShipment("s10002")
        val result = DelayedStrategy().apply(
            shipment,
            listOf("delayed", "s10002", "123456789")
        )
        result shouldBe null
    }

    test("DelayedStrategy should return null when shipment is null and data is too short") {
        val result = DelayedStrategy().apply(
            null,
            listOf("delayed", "s10003")
        )
        result shouldBe null
    }

    test("DelayedStrategy should return null when timestamp is not a number") {
        val shipment = makeShipment("s10004")
        val result = DelayedStrategy().apply(
            shipment,
            listOf("delayed", "s10004", "not-a-timestamp", "777777777")
        )
        result shouldBe null
    }

    test("DelayedStrategy should return null when expected delivery is not a number") {
        val shipment = makeShipment("s10005")
        val result = DelayedStrategy().apply(
            shipment,
            listOf("delayed", "s10005", "123456789", "not-a-number")
        )
        result shouldBe null
    }
})