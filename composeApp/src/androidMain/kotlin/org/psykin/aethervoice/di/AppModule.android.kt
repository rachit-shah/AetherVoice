package org.psykin.aethervoice.di

import android.content.Context
import org.psykin.aethervoice.DatabaseDriverFactory
import org.psykin.aethervoice.dao.DocumentRepository
import org.psykin.aethervoice.dao.DocumentRepositoryImpl
import org.psykin.aethervoice.database.AetherVoiceDatabase
import org.psykin.aethervoice.parser.AndroidDocumentParser
import org.psykin.aethervoice.parser.DocumentParser

actual class AppModule(private val context: Context) {
    actual val documentRepository: DocumentRepository by lazy {
        DocumentRepositoryImpl(
            database = AetherVoiceDatabase(
                driver = DatabaseDriverFactory(context).create()
            )
        )
    }
    actual val documentParser: DocumentParser by lazy {
        AndroidDocumentParser(context)
    }
}