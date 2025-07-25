package org.example.project.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object TrackingClient {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun sendUpdate(update: String) {
        client.post("http://localhost:8080/update") {
            contentType(ContentType.Text.Plain)
            setBody(update)
        }
    }

    suspend fun getShipment(id: String): ShipmentDto {
        println("? Requesting shipment $id")
        return client.get("http://localhost:8080/shipment/$id").body()
    }
}