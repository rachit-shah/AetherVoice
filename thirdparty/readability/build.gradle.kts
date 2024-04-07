import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
  androidTarget {
    compilations.all {
      kotlinOptions {
        jvmTarget = "21"
      }
    }
  }

  jvm() {
    jvmToolchain(21)
  }

  iosX64()
  iosArm64()
  iosSimulatorArm64()

  sourceSets {
    commonMain.dependencies {
      implementation(libs.bundles.document.parsers.common)
      implementation(libs.uri)
    }
  }
  task("testClasses")
}

android {
  namespace = "org.psykin.readability"
  compileSdk = libs.versions.android.compileSdk.get().toInt()
  defaultConfig { minSdk = libs.versions.android.minSdk.get().toInt() }
}
