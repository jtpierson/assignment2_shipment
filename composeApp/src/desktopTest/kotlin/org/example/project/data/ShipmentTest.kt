package org.example.project.data

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.example.project.observer.Observer

class ShipmentTest : FunSpec({

    test("addNote should add to notes list and notify observers") {
        val shipment = Shipment("S1", "created", 0L, "Boston")
        val observer = TestObserver()
        shipment.registerObserver(observer)

        shipment.addNote("Package is fragile")

        shipment.notes shouldContain "Package is fragile"
        observer.wasNotified shouldBe true
    }

    test("markShipped should update status and expected delivery") {
        val shipment = Shipment("S2", "created", 0L, "NY")
        shipment.markShipped(expectedDelivery = 123456789L, timestamp = 1111L)

        shipment.status shouldBe "shipped"
        shipment.expectedDeliveryDateTimestamp shouldBe 123456789L
        shipment.updateHistory shouldHaveSize 1
        shipment.updateHistory.last().newStatus shouldBe "shipped"
    }

    test("markRelocated should update location and add update entry") {
        val shipment = Shipment("S3", "shipped", 0L, "Chicago")
        shipment.markRelocated("Dallas", timestamp = 2222L)

        shipment.currentLocation shouldBe "Dallas"
        shipment.status shouldBe "relocated"
        shipment.updateHistory.last().newStatus shouldBe "relocated"
    }

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
})

private class TestObserver : Observer<Shipment> {
    var wasNotified = false
    override fun update(subject: Shipment) {
        wasNotified = true
    }
}