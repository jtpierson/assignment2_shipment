import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    kotlin("plugin.serialization") version "2.2.0"
}

kotlin {
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Core HTTP client and CIO engine
            implementation("io.ktor:ktor-client-core:2.3.5")
            implementation("io.ktor:ktor-client-cio:2.3.5")

            // For sending JSON and deserializing response bodies
            implementation("io.ktor:ktor-client-content-negotiation:2.3.5")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5")

            // Kotlinx Serialization JSON
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
        }
        val desktopTest by getting
        desktopTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotest.api)
            implementation(libs.kotest.engine)
            implementation(libs.kotest.assertions)
            implementation(libs.kotest.runner)
            implementation("org.jetbrains.kotlin:kotlin-reflect")

        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}


compose.desktop {
    application {
        mainClass = "org.example.project.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.example.project"
            packageVersion = "1.0.0"
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

