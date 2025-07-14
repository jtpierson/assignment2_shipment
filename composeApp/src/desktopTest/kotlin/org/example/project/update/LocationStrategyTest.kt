package org.example.project.update

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.project.data.Shipment

class LocationStrategyTest : FunSpec({

    // *** apply sets shipment status to relocated and updates location
    test("LocationStrategy should update status to relocated and change location") {
        val shipment = Shipment("S4", "shipped", 0L, "Chicago")
        val result = LocationStrategy().apply(
            shipment,
            listOf("location", "S4", "123456789", "Dallas")
        )

        result shouldBe shipment
        shipment.status shouldBe "relocated"
        shipment.currentLocation shouldBe "Dallas"
    }

    // *** apply returns null when shipment is null and data is valid
    test("LocationStrategy should return null when shipment is null and data is valid length") {
        val result = LocationStrategy().apply(
            null,
            listOf("location", "S4", "123456789", "Dallas")
        )
        result shouldBe null
    }

    // *** apply returns null when data list is too short and shipment is valid
    test("LocationStrategy should return null when data list is too short and shipment is valid") {
        val shipment = Shipment("S5", "shipped", 0L, "Seattle")
        val result = LocationStrategy().apply(
            shipment,
            listOf("location", "S5", "123456789") // missing location
        )
        result shouldBe null
    }

    // *** apply returns null when shipment is null and data is too short
    test("LocationStrategy should return null when shipment is null and data is too short") {
        val result = LocationStrategy().apply(
            null,
            listOf("location", "S6") // missing timestamp and location
        )
        result shouldBe null
    }

    // *** apply returns null when timestamp is not a number
    test("LocationStrategy should return null when timestamp is invalid") {
        val shipment = Shipment("S7", "shipped", 0L, "Austin")
        val result = LocationStrategy().apply(
            shipment,
            listOf("location", "S7", "not-a-number", "Houston")
        )
        result shouldBe null
    }
})