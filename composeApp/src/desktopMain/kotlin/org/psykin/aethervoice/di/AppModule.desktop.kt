package org.psykin.aethervoice.di

import org.psykin.aethervoice.dao.DocumentRepository
import org.psykin.aethervoice.dao.JvmDocumentRepositoryImpl
import org.psykin.aethervoice.parser.DesktopDocumentParser
import org.psykin.aethervoice.parser.DocumentParser

actual class AppModule() {
    actual val documentRepository: DocumentRepository by lazy {
        JvmDocumentRepositoryImpl()
    }
    actual val documentParser: DocumentParser by lazy {
        DesktopDocumentParser()
    }
}