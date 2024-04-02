package org.psykin.aethervoice.di

import org.psykin.aethervoice.DatabaseDriverFactory
import org.psykin.aethervoice.dao.DocumentRepository
import org.psykin.aethervoice.dao.DocumentRepositoryImpl
import org.psykin.aethervoice.database.AetherVoiceDatabase
import org.psykin.aethervoice.parser.DesktopDocumentParser
import org.psykin.aethervoice.parser.DocumentParser

actual class AppModule() {
    actual val documentRepository: DocumentRepository by lazy {
        DocumentRepositoryImpl(
            database = AetherVoiceDatabase(
                driver = DatabaseDriverFactory().create()
            )
        )
    }
    actual val documentParser: DocumentParser by lazy {
        DesktopDocumentParser()
    }
}