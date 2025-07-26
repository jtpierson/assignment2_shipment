package org.example.project.update

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.project.data.StandardShipment

class LocationStrategyTest : FunSpec({

    fun makeShipment(
        id: String = "s10000",
        type: String = "standard",
        expected: Long? = null,
        location: String = "Chicago",
        createdAt: Long = 1000L
    ) = StandardShipment(id, type, expected, location, createdAt)

    // *** apply sets shipment status to relocated and updates location
    test("LocationStrategy should update status to relocated and change location") {
        val shipment = makeShipment("s10000", location = "Chicago")
        val result = LocationStrategy().apply(
            shipment,
            listOf("location", "s10000", "123456789", "Dallas")
        )

        result shouldBe shipment
        shipment.status shouldBe "relocated"
        shipment.currentLocation shouldBe "Dallas"
    }

    // *** apply returns null when shipment is null and data is valid
    test("LocationStrategy should return null when shipment is null and data is valid length") {
        val result = LocationStrategy().apply(
            null,
            listOf("location", "s10001", "123456789", "Dallas")
        )
        result shouldBe null
    }

    // *** apply returns null when data list is too short and shipment is valid
    test("LocationStrategy should return null when data list is too short and shipment is valid") {
        val shipment = makeShipment("s10002", location = "Seattle")
        val result = LocationStrategy().apply(
            shipment,
            listOf("location", "s10002", "123456789") // missing location
        )
        result shouldBe null
    }

    // *** apply returns null when shipment is null and data is too short
    test("LocationStrategy should return null when shipment is null and data is too short") {
        val result = LocationStrategy().apply(
            null,
            listOf("location", "s10003") // missing timestamp and location
        )
        result shouldBe null
    }

    // *** apply returns null when timestamp is not a number
    test("LocationStrategy should return null when timestamp is invalid") {
        val shipment = makeShipment("s10004", location = "Austin")
        val result = LocationStrategy().apply(
            shipment,
            listOf("location", "s10004", "not-a-number", "Houston")
        )
        result shouldBe null
    }
})
