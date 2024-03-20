package org.psykin.aethervoice.dao

import org.psykin.aethervoice.database.Document

interface DocumentRepository {
    suspend fun addDocument(document: Document)
    suspend fun getDocuments(): List<Document>
    suspend fun getDocumentById(id: String): Document?
    suspend fun updateDocument(document: Document)
    suspend fun deleteDocument(id: String)
}