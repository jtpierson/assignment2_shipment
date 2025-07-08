package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import assignment2_shipment.composeapp.generated.resources.Res
import assignment2_shipment.composeapp.generated.resources.compose_multiplatform
import org.example.project.tracking.TrackerViewHelper
import org.example.project.tracking.TrackingSimulator
import org.example.project.ui.UserInterface
import java.io.File

@Composable
fun App() {
    val viewHelper = remember { TrackerViewHelper() }

    LaunchedEffect(Unit) {
        val file = File("../test.txt")
        if (file.exists()) {
            try {
                TrackingSimulator.runSimulation(file)
            } catch (e: Exception) {
                println("Simulation error: ${e.message}")
                e.printStackTrace()
            }
        } else {
            println("File not found!")
        }
    }

    UserInterface(viewHelper)
}