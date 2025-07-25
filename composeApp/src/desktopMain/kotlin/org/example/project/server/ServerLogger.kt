package org.example.project.server

import org.example.project.data.Shipment
import org.example.project.observer.Observer

class ServerLogger : Observer<Shipment> {

    override fun update(subject: Shipment) {
        println(
            buildString {
                append("🔔 ServerLogger: Shipment '${subject.id}' - ${subject.type} was updated.\n")
                append("   → Status: ${subject.status}\n")
                append("   → Location: ${subject.currentLocation}\n")
                append("   → Expected Delivery: ${subject.expectedDeliveryDateTimestamp}\n")
                if (subject.violations.isNotEmpty()) {
                    append("   ⚠ Violations:\n")
                    subject.violations.forEach { append("     - $it\n") }
                }
                if (subject.notes.isNotEmpty()) {
                    append("   📝 Notes:\n")
                    subject.notes.forEach { append("     - $it\n") }
                }
                append("   📜 Update history count: ${subject.updateHistory.size}")
            }
        )
    }
}