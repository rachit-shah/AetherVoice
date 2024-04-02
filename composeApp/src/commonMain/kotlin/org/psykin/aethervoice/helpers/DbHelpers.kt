package org.psykin.aethervoice.helpers

import kotlinx.datetime.Clock
import kotlinx.uuid.UUID
import kotlinx.uuid.generateUUID
import org.psykin.aethervoice.model.DocumentFormat

fun generateUniqueId(): String {
    return UUID.generateUUID().toString()
}

fun getDocumentFormatFromUri(uri: String): DocumentFormat {
    val extension = uri.substringAfterLast('.', "").lowercase()
    return when (extension) {
        "txt" -> DocumentFormat.TXT
        "pdf" -> DocumentFormat.PDF
        "epub" -> DocumentFormat.EPUB
        "doc", "docx" -> DocumentFormat.DOCX
        else -> DocumentFormat.UNKNOWN
    }
}

fun getCurrentTimestampRFC3339(): String {
    return Clock.System.now().toString()
}