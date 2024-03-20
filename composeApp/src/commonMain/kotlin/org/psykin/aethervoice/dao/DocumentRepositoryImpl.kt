package org.psykin.aethervoice.dao

import org.psykin.aethervoice.database.AetherVoiceDatabase
import org.psykin.aethervoice.database.Document

class DocumentRepositoryImpl(private val database: AetherVoiceDatabase) : DocumentRepository {
    override suspend fun addDocument(document: Document) {
        val currentTimestamp = getCurrentTimestampRFC3339()
        database.documentQueries.insertDocument(
            document.id,
            document.title,
            document.content,
            document.format,
            currentTimestamp,
            currentTimestamp
        )
    }

    override suspend fun getDocuments(): List<Document> {
        return database.documentQueries.selectAllDocuments().executeAsList().map { row ->
            Document(
                id = row.id,
                title = row.title,
                content = row.content,
                format = row.format,
                createdAt = row.createdAt,
                updatedAt = row.updatedAt
            )
        }
    }

    override suspend fun getDocumentById(id: String): Document {
        return database.documentQueries.selectDocumentById(id).executeAsOne()
    }

    override suspend fun updateDocument(document: Document) {
        val currentTimestamp = getCurrentTimestampRFC3339()
        database.documentQueries.updateDocument(
            document.title,
            document.content,
            document.format,
            currentTimestamp,
            document.id
        )
    }

    override suspend fun deleteDocument(id: String) {
        database.documentQueries.deleteDocument(id)
    }

    private fun getCurrentTimestampRFC3339(): String {
        return kotlinx.datetime.Clock.System.now().toString()
    }
}