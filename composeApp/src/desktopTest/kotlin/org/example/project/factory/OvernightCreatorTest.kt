package org.example.project.factory

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.instanceOf
import org.example.project.data.OvernightShipment

class OvernightCreatorTest : FunSpec({

    // *** OvernightCreator creates OvernightShipment with correct values
    test("OvernightCreator should create an OvernightShipment with correct data") {
        val creator = OvernightCreator()

        val shipment = creator.create(
            id = "s789",
            type = "overnight",
            expected = 999999999L,
            location = "Houston",
            createdAt = 555555555L
        )

        shipment shouldBe instanceOf<OvernightShipment>()
        shipment.id shouldBe "s789"
        shipment.type shouldBe "overnight"
        shipment.expectedDeliveryDateTimestamp shouldBe 999999999L
        shipment.currentLocation shouldBe "Houston"
        shipment.createdAtTimestamp shouldBe 555555555L
    }

    // *** Accepts null expected delivery
    test("OvernightCreator should handle null expected delivery timestamp") {
        val creator = OvernightCreator()

        val shipment = creator.create(
            id = "s900",
            type = "overnight",
            expected = null,
            location = "Miami",
            createdAt = 666666666L
        )

        shipment.expectedDeliveryDateTimestamp shouldBe null
    }
})
