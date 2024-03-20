package org.psykin.aethervoice

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.psykin.aethervoice.database.AetherVoiceDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun create(): SqlDriver {
        return AndroidSqliteDriver(
            AetherVoiceDatabase.Schema,
            context,
            "document.db"
        )
    }
}