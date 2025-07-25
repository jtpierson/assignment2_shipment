package org.example.project


import androidx.compose.runtime.*

import org.example.project.tracking.TrackerViewHelper
import org.example.project.ui.UserInterface

@Composable
fun App() {
    val viewHelper = remember { TrackerViewHelper() }

    UserInterface(viewHelper)
}