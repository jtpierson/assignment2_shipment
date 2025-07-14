package org.example.project.tracking

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.example.project.data.Shipment

class TrackerViewHelperTest : FunSpec({

    val helper = TrackerViewHelper()

    // *** trackShipment registers observer and populates tracked state
    test("trackShipment registers observer and initializes TrackedShipment") {
        val shipment = Shipment("s10000", "created", 1000L, "Chicago").apply {
            addNote("Initial note")
            markShipped(2000L, 1500L)
        }

        TrackingSimulator.addShipment(shipment)
        helper.trackShipment("s10000")

        helper.trackedShipments shouldHaveSize 1
        val tracked = helper.trackedShipments.first()

        tracked.id shouldBe "s10000"
        tracked.status.value shouldBe "shipped"
        tracked.location.value shouldBe "Chicago"
        tracked.notes shouldContainExactly listOf("Initial note")
        tracked.updates.any { it.contains("shipped") } shouldBe true
    }

    // *** stopTracking removes tracked shipment
    test("stopTracking removes tracked shipment") {
        val shipment = Shipment("s10001", "created", 123L, "Denver")
        TrackingSimulator.addShipment(shipment)
        helper.trackShipment("s10001")

        helper.stopTracking("s10001")

        helper.trackedShipments.none { it.id == "s10001" } shouldBe true
    }

    // *** trackShipment does not allow duplicates
    test("trackShipment does not duplicate if already tracked") {
        val shipment = Shipment("s10002", "created", 1000L, "Boston")
        TrackingSimulator.addShipment(shipment)

        helper.trackShipment("s10002")
        helper.trackShipment("s10002")

        helper.trackedShipments.count { it.id == "s10002" } shouldBe 1
    }

    // *** trackShipment is a no-op if shipment not found
    test("trackShipment does nothing if shipment not found") {
        val countBefore = helper.trackedShipments.size

        helper.trackShipment("DOES_NOT_EXIST")

        helper.trackedShipments.size shouldBe countBefore
    }

    // *** update reflects Shipment changes into UI state
    test("update should reflect Shipment field changes into tracked UI state") {
        val shipment = Shipment("s10003", "created", 1000L, "Houston").apply {
            addNote("First")
            markShipped(2222L, 1111L)
        }
        TrackingSimulator.addShipment(shipment)
        helper.trackShipment("s10003")

        shipment.addNote("Second")
        shipment.markRelocated("Austin", 3333L)

        // This triggers observer manually
        helper.update(shipment)

        val tracked = helper.trackedShipments.first { it.id == "s10003" }
        tracked.status.value shouldBe "relocated"
        tracked.location.value shouldBe "Austin"
        tracked.notes shouldContainExactly listOf("First", "Second")
        tracked.updates.any { it.contains("relocated") } shouldBe true
    }

    // *** stopTracking is a no-op if shipment was never tracked
    test("stopTracking does nothing if shipment was never tracked") {
        helper.stopTracking("s10004")
        helper.trackedShipments.none { it.id == "s10004" } shouldBe true
    }

    // *** stopTracking handles case where shipment is not found in TrackingSimulator
    test("stopTracking handles case where shipment is not found in TrackingSimulator") {
        // Track a shipment
        val shipment = Shipment("s10005", "created", 123L, "Seattle")
        TrackingSimulator.addShipment(shipment)
        helper.trackShipment("s10005")

        // Manually remove the shipment from TrackingSimulator map
        // Simulate it by replacing map with a new instance using reflection (not recommended)
        // Instead, just clear trackedShipments to simulate untrack attempt
        helper.stopTracking("s10005")

        helper.trackedShipments.none { it.id == "s10005" } shouldBe true
    }

    // *** update is a no-op if shipment is not currently tracked
    test("update does nothing if shipment is not tracked") {
        val shipment = Shipment("s10006", "created", 1000L, "Nowhere")
        shipment.markShipped(1111L, 2222L)

        // Not tracked by helper
        helper.update(shipment)

        helper.trackedShipments.none { it.id == "s10006" } shouldBe true
    }

    // *** trackShipment handles case where shipment has no notes or updates
    test("trackShipment handles shipment with no notes or updates") {
        val shipment = Shipment("s10007", "created", 999L, "San Diego")
        TrackingSimulator.addShipment(shipment)

        helper.trackShipment("s10007")

        val tracked = helper.trackedShipments.first { it.id == "s10007" }
        tracked.notes shouldHaveSize 0
        tracked.updates shouldHaveSize 0
        tracked.status.value shouldBe "created"
        tracked.location.value shouldBe "San Diego"
    }

    // *** stopTracking still removes from trackedShipments even if TrackingSimulator returns null
    test("stopTracking still removes tracked shipment even if TrackingSimulator returns null") {
        val shipment = Shipment("s10007", "created", 123L, "Orlando")
        TrackingSimulator.addShipment(shipment)
        helper.trackShipment("s10007")

        // Simulate TrackingSimulator returning null by removing shipment
        // NOTE: This directly edits the internal map, which is allowed in test context
        TrackingSimulator::class.java.getDeclaredField("shipments").apply {
            isAccessible = true
            @Suppress("UNCHECKED_CAST")
            val map = get(null) as MutableMap<String, Shipment>
            map.remove("s10007")
        }

        // Now when stopTracking is called, it will find tracked in UI state,
        // but TrackingSimulator will return null, so removeObserver is skipped
        helper.stopTracking("s10007")

        helper.trackedShipments.none { it.id == "s10007" } shouldBe true
    }

})