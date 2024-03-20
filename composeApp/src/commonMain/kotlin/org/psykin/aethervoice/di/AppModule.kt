package org.psykin.aethervoice.di

import org.psykin.aethervoice.dao.DocumentRepository

expect class AppModule {
    val documentRepository: DocumentRepository
}