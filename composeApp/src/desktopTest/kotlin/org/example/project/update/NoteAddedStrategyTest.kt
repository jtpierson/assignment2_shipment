package org.example.project.update

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.example.project.data.Shipment

class NoteAddedStrategyTest : FunSpec({

    // *** apply adds note to shipment when valid
    test("NoteAddedStrategy should add note to shipment when data is valid") {
        val shipment = Shipment("S1", "created", 0L, "Phoenix")
        val result = NoteAddedStrategy().apply(
            shipment,
            listOf("noteadded", "S1", "123456789", "Handle with care")
        )

        result shouldBe shipment
        shipment.notes shouldContain "Handle with care"
    }

    // *** apply returns null when shipment is null and data is valid
    test("NoteAddedStrategy should return null when shipment is null and data is valid") {
        val result = NoteAddedStrategy().apply(
            null,
            listOf("noteadded", "S2", "123456789", "Important")
        )
        result shouldBe null
    }

    // *** apply returns null when data list is too short and shipment is present
    test("NoteAddedStrategy should return null when data list is too short and shipment is present") {
        val shipment = Shipment("S3", "created", 0L, "Austin")
        val result = NoteAddedStrategy().apply(
            shipment,
            listOf("noteadded", "S3", "123456789") // missing note
        )
        result shouldBe null
    }

    // *** apply returns null when shipment is null and data is too short
    test("NoteAddedStrategy should return null when shipment is null and data is too short") {
        val result = NoteAddedStrategy().apply(
            null,
            listOf("noteadded", "S4") // missing timestamp and note
        )
        result shouldBe null
    }
})
