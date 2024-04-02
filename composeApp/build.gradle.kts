import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.sqlDelight)
}

sqldelight {
    databases {
        create("AetherVoiceDatabase") {
            packageName.set("org.psykin.aethervoice.database")
        }
    }
    linkSqlite = true
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    
    jvm("desktop")
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        all {
            languageSettings.apply {
                optIn("kotlin.RequiresOptIn")
                optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
                optIn("kotlin.time.ExperimentalTime")
                optIn("kotlinx.cinterop.ExperimentalForeignApi")
                optIn("kotlinx.cinterop.BetaInteropApi")
            }
        }

        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.coroutines.core)
            implementation(libs.coroutines.android)
            implementation(libs.sqlDelight.android)
            implementation(libs.ktor.client.okHttp)
            implementation(libs.bundles.android.document.parsers)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.coroutines.core)
            implementation(libs.sqlDelight.coroutinesExt)
            implementation(libs.swipebox)
            implementation(libs.bundles.ktor.common)
            implementation(libs.bundles.document.parsers.common)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.uuid)
            implementation(libs.bundles.voyager.common)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.coroutines.swing)
            implementation(libs.ktor.client.okHttp)
            implementation(libs.sqlDelight.jvm)
            implementation(libs.bundles.android.document.parsers)
        }
        commonTest.dependencies {
            implementation(libs.coroutines.test)
        }
        iosMain.dependencies {
            implementation(libs.sqlDelight.native)
            implementation(libs.ktor.client.ios)
        }
    }
}

android {
    namespace = "org.psykin.aethervoice"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")
    sourceSets["main"].resources.srcDirs("src/main/res")

    defaultConfig {
        applicationId = "org.psykin.aethervoice"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/DEPENDENCIES"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.psykin.aethervoice"
            packageVersion = "1.0.0"
        }
    }
}
