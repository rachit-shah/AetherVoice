import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.sqlDelight)
}

sqldelight {
    databases {
        create("AetherVoiceDatabase") {
            packageName.set("org.psykin.aethervoice.db")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/org/psykin/aethervoice/schema"))
            verifyMigrations.set(true)
        }
    }
    linkSqlite = true
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
  androidTarget {
    compilations.all {
      kotlinOptions {
        jvmTarget = "21"
      }
    }
  }

  jvm {
    jvmToolchain(21)
  }

  iosX64()
  iosArm64()
  iosSimulatorArm64()

  compilerOptions { freeCompilerArgs.add("-Xexpect-actual-classes") }

  sourceSets {
    all {
      languageSettings.apply {
        optIn("kotlin.experimental.ExperimentalObjCName")
        optIn("kotlin.RequiresOptIn")
        optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
        optIn("kotlin.time.ExperimentalTime")
        optIn("kotlinx.cinterop.ExperimentalForeignApi")
        optIn("kotlinx.cinterop.BetaInteropApi")
      }
    }
    commonMain {
      dependencies {
        implementation(libs.sqlDelight.runtime)
        implementation(libs.sqlDelight.coroutinesExt)
        implementation(libs.sqlDelight.primitive.adapter)
        implementation(libs.kermit)
        implementation(libs.ksoup)
        implementation(libs.kotlinx.datetime)
        implementation(libs.kotlinx.uuid)
      }
    }

    androidMain {
      dependencies {
        implementation(libs.sqlDelight.android)
      }
    }

    iosMain {
      dependencies {
        implementation(libs.sqlDelight.native)
      }
    }
    jvmMain {
      dependencies {
        implementation(libs.sqlDelight.jvm)
      }
    }
  }
  task("testClasses")
}

android {
  namespace = "org.psykin.aethervoice.database"
  compileSdk = libs.versions.android.compileSdk.get().toInt()
  defaultConfig { minSdk = libs.versions.android.minSdk.get().toInt() }
}
