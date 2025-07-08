package org.example.project.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.project.tracking.TrackedShipment
import org.example.project.tracking.TrackerViewHelper

@Composable
fun UserInterface(viewHelper: TrackerViewHelper) {
    var input by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {

        // Input field
        TextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Shipment ID") },
            modifier = Modifier.fillMaxWidth()
        )

        // Track button
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Button(onClick = { viewHelper.trackShipment(input) }) {
                Text("Track")
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // Display tracked shipments
        LazyColumn {
            items(viewHelper.trackedShipments, key = { it.id }) { shipment ->
                TrackedShipmentCard(
                    shipment = shipment,
                    onStopTracking = { viewHelper.stopTracking(shipment.id) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun TrackedShipmentCard(
    shipment: TrackedShipment,
    onStopTracking: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Tracking shipment: ${shipment.id}", style = MaterialTheme.typography.h6)
            Text("Status: ${shipment.status.value}")
            Text("Location: ${shipment.location.value}")
            Text("Expected Delivery: ${shipment.expectedDelivery.value}")

            Spacer(modifier = Modifier.height(8.dp))

            Text("Notes:")
            for (note in shipment.notes) {
                Text("- $note")
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text("Update History:")
            for (entry in shipment.updates) {
                Text("- $entry")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = onStopTracking) {
                Text("Stop Tracking")
            }
        }
    }
}