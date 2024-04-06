package org.psykin.aethervoice.dao

import com.fleeksoft.ksoup.Ksoup
import org.psykin.aethervoice.database.AetherVoiceDatabase
import org.psykin.aethervoice.helpers.getCurrentTimestampRFC3339
import org.psykin.aethervoice.model.DocumentFormat
import org.psykin.aethervoice.model.Document as modelDocument

class DocumentRepositoryImpl(private val database: AetherVoiceDatabase) : DocumentRepository {
    override suspend fun addDocument(document: modelDocument) {
        val currentTimestamp = getCurrentTimestampRFC3339()
        database.documentQueries.insertDocument(
            document.id,
            document.title,
            document.content.html(),
            document.format.toString(),
            currentTimestamp,
            currentTimestamp
        )
    }

    override suspend fun getDocuments(): List<modelDocument> {
        return database.documentQueries.selectAllDocuments().executeAsList().map { row ->
            modelDocument(
                id = row.id,
                title = row.title,
                content = Ksoup.parse(row.content),
                format = DocumentFormat.valueOf(row.format),
                createdAt = row.createdAt,
                updatedAt = row.updatedAt
            )
        }
    }

    override suspend fun getDocumentById(id: String): modelDocument {
        val dbDoc = database.documentQueries.selectDocumentById(id).executeAsOne()
        return modelDocument(
            id = dbDoc.id,
            title = dbDoc.title,
            content = Ksoup.parse(dbDoc.content),
            format = DocumentFormat.valueOf(dbDoc.format),
            createdAt = dbDoc.createdAt,
            updatedAt = dbDoc.updatedAt
        )
    }

    override suspend fun updateDocument(document: modelDocument) {
        val currentTimestamp = getCurrentTimestampRFC3339()
        database.documentQueries.updateDocument(
            document.title,
            document.content.html(),
            document.format.toString(),
            currentTimestamp,
            document.id
        )
    }

    override suspend fun deleteDocument(id: String) {
        database.documentQueries.deleteDocument(id)
    }
}