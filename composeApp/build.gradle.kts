import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    kotlin("plugin.serialization") version "2.2.0"
}

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlinx" && requested.name.startsWith("kotlinx-coroutines")) {
            useVersion("1.7.3")
        }
    }
}

kotlin {
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.runtimeCompose)

                implementation("io.ktor:ktor-client-core:2.3.5")
                implementation("io.ktor:ktor-client-cio:2.3.5")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.5")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5")

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutinesSwing)

                implementation("io.ktor:ktor-server-core:2.3.5")
                implementation("io.ktor:ktor-server-netty:2.3.5")
                implementation("io.ktor:ktor-server-content-negotiation:2.3.5")
                implementation("io.ktor:ktor-server-call-logging:2.3.5")
                implementation("ch.qos.logback:logback-classic:1.4.11")
                resources.srcDirs("src/desktopMain/resources")
            }
        }

        val desktopTest by getting {
            dependencies {
                implementation(libs.kotlin.test)

                // ✅ Ensure correct Kotest modules are here
                implementation("io.kotest:kotest-runner-junit5:5.9.0")
                implementation("io.kotest:kotest-assertions-core:5.9.0")
                implementation("io.kotest:kotest-framework-engine:5.9.0")
                implementation("org.jetbrains.kotlin:kotlin-reflect")
                implementation("io.ktor:ktor-server-test-host:2.3.5")

            }
        }
    }
}

// ✅ Ensure JUnit Platform is enabled for all test tasks
tasks.withType<Test>().configureEach {
    useJUnitPlatform()
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