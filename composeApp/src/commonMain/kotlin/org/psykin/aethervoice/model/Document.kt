package org.psykin.aethervoice.model

data class Document(
    var id: String,
    var title: String,
    var content: String,
    var format: DocumentFormat,
    var createdAt: String,
    var updatedAt: String,
    var checked: Boolean = false
)
