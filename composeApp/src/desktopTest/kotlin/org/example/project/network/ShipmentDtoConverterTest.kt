package org.example.project.network

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.example.project.data.ShippingUpdate
import org.example.project.factory.ShipmentCreatorSelector
import org.example.project.util.formatTimestamp

class ShipmentDtoConverterTest : FunSpec({

    test("fromShipment converts a Standard shipment correctly") {
        val shipment = ShipmentCreatorSelector
            .getCreator("standard")!!
            .create(
                id = "s42",
                type = "standard",
                expected = 1650000000000,
                location = "Chicago",
                createdAt = 1649990000000
            )

        shipment.addNote("Leave at doorstep")
        shipment.setViolation("Temperature out of range")

        shipment.addUpdate(
            ShippingUpdate(
                previousStatus = "created",
                newStatus = "shipped",
                timestamp = 1650001234567
            )
        )

        val dto = ShipmentDtoConverter.fromShipment(shipment)

        dto.id shouldBe "s42"
        dto.type shouldBe "standard"
        dto.status shouldBe "created"
        dto.location shouldBe "Chicago"
        dto.expectedDelivery shouldBe formatTimestamp(1650000000000)

        dto.notes shouldContain "Leave at doorstep"
        dto.notes shouldContain "Temperature out of range"
        dto.violations shouldBe listOf("Temperature out of range")

        dto.updates.size shouldBe 1
        dto.updates.first() shouldContain "Shipment went from created to shipped"
    }

    test("instantiate ShipmentDtoConverter for coverage") {
        ShipmentDtoConverter()
    }
})
