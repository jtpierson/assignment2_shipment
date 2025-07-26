package org.example.project.factory

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.instanceOf

class ShipmentCreatorSelectorTest : FunSpec({

    // *** valid creator types return correct class
    test("getCreator returns StandardCreator for 'standard'") {
        val creator = ShipmentCreatorSelector.getCreator("standard")
        creator shouldBe instanceOf<StandardCreator>()
    }

    test("getCreator returns ExpressCreator for 'express'") {
        val creator = ShipmentCreatorSelector.getCreator("express")
        creator shouldBe instanceOf<ExpressCreator>()
    }

    test("getCreator returns OvernightCreator for 'overnight'") {
        val creator = ShipmentCreatorSelector.getCreator("overnight")
        creator shouldBe instanceOf<OvernightCreator>()
    }

    test("getCreator returns BulkCreator for 'bulk'") {
        val creator = ShipmentCreatorSelector.getCreator("bulk")
        creator shouldBe instanceOf<BulkCreator>()
    }

    // *** case insensitivity check
    test("getCreator is case-insensitive") {
        val creator = ShipmentCreatorSelector.getCreator("ExPrEsS")
        creator shouldBe instanceOf<ExpressCreator>()
    }

    // *** invalid type returns null
    test("getCreator returns null for unrecognized type") {
        val creator = ShipmentCreatorSelector.getCreator("unknown")
        creator shouldBe null
    }

    test("getCreator returns null for empty string") {
        val creator = ShipmentCreatorSelector.getCreator("")
        creator shouldBe null
    }
})