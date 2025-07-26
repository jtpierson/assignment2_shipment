package org.example.project.update

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.example.project.data.StandardShipment

class NoteAddedStrategyTest : FunSpec({

    fun makeShipment(
        id: String,
        type: String = "standard",
        expected: Long? = null,
        location: String = "Phoenix",
        createdAt: Long = 1000L
    ) = StandardShipment(id, type, expected, location, createdAt)

    // *** apply adds note to shipment when valid
    test("NoteAddedStrategy should add note to shipment when data is valid") {
        val shipment = makeShipment("s10000")
        val result = NoteAddedStrategy().apply(
            shipment,
            listOf("noteadded", "s10000", "123456789", "Handle with care")
        )

        result shouldBe shipment
        shipment.notes shouldContain "Handle with care"
    }

    // *** apply returns null when shipment is null and data is valid
    test("NoteAddedStrategy should return null when shipment is null and data is valid") {
        val result = NoteAddedStrategy().apply(
            null,
            listOf("noteadded", "s10001", "123456789", "Important")
        )
        result shouldBe null
    }

    // *** apply returns null when data list is too short and shipment is present
    test("NoteAddedStrategy should return null when data list is too short and shipment is present") {
        val shipment = makeShipment("s10002", location = "Austin")
        val result = NoteAddedStrategy().apply(
            shipment,
            listOf("noteadded", "s10002", "123456789") // missing note
        )
        result shouldBe null
    }

    // *** apply returns null when shipment is null and data is too short
    test("NoteAddedStrategy should return null when shipment is null and data is too short") {
        val result = NoteAddedStrategy().apply(
            null,
            listOf("noteadded", "s10003") // missing timestamp and note
        )
        result shouldBe null
    }
})
