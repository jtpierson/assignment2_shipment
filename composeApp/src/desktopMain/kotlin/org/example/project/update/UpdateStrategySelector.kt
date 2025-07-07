package org.example.project.update

class UpdateStrategySelector {

    private val strategies : Map<String, UpdateStrategy> = mapOf(
        "created" to CreatedStrategy(),
        "shipped" to ShippedStrategy(),
        "location" to LocationStrategy(),
        "delivered" to DeliveredStrategy(),
        "delayed" to DelayedStrategy(),
        "lost" to LostStrategy(),
        "canceled" to CanceledStrategy(),
        "noteadded" to NoteAddedStrategy(),
    )

    fun getStrategy(type : String): UpdateStrategy? {
        return strategies[type.lowercase()]
    }
}