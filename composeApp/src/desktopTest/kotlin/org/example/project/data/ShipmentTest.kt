package org.example.project.data

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.example.project.observer.Observer

class ShipmentTest : FunSpec({

    fun createShipment(
        id: String = "S-test",
        type: String = "test-type",
        status: String = "created",
        expected: Long = 1000L,
        createdAt: Long = 500L,
        location: String = "Testville"
    ): Shipment {
        return FakeShipment(id, type, status, expected, createdAt, location)
    }

    // *** addNote checks
    test("addNote should add to notes list and notify observers") {
        val shipment = createShipment()
        val observer = TestObserver()
        shipment.registerObserver(observer)

        shipment.addNote("Package is fragile")

        shipment.notes shouldContain "Package is fragile"
        observer.wasNotified shouldBe true
    }

    test("addNote should preserve order of notes") {
        val shipment = createShipment()
        shipment.addNote("First note")
        shipment.addNote("Second note")

        shipment.notes shouldBe listOf("First note", "Second note")
    }

    // *** markShipped checks
    test("markShipped should update status and expected delivery") {
        val shipment = createShipment()
        shipment.markShipped(expectedDelivery = 123456789L, timestamp = 1111L)

        shipment.status shouldBe "shipped"
        shipment.expectedDeliveryDateTimestamp shouldBe 123456789L
        shipment.updateHistory shouldHaveSize 1
        shipment.updateHistory.last().newStatus shouldBe "shipped"
    }

    test("Observers should be notified on markShipped") {
        val shipment = createShipment()
        val observer = TestObserver()
        shipment.registerObserver(observer)

        shipment.markShipped(expectedDelivery = 777777L, timestamp = 8888L)

        observer.wasNotified shouldBe true
    }

    // *** markRelocated checks
    test("markRelocated should update location and add update entry") {
        val shipment = createShipment()
        shipment.markRelocated("Dallas", timestamp = 2222L)

        shipment.currentLocation shouldBe "Dallas"
        shipment.status shouldBe "relocated"
        shipment.updateHistory.last().newStatus shouldBe "relocated"
    }

    // *** markDelivered checks
    test("markDelivered should update status and add update entry") {
        val shipment = createShipment()
        shipment.markDelivered(timestamp = 3333L)

        shipment.status shouldBe "delivered"
        shipment.updateHistory.last().newStatus shouldBe "delivered"
        shipment.updateHistory.last().timestamp shouldBe 3333L
    }

    // *** markDelayed checks
    test("markDelayed should update status, expected delivery, and add update") {
        val shipment = createShipment()
        shipment.markDelayed(newExpectedDelivery = 999999L, timestamp = 4444L)

        shipment.status shouldBe "late"
        shipment.expectedDeliveryDateTimestamp shouldBe 999999L
        shipment.updateHistory.last().newStatus shouldBe "late"
    }

    // *** markLost checks
    test("markLost should update status and add update") {
        val shipment = createShipment()
        shipment.markLost(timestamp = 5555L)

        shipment.status shouldBe "lost"
        shipment.updateHistory.last().newStatus shouldBe "lost"
    }

    // *** markCanceled checks
    test("markCanceled should update status and add update entry") {
        val shipment = createShipment()
        shipment.markCanceled(timestamp = 6666L)

        shipment.status shouldBe "canceled"
        shipment.updateHistory shouldHaveSize 1
        shipment.updateHistory.last().newStatus shouldBe "canceled"
        shipment.updateHistory.last().timestamp shouldBe 6666L
    }

    // *** Observer management
    test("registerObserver and notifyObservers should trigger update") {
        val shipment = createShipment()
        val observer = TestObserver()
        shipment.registerObserver(observer)

        shipment.addNote("Temp sensitive")

        observer.wasNotified shouldBe true
    }

    test("removeObserver should prevent notifications") {
        val shipment = createShipment()
        val observer = TestObserver()
        shipment.registerObserver(observer)
        shipment.removeObserver(observer)

        shipment.addNote("Do not notify")

        observer.wasNotified shouldBe false
    }

    test("All registered observers should be notified") {
        val shipment = createShipment()
        val observer1 = TestObserver()
        val observer2 = TestObserver()
        shipment.registerObserver(observer1)
        shipment.registerObserver(observer2)

        shipment.addNote("Important update")

        observer1.wasNotified shouldBe true
        observer2.wasNotified shouldBe true
    }

    // *** setViolation should overwrite and notify
    test("setViolation should add message and notify observers") {
        val shipment = createShipment()
        val observer = TestObserver()
        shipment.registerObserver(observer)

        shipment.setViolation("Invalid date")

        shipment.violations shouldBe listOf("Invalid date")
        observer.wasNotified shouldBe true
    }

    // *** clearViolations should empty the list and notify
    test("clearViolations should remove all violations and notify observers") {
        val shipment = createShipment()
        shipment.setViolation("Old violation")

        val observer = TestObserver()
        shipment.registerObserver(observer)

        shipment.clearViolations()

        shipment.violations shouldBe emptyList()
        observer.wasNotified shouldBe true
    }

})

// Dummy test subclass of Shipment (since Shipment is abstract)
private class FakeShipment(
    id: String,
    type: String,
    status: String,
    expected: Long,
    createdAt: Long,
    location: String
) : Shipment(id, type, status, expected, createdAt, location) {
    override fun checkForViolations() {
        // No-op for testing
    }
}

// Simple observer stub
private class TestObserver : Observer<Shipment> {
    var wasNotified = false
    override fun update(subject: Shipment) {
        wasNotified = true
    }
}
