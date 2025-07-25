package org.example.project.tracking

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList

data class TrackedShipment(
    val id: String,
    val type: String,
    val status: MutableState<String>,
    val location: MutableState<String>,
    val expectedDelivery: MutableState<String>,
    val notes: SnapshotStateList<String>,
    val updates: SnapshotStateList<String>
)