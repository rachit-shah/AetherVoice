package org.psykin.aethervoice.database

import org.psykin.aethervoice.helpers.AppEnvironment
import java.io.File

internal object AppDataPathBuilder {

    private val appDataPath = when {
        System.getProperty("os.name").contains("Mac", true) -> {
            "${System.getProperty("user.home")}/Library/Application Support/AetherVoice"
        }
        System.getProperty("os.name").contains("windows", true) -> {
            "${System.getProperty("user.home")}\\AppData\\Local\\AetherVoice"
        }
        else -> {
            error("OS not supported")
        }
    }

    fun getAppDataPath(appEnvironment: AppEnvironment): String {
        val appPath = if (appEnvironment.isDebug()) {
            "$appDataPath-dev"
        } else {
            appDataPath
        }
        if (!File(appPath).exists()) {
            File(appPath).mkdirs()
        }
        return appPath
    }
}