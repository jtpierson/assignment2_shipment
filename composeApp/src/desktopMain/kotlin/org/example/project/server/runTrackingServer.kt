package org.example.project.server

import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.application.Application
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.serialization.kotlinx.json.json
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import org.example.project.network.ShipmentDto
import org.example.project.util.formatTimestamp
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.http.content.defaultResource
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.http.content.staticResources
import org.example.project.network.ShipmentDtoConverter

fun main() {
    embeddedServer(Netty, port = 8080) {
        configureServerRoutes()
    }.start(wait = true)
}

private fun Application.configureServerRoutes() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    routing {

        staticResources("/", "static") {
            default("index.html")
        }
        post("/update") {
            val updateText = call.receiveText()
            TrackingServer.handleUpdate(updateText)
            call.respond(HttpStatusCode.OK)
        }

        get("/shipment/{id}") {
            val id = call.parameters["id"]
            val shipment = id?.let { TrackingServer.getShipment(it) }

            if (shipment == null) {
                call.respond(HttpStatusCode.NotFound)
                return@get // exits {} block not the whole function
            }

            call.respond(ShipmentDtoConverter.fromShipment(shipment))
        }
    }
}