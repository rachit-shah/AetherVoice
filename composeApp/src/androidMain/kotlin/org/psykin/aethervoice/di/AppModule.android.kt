package org.psykin.aethervoice.di

import android.content.Context
import org.psykin.aethervoice.dao.AndroidDocumentRepositoryImpl
import org.psykin.aethervoice.dao.DocumentRepository
import org.psykin.aethervoice.parser.AndroidDocumentParser
import org.psykin.aethervoice.parser.DocumentParser

actual class AppModule(private val context: Context) {
    actual val documentRepository: DocumentRepository by lazy {
        AndroidDocumentRepositoryImpl(context)
    }
    actual val documentParser: DocumentParser by lazy {
        AndroidDocumentParser(context)
    }
}