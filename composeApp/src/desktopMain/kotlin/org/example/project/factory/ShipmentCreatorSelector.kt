package org.example.project.factory

object ShipmentCreatorSelector {

    private val creators: Map<String, ShipmentCreator> = mapOf(
        "standard" to StandardCreator(),
        "express" to ExpressCreator(),
        "overnight" to OvernightCreator(),
        "bulk" to BulkCreator()
    )

    fun getCreator(type: String): ShipmentCreator? {
        return creators[type.lowercase()]
    }
}