package org.psykin.aethervoice

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.psykin.aethervoice.database.AetherVoiceDatabase

actual class DatabaseDriverFactory {
    actual fun create(): SqlDriver {
        return JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply{
            AetherVoiceDatabase.Schema.create(this)
        }
    }
}