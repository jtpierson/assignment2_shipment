package org.example.project.factory

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.instanceOf
import org.example.project.data.BulkShipment

class BulkCreatorTest : FunSpec({

    // *** BulkCreator creates BulkShipment with correct data
    test("BulkCreator should create a BulkShipment with correct values") {
        val creator = BulkCreator()

        val shipment = creator.create(
            id = "s789",
            type = "bulk",
            expected = 999999999L,
            location = "Houston",
            createdAt = 777777777L
        )

        shipment shouldBe instanceOf<BulkShipment>()
        shipment.id shouldBe "s789"
        shipment.type shouldBe "bulk"
        shipment.expectedDeliveryDateTimestamp shouldBe 999999999L
        shipment.currentLocation shouldBe "Houston"
        shipment.createdAtTimestamp shouldBe 777777777L
    }

    // *** Accepts null expected delivery
    test("BulkCreator should handle null expected delivery timestamp") {
        val creator = BulkCreator()

        val shipment = creator.create(
            id = "s790",
            type = "bulk",
            expected = null,
            location = "Tampa",
            createdAt = 777777778L
        )

        shipment.expectedDeliveryDateTimestamp shouldBe null
    }
})