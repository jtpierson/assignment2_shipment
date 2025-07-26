import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import org.example.project.network.TrackingClient
import org.example.project.network.ShipmentDto
import org.example.project.server.TrackingServer
import org.example.project.server.configureServerRoutes

class TrackingClientTest : FunSpec({

    test("TrackingClient.getShipment() returns correct data for existing shipment") {
        testApplication {
            application {
                configureServerRoutes()
            }

            // Create shipment before calling TrackingClient
            TrackingServer.handleUpdate("created,s12345,express,1650000000000")

            // Patch the test client's JSON config so it matches the real one
            val testClient = createClient {
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    })
                }
            }

            // Inject patched client into TrackingClient
            TrackingClient.setClient(testClient, "")

            val dto: ShipmentDto = TrackingClient.getShipment("s12345")

            dto.id shouldBe "s12345"
            dto.type shouldBe "express"
        }
    }
})