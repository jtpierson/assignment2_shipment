package org.example.project.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object TrackingClient {
    private var client: HttpClient = defaultClient()
    private fun defaultClient() = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
    private var baseUrl: String = "http://localhost:8080" // DEFAULT



    suspend fun getShipment(id: String): ShipmentDto {
        println("? Requesting shipment $id")
        return client.get("$baseUrl/shipment/$id").body()
    }



    // Used ONLY IN TESTING to override client + base URL
    fun setClient(testClient: HttpClient, testBaseUrl: String = "") {
        client = testClient
        baseUrl = testBaseUrl
    }
}