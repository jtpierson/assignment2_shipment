package org.example.project.update

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.project.factory.ShipmentCreatorSelector

class CreatedStrategyTest : FunSpec({

    // Register a simple creator (e.g., Express) for test use
    beforeTest {
        ShipmentCreatorSelector.getCreator("express")
    }

    // *** valid data creates new shipment
    test("CreatedStrategy should return new Shipment with correct values") {
        val result = CreatedStrategy().apply(null, listOf("created", "s10000", "express", "123456789"))

        result!!.id shouldBe "s10000"
        result.status shouldBe "created"
        result.expectedDeliveryDateTimestamp shouldBe null
        result.currentLocation shouldBe "Unknown"
    }

    // *** too few fields
    test("CreatedStrategy should return null on too-short input") {
        val result = CreatedStrategy().apply(null, listOf("created", "s10001"))
        result shouldBe null
    }

    // *** bad timestamp
    test("CreatedStrategy should return null on invalid timestamp") {
        val result = CreatedStrategy().apply(null, listOf("created", "s10002", "express", "badTimestamp"))
        result shouldBe null
    }

    // *** unrecognized type
    test("CreatedStrategy should return null if type is unrecognized") {
        val result = CreatedStrategy().apply(null, listOf("created", "s10003", "unknown", "123456789"))
        result shouldBe null
    }
})
