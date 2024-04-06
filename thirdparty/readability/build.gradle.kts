import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
  androidTarget {
    compilations.all {
      kotlinOptions {
        jvmTarget = "1.8"
      }
    }
  }

  jvm()

  listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
    iosTarget.binaries.framework {
      baseName = "readability"
      isStatic = true
    }
  }

  sourceSets {
    commonMain.dependencies {
      implementation(libs.bundles.document.parsers.common)
      implementation(libs.uri)
    }
  }
}

android {
  namespace = "org.psykin.readability"
  compileSdk = libs.versions.android.compileSdk.get().toInt()
  defaultConfig { minSdk = libs.versions.android.minSdk.get().toInt() }
}
