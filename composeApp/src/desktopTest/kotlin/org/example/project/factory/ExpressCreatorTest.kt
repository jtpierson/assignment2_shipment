package org.example.project.factory

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.instanceOf
import org.example.project.data.ExpressShipment

class ExpressCreatorTest : FunSpec({

    // *** ExpressCreator creates ExpressShipment with correct values
    test("ExpressCreator should create an ExpressShipment with correct data") {
        val creator = ExpressCreator()

        val shipment = creator.create(
            id = "s456",
            type = "express",
            expected = 888888888L,
            location = "Los Angeles",
            createdAt = 444444444L
        )

        shipment shouldBe instanceOf<ExpressShipment>()
        shipment.id shouldBe "s456"
        shipment.type shouldBe "express"
        shipment.expectedDeliveryDateTimestamp shouldBe 888888888L
        shipment.currentLocation shouldBe "Los Angeles"
        shipment.createdAtTimestamp shouldBe 444444444L
    }

    // *** Accepts null expected delivery
    test("ExpressCreator should handle null expected delivery timestamp") {
        val creator = ExpressCreator()

        val shipment = creator.create(
            id = "s457",
            type = "express",
            expected = null,
            location = "San Jose",
            createdAt = 444444445L
        )

        shipment.expectedDeliveryDateTimestamp shouldBe null
    }
})
