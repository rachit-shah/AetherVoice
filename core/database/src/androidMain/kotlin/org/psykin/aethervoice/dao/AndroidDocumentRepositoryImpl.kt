package org.psykin.aethervoice.dao

import android.content.Context
import org.psykin.aethervoice.DatabaseDriverFactory
import org.psykin.aethervoice.db.AetherVoiceDatabase

class AndroidDocumentRepositoryImpl(context: Context) : DocumentRepositoryImpl() {

    override var database: AetherVoiceDatabase

    init {
        this.database = AetherVoiceDatabase(
            driver = DatabaseDriverFactory(context).create()
        )
    }
}