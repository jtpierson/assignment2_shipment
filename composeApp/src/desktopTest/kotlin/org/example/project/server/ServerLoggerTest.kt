package org.example.project.server

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import org.example.project.data.ShippingUpdate
import org.example.project.data.Shipment
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ServerLoggerTest : FunSpec({

    fun captureLoggerOutput(shipment: Shipment): String {
        val originalOut = System.out
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))

        ServerLogger.update(shipment)

        System.setOut(originalOut)
        return outputStream.toString()
    }

    fun shipmentWithViolation(): Shipment {
        return object : Shipment("s123", "express", "created", 111L, 111L, "Denver") {
            override fun checkForViolations() {
                setViolation("Test violation")
            }
        }.apply {
            checkForViolations()
        }
    }

    fun shipmentWithNoteOnly(): Shipment {
        return object : Shipment("s124", "bulk", "created", 111L, 111L, "Phoenix") {
            override fun checkForViolations() {}
        }.apply {
            addNote("Fragile")
        }
    }

    fun shipmentWithNeither(): Shipment {
        return object : Shipment("s125", "overnight", "created", 111L, 111L, "Austin") {
            override fun checkForViolations() {}
        }
    }

    test("ServerLogger logs both violations and notes") {
        val shipment = shipmentWithViolation().apply {
            addNote("Handle with care")
        }

        val output = captureLoggerOutput(shipment)

        output shouldContain "Violations:"
        output shouldContain "- Test violation"
        output shouldContain "Notes:"
        output shouldContain "- Handle with care"
    }

    test("ServerLogger logs only notes when no violations") {
        val shipment = shipmentWithNoteOnly()
        val output = captureLoggerOutput(shipment)

        output shouldContain "Notes:"
        output shouldContain "- Fragile"
        output shouldNotContain "Violations:"
    }

    test("ServerLogger logs neither violations nor notes") {
        val shipment = shipmentWithNeither()
        val output = captureLoggerOutput(shipment)

        output shouldNotContain "Violations:"
        output shouldNotContain "Notes:"
    }
})
