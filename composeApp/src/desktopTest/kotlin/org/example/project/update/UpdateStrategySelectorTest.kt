package org.example.project.update

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.shouldBe

class UpdateStrategySelectorTest : FunSpec({

    // *** should return CreatedStrategy for type "created"
    test("getStrategy returns CreatedStrategy for 'created'") {
        val strategy = UpdateStrategySelector.getStrategy("created")
        strategy.shouldBeInstanceOf<CreatedStrategy>()
    }

    // *** should return ShippedStrategy for type "shipped"
    test("getStrategy returns ShippedStrategy for 'shipped'") {
        val strategy = UpdateStrategySelector.getStrategy("shipped")
        strategy.shouldBeInstanceOf<ShippedStrategy>()
    }

    // *** should return LocationStrategy for type "location"
    test("getStrategy returns LocationStrategy for 'location'") {
        val strategy = UpdateStrategySelector.getStrategy("location")
        strategy.shouldBeInstanceOf<LocationStrategy>()
    }

    // *** should return DeliveredStrategy for type "delivered"
    test("getStrategy returns DeliveredStrategy for 'delivered'") {
        val strategy = UpdateStrategySelector.getStrategy("delivered")
        strategy.shouldBeInstanceOf<DeliveredStrategy>()
    }

    // *** should return DelayedStrategy for type "delayed"
    test("getStrategy returns DelayedStrategy for 'delayed'") {
        val strategy = UpdateStrategySelector.getStrategy("delayed")
        strategy.shouldBeInstanceOf<DelayedStrategy>()
    }

    // *** should return LostStrategy for type "lost"
    test("getStrategy returns LostStrategy for 'lost'") {
        val strategy = UpdateStrategySelector.getStrategy("lost")
        strategy.shouldBeInstanceOf<LostStrategy>()
    }

    // *** should return CanceledStrategy for type "canceled"
    test("getStrategy returns CanceledStrategy for 'canceled'") {
        val strategy = UpdateStrategySelector.getStrategy("canceled")
        strategy.shouldBeInstanceOf<CanceledStrategy>()
    }

    // *** should return NoteAddedStrategy for type "noteadded"
    test("getStrategy returns NoteAddedStrategy for 'noteadded'") {
        val strategy = UpdateStrategySelector.getStrategy("noteadded")
        strategy.shouldBeInstanceOf<NoteAddedStrategy>()
    }

    // *** getStrategy should return null for unknown type
    test("getStrategy returns null for unknown type") {
        val strategy = UpdateStrategySelector.getStrategy("unknown")
        strategy shouldBe null
    }

    // *** getStrategy should be case-insensitive
    test("getStrategy should be case-insensitive") {
        val strategy = UpdateStrategySelector.getStrategy("CrEaTeD")
        strategy.shouldBeInstanceOf<CreatedStrategy>()
    }
})