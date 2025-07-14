package org.example.project.update

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.project.data.Shipment

class LostStrategyTest : FunSpec({

    // *** apply marks shipment as lost
    test("LostStrategy should mark shipment as lost") {
        val shipment = Shipment("S1", "relocated", 0L, "San Diego")
        val result = LostStrategy().apply(
            shipment,
            listOf("lost", "S1", "123456789")
        )

        result shouldBe shipment
        shipment.status shouldBe "lost"
        shipment.updateHistory.last().newStatus shouldBe "lost"
        shipment.updateHistory.last().timestamp shouldBe 123456789L
    }

    // *** apply returns null when shipment is null and data is valid
    test("LostStrategy should return null when shipment is null and data is valid") {
        val result = LostStrategy().apply(null, listOf("lost", "S2", "123456789"))
        result shouldBe null
    }

    // *** apply returns null when data is too short and shipment is present
    test("LostStrategy should return null when data is too short") {
        val shipment = Shipment("S3", "relocated", 0L, "Denver")
        val result = LostStrategy().apply(shipment, listOf("lost", "S3")) // missing timestamp
        result shouldBe null
    }

    // *** apply returns null when timestamp is invalid
    test("LostStrategy should return null when timestamp is not a number") {
        val shipment = Shipment("S4", "relocated", 0L, "Las Vegas")
        val result = LostStrategy().apply(
            shipment,
            listOf("lost", "S4", "not-a-timestamp")
        )
        result shouldBe null
    }

    // *** apply returns null when shipment is null and data is too short
    test("LostStrategy should return null when shipment is null and data is too short") {
        val result = LostStrategy().apply(null, listOf("lost", "S5"))
        result shouldBe null
    }

    // *** apply returns null when shipment is null and timestamp is invalid
    test("LostStrategy should return null when shipment is null and timestamp is invalid") {
        val result = LostStrategy().apply(null, listOf("lost", "S6", "invalid"))
        result shouldBe null
    }
})
