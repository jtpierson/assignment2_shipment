package org.example.project.tracking

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import kotlinx.coroutines.runBlocking
import org.example.project.network.TrackingClient
import org.example.project.server.TrackingServer
import org.example.project.server.configureServerRoutes
import org.example.project.util.formatTimestamp

class TrackerViewHelperTest : FunSpec({

    // 🛠️ Helper to create valid TrackedShipment objects
    fun makeTracked(id: String, type: String = "standard") = TrackedShipment(
        id = id,
        type = type,
        status = mutableStateOf("created"),
        location = mutableStateOf("Unknown"),
        expectedDelivery = mutableStateOf("--"),
        notes = mutableStateListOf(),
        updates = mutableStateListOf()
    )

    test("trackShipment adds a tracked shipment and updates UI model") {
        testApplication {
            application {
                configureServerRoutes()
            }

            TrackingClient.setClient(createClient {
                install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                    json()
                }
            })

            TrackingServer.handleUpdate("created,s99999,standard,1650000000000")

            val helper = TrackerViewHelper()

            runBlocking {
                helper.trackShipment("s99999")
            }

            val tracked = helper.trackedShipments.firstOrNull { it.id == "s99999" }
            tracked?.type shouldBe "standard"
            tracked?.status?.value shouldBe "created"
            tracked?.location?.value shouldBe "Unknown"
            tracked?.expectedDelivery?.value shouldBe formatTimestamp(null)
        }
    }

    test("trackShipment doesn't add duplicate tracked shipments") {
        testApplication {
            application {
                configureServerRoutes()
            }

            TrackingClient.setClient(createClient {
                install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                    json()
                }
            })

            TrackingServer.handleUpdate("created,s11111,standard,1650000000000")

            val helper = TrackerViewHelper()

            runBlocking {
                helper.trackShipment("s11111")  // First → adds
                helper.trackShipment("s11111")  // Second → should early return
            }

            helper.trackedShipments.count { it.id == "s11111" } shouldBe 1
        }
    }

    test("trackShipment sets trackingError on failure") {
        val helper = TrackerViewHelper()

        runBlocking {
            helper.trackShipment("nonexistent-id")
        }

        helper.trackingError.value shouldBe "Shipment 'nonexistent-id' not found."
    }

    test("stopTracking removes shipment from tracked list") {
        val helper = TrackerViewHelper()
        helper.trackedShipments.add(makeTracked("s123"))

        helper.trackedShipments.size shouldBe 1

        helper.stopTracking("s123")

        helper.trackedShipments.size shouldBe 0
    }

    test("stopTracking does nothing if shipment is not tracked") {
        val helper = TrackerViewHelper()

        helper.stopTracking("notTracked")

        helper.trackedShipments.size shouldBe 0
    }

    test("stopTracking only removes the matching shipment") {
        val helper = TrackerViewHelper()
        helper.trackedShipments.addAll(
            listOf(
                makeTracked("s1"),
                makeTracked("s2"),
                makeTracked("s3"),
            )
        )

        helper.stopTracking("s2")

        helper.trackedShipments.map { it.id } shouldContainExactly listOf("s1", "s3")
    }
})