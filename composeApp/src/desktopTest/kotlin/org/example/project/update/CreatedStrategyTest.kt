package org.example.project.update
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class CreatedStrategyTest : FunSpec({

    // *** valid data creates new shipment
    test("CreatedStrategy should return new Shipment with correct values") {
        val result = CreatedStrategy().apply(null, listOf("created", "X1", "123456789"))

        result!!.id shouldBe "X1"
        result.status shouldBe "created"
        result.expectedDeliveryDateTimestamp shouldBe 123456789L
        result.currentLocation shouldBe "Origin"
    }

    // *** too few fields
    test("CreatedStrategy should return null on too-short input") {
        val result = CreatedStrategy().apply(null, listOf("created", "X2"))
        result shouldBe null
    }

    // *** bad timestamp
    test("CreatedStrategy should return null on invalid timestamp") {
        val result = CreatedStrategy().apply(null, listOf("created", "X3", "badTimestamp"))
        result shouldBe null
    }
})