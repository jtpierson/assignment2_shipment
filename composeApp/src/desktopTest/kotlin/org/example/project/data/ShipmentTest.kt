package org.example.project.data

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.example.project.observer.Observer

class ShipmentTest : FunSpec({

    // *** addNote checks
    test("addNote should add to notes list and notify observers") {
        val shipment = Shipment("S1", "created", 0L, "Boston")
        val observer = TestObserver()
        shipment.registerObserver(observer)

        shipment.addNote("Package is fragile")

        shipment.notes shouldContain "Package is fragile"
        observer.wasNotified shouldBe true
    }

    test("addNote should preserve order of notes") {
        val shipment = Shipment("S11", "created", 0L, "LA")
        shipment.addNote("First note")
        shipment.addNote("Second note")

        shipment.notes shouldBe listOf("First note", "Second note")
    }

    // *** markShipped checks
    test("markShipped should update status and expected delivery") {
        val shipment = Shipment("S2", "created", 0L, "NY")
        shipment.markShipped(expectedDelivery = 123456789L, timestamp = 1111L)

        shipment.status shouldBe "shipped"
        shipment.expectedDeliveryDateTimestamp shouldBe 123456789L
        shipment.updateHistory shouldHaveSize 1
        shipment.updateHistory.last().newStatus shouldBe "shipped"
    }

    test("Observers should be notified on markShipped") {
        val shipment = Shipment("S10", "created", 0L, "NYC")
        val observer = TestObserver()
        shipment.registerObserver(observer)

        shipment.markShipped(expectedDelivery = 777777L, timestamp = 8888L)

        observer.wasNotified shouldBe true
    }

    // *** markRelocated checks
    test("markRelocated should update location and add update entry") {
        val shipment = Shipment("S3", "shipped", 0L, "Chicago")
        shipment.markRelocated("Dallas", timestamp = 2222L)

        shipment.currentLocation shouldBe "Dallas"
        shipment.status shouldBe "relocated"
        shipment.updateHistory.last().newStatus shouldBe "relocated"
    }

    // *** markDelivered checks
    test("markDelivered should update status and add update entry") {
        val shipment = Shipment("S6", "shipped", 0L, "Atlanta")
        shipment.markDelivered(timestamp = 3333L)

        shipment.status shouldBe "delivered"
        shipment.updateHistory.last().newStatus shouldBe "delivered"
        shipment.updateHistory.last().timestamp shouldBe 3333L
    }

    // *** markDelayed checks
    test("markDelayed should update status, expected delivery, and add update") {
        val shipment = Shipment("S7", "shipped", 0L, "Austin")
        shipment.markDelayed(newExpectedDelivery = 999999L, timestamp = 4444L)

        shipment.status shouldBe "late"
        shipment.expectedDeliveryDateTimestamp shouldBe 999999L
        shipment.updateHistory.last().newStatus shouldBe "late"
    }

    // *** markLost checks
    test("markLost should update status and add update") {
        val shipment = Shipment("S8", "relocated", 0L, "Denver")
        shipment.markLost(timestamp = 5555L)

        shipment.status shouldBe "lost"
        shipment.updateHistory.last().newStatus shouldBe "lost"
    }

    // *** markCanceled checks
    test("markCanceled should update status and add update entry") {
        val shipment = Shipment("S9", "created", 0L, "San Francisco")
        shipment.markCanceled(timestamp = 6666L)

        shipment.status shouldBe "canceled"
        shipment.updateHistory shouldHaveSize 1
        shipment.updateHistory.last().newStatus shouldBe "canceled"
        shipment.updateHistory.last().timestamp shouldBe 6666L
    }

    // *** Observer management
    test("registerObserver and notifyObservers should trigger update") {
        val shipment = Shipment("S4", "created", 0L, "LA")
        val observer = TestObserver()
        shipment.registerObserver(observer)

        shipment.addNote("Temp sensitive")

        observer.wasNotified shouldBe true
    }

    test("removeObserver should prevent notifications") {
        val shipment = Shipment("S5", "created", 0L, "Seattle")
        val observer = TestObserver()
        shipment.registerObserver(observer)
        shipment.removeObserver(observer)

        shipment.addNote("Do not notify")

        observer.wasNotified shouldBe false
    }

    test("All registered observers should be notified") {
        val shipment = Shipment("S12", "created", 0L, "Miami")
        val observer1 = TestObserver()
        val observer2 = TestObserver()
        shipment.registerObserver(observer1)
        shipment.registerObserver(observer2)

        shipment.addNote("Important update")

        observer1.wasNotified shouldBe true
        observer2.wasNotified shouldBe true
    }
})

private class TestObserver : Observer<Shipment> {
    var wasNotified = false
    override fun update(subject: Shipment) {
        wasNotified = true
    }
}