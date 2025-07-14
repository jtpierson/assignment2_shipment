package org.example.project.data

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ShippingUpdateTest : FunSpec({

    // *** property access checks
    test("ShippingUpdate should store and expose its properties correctly") {
        val update = ShippingUpdate(
            previousStatus = "created",
            newStatus = "shipped",
            timestamp = 123456789L
        )

        update.previousStatus shouldBe "created"
        update.newStatus shouldBe "shipped"
        update.timestamp shouldBe 123456789L
    }
})