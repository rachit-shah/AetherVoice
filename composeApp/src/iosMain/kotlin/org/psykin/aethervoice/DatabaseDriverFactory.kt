package org.psykin.aethervoice

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import org.psykin.aethervoice.database.AetherVoiceDatabase

actual class DatabaseDriverFactory {
    actual fun create(): SqlDriver {
        return NativeSqliteDriver(
            AetherVoiceDatabase.Schema,
            "document.db"
        )
    }
}