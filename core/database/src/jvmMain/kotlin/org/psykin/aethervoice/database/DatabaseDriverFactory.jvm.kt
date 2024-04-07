package org.psykin.aethervoice

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import co.touchlab.kermit.Logger
import org.psykin.aethervoice.database.AppDataPathBuilder
import org.psykin.aethervoice.db.AetherVoiceDatabase
import org.psykin.aethervoice.helpers.AppEnvironment
import java.io.File
import java.util.Properties

actual class DatabaseDriverFactory {
    actual fun create(): SqlDriver {
        val properties = Properties()
        val isRelease = properties["is_release"]
            ?.toString()
            ?.toBooleanStrictOrNull()
            ?: false
        val appEnvironment = if (isRelease) {
            AppEnvironment.Release
        } else {
            AppEnvironment.Debug
        }
        val appPath = AppDataPathBuilder.getAppDataPath(appEnvironment)

        val databasePath = File(appPath, "/document.db")

        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY + databasePath.absolutePath, Properties())

        val sqlCursor = driver.executeQuery(
            null,
            "PRAGMA user_version;",
            {
                QueryResult.Value(it.getLong(0))
            },
            0,
            null,
        )
        val currentVer: Long = sqlCursor.value ?: -1L

        if (currentVer == 0L) {
            AetherVoiceDatabase.Schema.create(driver)
            setVersion(driver, AetherVoiceDatabase.Schema.version)
            Logger.d("init: created tables, setVersion to ${AetherVoiceDatabase.Schema.version}")
        } else {
            val schemaVer = AetherVoiceDatabase.Schema.version
            if (schemaVer > currentVer) {
                AetherVoiceDatabase.Schema.migrate(driver, oldVersion = currentVer, newVersion = schemaVer)
                setVersion(driver, schemaVer)
                Logger.d("init: migrated from $currentVer to $schemaVer")
            } else {
                Logger.d("init with existing database")
            }
        }
        return driver
    }

    fun setVersion(driver: SqlDriver, version: Long) {
        driver.execute(null, "PRAGMA user_version = $version;", 0, null)
    }
}