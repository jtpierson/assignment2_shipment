package org.example.project.update

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.project.data.Shipment

class CanceledStrategyTest : FunSpec({

    // *** apply marks shipment as canceled
    test("CanceledStrategy should mark shipment as canceled") {
        val shipment = Shipment("S1", "created", 0L, "Tampa")
        val result = CanceledStrategy().apply(
            shipment,
            listOf("canceled", "S1", "123456789")
        )

        result shouldBe shipment
        shipment.status shouldBe "canceled"
        shipment.updateHistory.last().newStatus shouldBe "canceled"
        shipment.updateHistory.last().timestamp shouldBe 123456789L
    }

    // *** apply returns null when shipment is null and data is valid
    test("CanceledStrategy should return null when shipment is null and data is valid") {
        val result = CanceledStrategy().apply(null, listOf("canceled", "S2", "123456789"))
        result shouldBe null
    }

    // *** apply returns null when data is too short and shipment is present
    test("CanceledStrategy should return null when data is too short") {
        val shipment = Shipment("S3", "created", 0L, "Austin")
        val result = CanceledStrategy().apply(shipment, listOf("canceled", "S3"))
        result shouldBe null
    }

    // *** apply returns null when timestamp is not a number
    test("CanceledStrategy should return null when timestamp is invalid") {
        val shipment = Shipment("S4", "created", 0L, "Chicago")
        val result = CanceledStrategy().apply(
            shipment,
            listOf("canceled", "S4", "not-a-timestamp")
        )
        result shouldBe null
    }

    // *** apply returns null when both shipment is null and data is too short
    test("CanceledStrategy should return null when shipment is null and data is too short") {
        val result = CanceledStrategy().apply(null, listOf("canceled", "S5"))
        result shouldBe null
    }

    // *** apply returns null when data is valid length but shipment is null and timestamp is invalid
    test("CanceledStrategy should return null when shipment is null and timestamp is invalid") {
        val result = CanceledStrategy().apply(null, listOf("canceled", "S6", "invalid"))
        result shouldBe null
    }
})