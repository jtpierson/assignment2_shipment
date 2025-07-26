package org.example.project.server

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class TrackingServerTest : FunSpec({

    beforeTest {
        // Clear internal state between tests
        val shipmentsField = TrackingServer::class.java.getDeclaredField("shipments")
        shipmentsField.isAccessible = true
        val map = shipmentsField.get(TrackingServer) as MutableMap<*, *>
        map.clear()
    }

    // *** created shipment gets stored and returned
    test("handleUpdate should store and return new shipment on 'created'") {
        val update = "created,s10000,standard,123456789"
        val result = TrackingServer.handleUpdate(update)

        result!!.id shouldBe "s10000"
        TrackingServer.getShipment("s10000") shouldBe result
    }

    // *** update existing shipment (shipped)
    test("handleUpdate should update shipment if already created") {
        TrackingServer.handleUpdate("created,s10001,express,123456789")
        val result = TrackingServer.handleUpdate("shipped,s10001,123456789,123999999")

        result!!.status shouldBe "shipped"
        result.expectedDeliveryDateTimestamp shouldBe 123999999L
    }

    // *** return null on unknown strategy
    test("handleUpdate should return null on unknown keyword") {
        val result = TrackingServer.handleUpdate("unknown,s10002,123456789")
        result shouldBe null
    }

    // *** return null if no id provided
    test("handleUpdate should return null when ID is missing") {
        val result = TrackingServer.handleUpdate("created")
        result shouldBe null
    }

    // *** return null if strategy returns null
    test("handleUpdate should return null when strategy returns null") {
        val result = TrackingServer.handleUpdate("created,s10003,express,badTimestamp")
        result shouldBe null
    }

    // *** getShipment returns null if not found
    test("getShipment should return null for unknown id") {
        val result = TrackingServer.getShipment("missing")
        result shouldBe null
    }

    // *** handleUpdate returns null when update string has no keyword
    test("handleUpdate should return null when update string is empty or only whitespace") {
        val result = TrackingServer.handleUpdate("    ") // splits to [""] → "" gets trimmed to "" → getOrNull(0) = ""
        result shouldBe null
    }

    test("handleUpdate should return null if split list is empty") {
        // This requires bypassing the expected input format — use reflection or test double if necessary,
        // but for now simulate with a completely malformed string that triggers keyword null

        val result = TrackingServer.handleUpdate("") // "" split by "," = [""] → getOrNull(0) = ""
        result shouldBe null
    }
})