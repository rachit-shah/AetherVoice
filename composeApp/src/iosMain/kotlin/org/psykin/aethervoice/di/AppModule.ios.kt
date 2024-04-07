package org.psykin.aethervoice.di

import org.psykin.aethervoice.dao.DocumentRepository
import org.psykin.aethervoice.dao.IosDocumentRepositoryImpl
import org.psykin.aethervoice.parser.DocumentParser
import org.psykin.aethervoice.parser.IosDocumentParser

actual class AppModule() {
    actual val documentRepository: DocumentRepository by lazy {
        IosDocumentRepositoryImpl()
    }
    actual val documentParser: DocumentParser by lazy {
        IosDocumentParser()
    }
}