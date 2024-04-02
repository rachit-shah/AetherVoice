package org.psykin.aethervoice.di

import org.psykin.aethervoice.dao.DocumentRepository
import org.psykin.aethervoice.parser.DocumentParser

expect class AppModule {
    val documentParser: DocumentParser
    val documentRepository: DocumentRepository
}