package org.example.project.factory

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.instanceOf
import org.example.project.data.StandardShipment

class StandardCreatorTest : FunSpec({

    // *** StandardCreator creates StandardShipment with correct values
    test("StandardCreator should create a StandardShipment with correct data") {
        val creator = StandardCreator()

        val shipment = creator.create(
            id = "s123",
            type = "standard",
            expected = 987654321L,
            location = "Salt Lake City",
            createdAt = 123456789L
        )

        shipment shouldBe instanceOf<StandardShipment>()
        shipment.id shouldBe "s123"
        shipment.type shouldBe "standard"
        shipment.expectedDeliveryDateTimestamp shouldBe 987654321L
        shipment.currentLocation shouldBe "Salt Lake City"
        shipment.createdAtTimestamp shouldBe 123456789L
    }

    // *** Accepts null expected delivery
    test("StandardCreator should handle null expected delivery timestamp") {
        val creator = StandardCreator()

        val shipment = creator.create(
            id = "s456",
            type = "standard",
            expected = null,
            location = "Denver",
            createdAt = 111111111L
        )

        shipment.expectedDeliveryDateTimestamp shouldBe null
    }
})