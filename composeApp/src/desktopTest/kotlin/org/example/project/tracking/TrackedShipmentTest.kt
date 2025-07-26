package org.example.project.tracking

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class TrackedShipmentTest : FunSpec({

    // *** TrackedShipment should store values correctly
    test("TrackedShipment should store initial values") {
        val shipment = TrackedShipment(
            id = "s10000",
            type = "test",
            status = mutableStateOf("shipped"),
            location = mutableStateOf("Chicago"),
            expectedDelivery = mutableStateOf("2025-07-15 12:00:00"),
            notes = SnapshotStateList<String>().apply { add("Fragile") },
            updates = SnapshotStateList<String>().apply { add("Shipment created") }
        )

        shipment.id shouldBe "s10000"
        shipment.type shouldBe "test"
        shipment.status.value shouldBe "shipped"
        shipment.location.value shouldBe "Chicago"
        shipment.expectedDelivery.value shouldBe "2025-07-15 12:00:00"
        shipment.notes[0] shouldBe "Fragile"
        shipment.updates[0] shouldBe "Shipment created"
    }
})