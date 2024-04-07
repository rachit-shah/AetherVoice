package org.psykin.aethervoice.dao

import org.psykin.aethervoice.DatabaseDriverFactory
import org.psykin.aethervoice.db.AetherVoiceDatabase

class IosDocumentRepositoryImpl : DocumentRepositoryImpl() {

    override var database: AetherVoiceDatabase = AetherVoiceDatabase(
        driver = DatabaseDriverFactory().create()
    )

}