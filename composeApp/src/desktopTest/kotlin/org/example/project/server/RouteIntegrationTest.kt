package org.example.project.server

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import org.example.project.network.ShipmentDto

class RouteIntegrationTest : FunSpec({

    test("POST /update should return 200 and register shipment") {
        testApplication {
            application {
                configureServerRoutes()
            }

            val response = client.post("/update") {
                setBody("created,s99999,standard,1650000000000")
            }

            response.status shouldBe HttpStatusCode.OK

            // Check that the shipment is registered
            val shipment = TrackingServer.getShipment("s99999")
            shipment?.id shouldBe "s99999"
            shipment?.type shouldBe "standard"
        }
    }

    test("GET /shipment/{id} should return shipment data if found") {
        testApplication {
            application {
                configureServerRoutes()
            }

            // Setup - create a shipment first
            TrackingServer.handleUpdate("created,s10001,express,1650000000000")

            val response = client.get("/shipment/s10001")

            response.status shouldBe HttpStatusCode.OK

            val json = response.bodyAsText()
            val dto = Json.decodeFromString<ShipmentDto>(json)

            dto.id shouldBe "s10001"
            dto.type shouldBe "express"
        }
    }

    test("GET /shipment/{id} should return 404 if shipment does not exist") {
        testApplication {
            application {
                configureServerRoutes()
            }

            val response = client.get("/shipment/doesNotExist")

            response.status shouldBe HttpStatusCode.NotFound
        }
    }
})